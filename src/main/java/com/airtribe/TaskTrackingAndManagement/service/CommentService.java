package com.airtribe.TaskTrackingAndManagement.service;

import com.airtribe.TaskTrackingAndManagement.entity.Comment;
import com.airtribe.TaskTrackingAndManagement.entity.Task;
import com.airtribe.TaskTrackingAndManagement.entity.User;
import com.airtribe.TaskTrackingAndManagement.repository.CommentRepository;
import com.airtribe.TaskTrackingAndManagement.repository.TaskRepository;
import com.airtribe.TaskTrackingAndManagement.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }
    public ResponseEntity<Comment> addCommentsToTask(Long id, String content) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        //Fetch the task by Id
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

        //Fetch the Current user
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));

        //Create a new comment
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setTask(task);
        //save the comment to the database
        Comment savedComment = commentRepository.save(comment);
        return ResponseEntity.ok(savedComment);
    }
}
