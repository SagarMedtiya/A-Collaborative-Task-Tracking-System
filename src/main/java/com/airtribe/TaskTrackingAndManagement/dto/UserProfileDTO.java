package com.airtribe.TaskTrackingAndManagement.dto;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Data
@NoArgsConstructor
@Getter
@Setter
public class UserProfileDTO {
    private Long id;
    private String userName;
    private String email;
    private String password;
    private Set<String> roles;
    public UserProfileDTO(Long id, String username, String email, String password) {
        this.id = id;
        this.userName = username;
        this.email = email;
        this.password = null;
    }
    public UserProfileDTO(Long id, String username, String email, Set<String> roles) {
        this.id = id;
        this.userName = username;
        this.email = email;
        this.roles = roles;
    }
    public UserProfileDTO(Long id, String username, String email) {
        this.id = id;
        this.userName = username;
        this.email = email;
    }
}
