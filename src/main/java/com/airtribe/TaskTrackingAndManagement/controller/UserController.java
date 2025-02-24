package com.airtribe.TaskTrackingAndManagement.controller;

import com.airtribe.TaskTrackingAndManagement.dto.LoginRequest;
import com.airtribe.TaskTrackingAndManagement.dto.RegisterRequest;
import com.airtribe.TaskTrackingAndManagement.dto.UserProfileDTO;
import com.airtribe.TaskTrackingAndManagement.repository.UserRepository;
import com.airtribe.TaskTrackingAndManagement.security.TokenBlacklist;
import com.airtribe.TaskTrackingAndManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenBlacklist tokenBlacklist;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Register endpoint
    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    //Login Endpoint
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    //view the current user's profile
    @GetMapping("/users/me")
    public ResponseEntity<?> getCurrentUserProfile() {
       //Retrieve the authenticated user's details from the SecurityContext
        return ResponseEntity.ok(userService.profile());
    }

    //update the details of the current user
    @PutMapping("/users/me")
    public ResponseEntity<?> updateCurrentProfile(@RequestBody UserProfileDTO updateUserProfileDTO){
        return ResponseEntity.ok(userService.updateProfile(updateUserProfileDTO));
    }
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader) {
        //Extract the token from the Authorization header
        String token = authorizationHeader.substring(7);

        //Add the token to the blacklist
        tokenBlacklist.addToBlacklist(token);

        //Invalidate the user's authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            SecurityContextHolder.clearContext();
        }
        return ResponseEntity.ok("Logged out successfully");
    }
}
