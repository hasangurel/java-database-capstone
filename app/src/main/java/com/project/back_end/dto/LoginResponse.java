package com.project.back_end.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for login response containing JWT token and user information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String role;
    private Long userId;
    private String username;
    private String message;

    public LoginResponse(String token, String role, Long userId, String username) {
        this.token = token;
        this.role = role;
        this.userId = userId;
        this.username = username;
        this.message = "Login successful";
    }
}
