package com.airtribe.TaskTrackingAndManagement.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private Long id;
    private String username;
    private String jwtToken;
}
