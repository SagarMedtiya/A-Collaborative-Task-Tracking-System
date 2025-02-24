package com.airtribe.TaskTrackingAndManagement.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String Username;
    private String password;
    private String email;
    private Set<String> roles;
}
