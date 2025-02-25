package com.airtribe.TaskTrackingAndManagement.service;


import com.airtribe.TaskTrackingAndManagement.entity.Attachment;
import com.airtribe.TaskTrackingAndManagement.entity.User;
import com.airtribe.TaskTrackingAndManagement.repository.AttachmentRepository;
import com.airtribe.TaskTrackingAndManagement.repository.TaskRepository;
import com.airtribe.TaskTrackingAndManagement.repository.UserRepository;
import com.airtribe.TaskTrackingAndManagement.util.FileStorageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.airtribe.TaskTrackingAndManagement.entity.Task;

import java.io.IOException;


@Service
@Transactional
public class AttachmentService {
    @Autowired
    private FileStorageUtil fileStorageUtil;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;

    public AttachmentService(TaskRepository taskRepository, UserRepository userRepository, AttachmentRepository attachmentRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.attachmentRepository = attachmentRepository;
    }

    public ResponseEntity<Attachment> addAttachmentsToTask(Long id, MultipartFile file) {
        //Fetch the current User
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        //Fetch the task by ID
        Task task = taskRepository.findById(id).orElseThrow(()-> new RuntimeException("Task not Found"));

        //Fetch the user
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(()-> new RuntimeException("User not Found"));

        //save the file to the filesystem or cloud storage
        String filePath ;
        try{
            filePath = fileStorageUtil.saveFile(file);
        }catch (IOException e){
            throw new RuntimeException("File storage error");
        }

        //create a new attachment
        Attachment attachment = new Attachment();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFilePath(filePath);
        attachment.setTask(task);
        attachment.setUser(user);


        //save the attachment to the database
        Attachment savedAttachment = attachmentRepository.save(attachment);
        return ResponseEntity.ok(savedAttachment);

    }
}
