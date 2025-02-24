package com.airtribe.TaskTrackingAndManagement.service;

import com.airtribe.TaskTrackingAndManagement.entity.Project;
import com.airtribe.TaskTrackingAndManagement.entity.Task;
import com.airtribe.TaskTrackingAndManagement.entity.User;
import com.airtribe.TaskTrackingAndManagement.repository.ProjectRepository;
import com.airtribe.TaskTrackingAndManagement.repository.TaskRepository;
import com.airtribe.TaskTrackingAndManagement.repository.UserRepository;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final NotificationService notificationService;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, ProjectRepository projectRepository, NotificationService notificationService){
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.notificationService = notificationService;
    }
    public Task createTask(Task task){
        //Retrieve the authenticated user's database from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        //Fetch the current user from the database
        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        //assign the task to the current user
        task.setAssignedTo(currentUser);

        //Save the task to the database
        Task savedTask = taskRepository.save(task);

        //send notification to the assigned user
        notificationService.sendNotification(userDetails.getUsername(), "You have been assigned a new task: "+ task.getTitle());
        return savedTask;
    }
    public List<Task> getAllTasks(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return taskRepository.findByAssignedToUsername(userDetails.getUsername());
    }
    public Task getTaskById(long id){
        return taskRepository.findById(id).orElseThrow(()-> new RuntimeException("Task not found"));
    }

    public Task markTaskAsCompleted(Long id){
        if(id == null){
            throw new IllegalArgumentException("Task id cannot be null");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        //Fetch the task by ID
        Task task = taskRepository.findById(id).orElseThrow(()-> new RuntimeException("Task not found"));

        //Ensure the task is assigned to the current USer
        if(!task.getAssignedTo().getUsername().equals(userDetails.getUsername())){
            throw new RuntimeException("You are not authorised to complete this task");
        }
        task.setCompleted(true);
        //save the updated task to the database
        return taskRepository.save(task);
    }
    public void deleteTask(long id){
        taskRepository.deleteById(id);
    }

    public Task assignTaskToUser(Long id, String userName) {
        //retrieve the authenticated user's details from the securitycontext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Task task = taskRepository.findById(id).orElseThrow(()-> new RuntimeException("Task not found"));

        //ensure the task is assigned to the current user(only the assigner can reassign)
        if(!task.getAssignedTo().getUsername().equals(userDetails.getUsername())){
            throw new RuntimeException("You are not authorised to assign this task as user is not present");
        }
        User newAssignee = userRepository.findByUsername(userName).orElseThrow(()-> new RuntimeException("User not found"));

        //assign the task
        task.setAssignedTo(newAssignee);
        return taskRepository.save(task);
    }

    public List<Task> getTaskByStatus(boolean completed) {
        //retrieve the authenticated user's details from the securitycontext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();


        return taskRepository.findByAssignedToUsernameAndCompleted(userDetails.getUsername(), completed);
    }

    //search by title or description
    public List<Task> searchTasks(String query){
        return taskRepository.searchTasks(query);
    }

    public Task updateTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

        Task updatedTask =taskRepository.save(task);
        // send a notification to the assigned user
        notificationService.sendNotification(task.getAssignedTo().getUsername(), "Task updated: "+ task.getTitle());

        return updatedTask;
    }
}
