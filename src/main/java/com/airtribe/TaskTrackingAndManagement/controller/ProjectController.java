package com.airtribe.TaskTrackingAndManagement.controller;

import com.airtribe.TaskTrackingAndManagement.dto.ProjectDTO;
import com.airtribe.TaskTrackingAndManagement.entity.Project;
import com.airtribe.TaskTrackingAndManagement.repository.ProjectRepository;
import com.airtribe.TaskTrackingAndManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.airtribe.TaskTrackingAndManagement.entity.User;
@RestController
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;
    @PostMapping
    @Transactional
    public ResponseEntity<Project> createProject(@RequestBody ProjectDTO projectDTO) {
        //Retrieve the authenticated user's details from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Project project = new Project();
        //Fetch the current user
        User owner = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(()-> new RuntimeException("User not found"));

        //Set the project owner
        project.setOwner(owner);
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        //add the owner as a member of the project
        project.getMembers().add(owner);

        //save the project to the database
        Project savedProject = projectRepository.save(project);
        return ResponseEntity.ok(savedProject);

    }
    @PostMapping("/{projectName}/invite")
    @Transactional
    public ResponseEntity<?> inviteProject(@PathVariable String projectName, @RequestParam String userName){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        //Fetch the project by name
        Project project = projectRepository.findByName(projectName).orElseThrow(()-> new RuntimeException("Project not found"));

        //Ensure the authenticated user is the project owner
        if(!project.getOwner().getUsername().equals(userDetails.getUsername())){
            throw new RuntimeException("Only the project owner can invite members");
        }
        //Fetch the user to be invited by username
        User user = userRepository.findByUsername(userName).orElseThrow(()-> new RuntimeException("User not found"));

        //CHeck if the user is already a member of the project
        if(project.getMembers().contains(user)){
            throw new RuntimeException("User is already a member of this project");
        }
        // Add the user to the project members
        project.getMembers().add(user);
        //Save the uupdated project to the database
        projectRepository.save(project);
        return ResponseEntity.ok(new ProjectDTO(project));
    }
}
