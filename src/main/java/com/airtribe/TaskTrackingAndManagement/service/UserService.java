package com.airtribe.TaskTrackingAndManagement.service;

import com.airtribe.TaskTrackingAndManagement.dto.LoginRequest;
import com.airtribe.TaskTrackingAndManagement.dto.LoginResponse;
import com.airtribe.TaskTrackingAndManagement.dto.RegisterRequest;
import com.airtribe.TaskTrackingAndManagement.dto.UserProfileDTO;
import com.airtribe.TaskTrackingAndManagement.entity.User;
import com.airtribe.TaskTrackingAndManagement.repository.UserRepository;
import com.airtribe.TaskTrackingAndManagement.util.jwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final com.airtribe.TaskTrackingAndManagement.util.jwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, jwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }
    public User register(RegisterRequest request) {

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(request.getRoles() != null && !request.getRoles().isEmpty() ? request.getRoles() : Set.of("USER"));

        return userRepository.save(user);
    }


    public Object login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = jwtUtil.generateToken(userDetails);
        return new LoginResponse(user.getId(), user.getUsername(), token);
    }

    public Object profile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not Authenticated");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        UserProfileDTO userProfile = new UserProfileDTO(user.getId(), user.getUsername(), user.getEmail(), Collections.singleton(user.getRoles().toString()));
        return userProfile;
    }

    public User updateProfile(UserProfileDTO updateUserProfileDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        if(updateUserProfileDTO.getUserName() != null){
            user.setUsername(updateUserProfileDTO.getUserName());
        }
        if(updateUserProfileDTO.getEmail() != null){
            user.setEmail(updateUserProfileDTO.getEmail());
        }
        if(updateUserProfileDTO.getPassword() != null && !updateUserProfileDTO.getPassword().isBlank()){
            user.setPassword(passwordEncoder.encode(updateUserProfileDTO.getPassword()));
        }
        if(updateUserProfileDTO.getRoles() !=null && !updateUserProfileDTO.getRoles().isEmpty()){
            user.setRoles(updateUserProfileDTO.getRoles());
        }
        return userRepository.save(user);
    }
}
