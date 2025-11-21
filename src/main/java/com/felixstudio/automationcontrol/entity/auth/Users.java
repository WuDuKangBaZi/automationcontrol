package com.felixstudio.automationcontrol.entity.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String account;          // 登录账号
    private String username;         // 显示名称
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;         // 密码（加密后的）
    private String phone;            // 手机号
    private Integer status;          // 状态：1-正常 0-禁用
    private LocalDateTime createdTime; // 创建时间
    private LocalDateTime updatedTime; // 更新时间
    private String avatarUrl; //头像URL

}
