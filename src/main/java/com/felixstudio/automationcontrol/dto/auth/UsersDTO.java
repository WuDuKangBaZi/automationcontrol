package com.felixstudio.automationcontrol.dto.auth;

import com.felixstudio.automationcontrol.entity.auth.Users;
import lombok.Data;

import java.util.List;
@Data
public class UsersDTO extends Users {
    private String roleNames;
}
