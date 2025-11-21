package com.felixstudio.automationcontrol.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String userId;
    private String account;
    private String username;
    private List<String> roles;
    private List<String> permissions;
    private String token;
    private Long expiresAt;
    private String avatar;
    private String phone;
}
