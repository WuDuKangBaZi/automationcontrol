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
public class RolePermission {
    private Long id;
    private Long roleId;            // 角色ID
    private Long permissionId;      // 权限ID
    private LocalDateTime createdTime;
}
