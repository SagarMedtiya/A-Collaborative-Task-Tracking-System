package com.airtribe.TaskTrackingAndManagement.controller;

import com.airtribe.TaskTrackingAndManagement.entity.Attachment;
import com.airtribe.TaskTrackingAndManagement.entity.Comment;
import com.airtribe.TaskTrackingAndManagement.entity.Task;
import com.airtribe.TaskTrackingAndManagement.repository.CommentRepository;
import com.airtribe.TaskTrackingAndManagement.repository.TaskRepository;
import com.airtribe.TaskTrackingAndManagement.repository.UserRepository;
import com.airtribe.TaskTrackingAndManagement.service.AIService;
import com.airtribe.TaskTrackingAndManagement.service.AttachmentService;
import com.airtribe.TaskTrackingAndManagement.service.CommentService;
import com.airtribe.TaskTrackingAndManagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final AttachmentService attachmentService;
    @Autowired
    private AIService aiService;

    public TaskController(TaskService taskService, TaskRepository taskRepository, UserRepository userRepository, CommentRepository commentRepository, CommentService commentService, AttachmentService attachmentService) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.commentService = commentService;
        this.commentRepository = commentRepository;
        this.attachmentService = attachmentService;

    }
    //create a task endpoint
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }
    //Get all Tasks endpoint
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }
    //Get Task bY ID endpoint
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    //update the task
    @PostMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task task) {
        return taskService.updateTask(id);
    }
    //Mark Task's status as completed
    @PutMapping("/{id}/complete")
    public Task markTaskAsCompleted(@PathVariable Long id) {
        return taskService.markTaskAsCompleted(id);
    }
    //Assign tasks to different users
    @PutMapping("/{id}/assign")
    public Task assignTaskToUser(@PathVariable Long id, @RequestParam("userName") String userName) {
        return taskService.assignTaskToUser(id, userName);
    }
    //delete a task
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
    //Filter by status
    @GetMapping("/filter")
    public List<Task> getTasksByStatus(@RequestParam boolean completed) {
        return taskService.getTaskByStatus(completed);
    }
    //Search the task
    @GetMapping("/search")
    public List<Task> searchTasks(@RequestParam String query){
        return taskService.searchTasks(query);
    }
    //add comments to the task
    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addCommentsToTask(@PathVariable Long id, @RequestBody String content) {
        return commentService.addCommentsToTask(id, content);
    }
    //add the attachments
    @PostMapping("/{id}/attachments")
    public ResponseEntity<Attachment> addAttachmentsToTask(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return attachmentService.addAttachmentsToTask(id, file);
    }
    //generate AI description
    @PostMapping("/generate-description")
    public ResponseEntity<String> generateDescription(@RequestParam String prompt) {
        String description = aiService.generateTaskDescription(prompt);
        return ResponseEntity.ok(description);
    }
}
