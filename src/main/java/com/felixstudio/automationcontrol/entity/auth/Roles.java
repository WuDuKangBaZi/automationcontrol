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
public class Roles {
    private Long id;
    private String roleCode;        // 角色代码，如：ADMIN, USER
    private String roleName;        // 角色名称
    private String description;     // 角色描述
    private Integer status;         // 状态：1-启用 0-禁用
    private LocalDateTime createdTime;
}
