package com.felixstudio.automationcontrol.entity.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoles {
    private Long id;
    private Long userId;            // 用户ID
    private Long roleId;            // 角色ID
    private LocalDateTime createdTime;
}
