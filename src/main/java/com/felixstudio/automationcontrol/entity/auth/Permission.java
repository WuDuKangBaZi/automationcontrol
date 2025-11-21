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
public class Permission {
    private Long id;
    private String permCode;        // 权限代码，如：USER:CREATE
    private String permName;        // 权限名称
    private String module;          // 模块分类
    private String description;     // 权限描述
    private LocalDateTime createdTime;
}