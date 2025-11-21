package com.felixstudio.automationcontrol.dto.auth;

import com.felixstudio.automationcontrol.entity.auth.Users;
import lombok.Data;

@Data
public class UserRegisterDTO {
    private Users users;
    private int deptId;
}
