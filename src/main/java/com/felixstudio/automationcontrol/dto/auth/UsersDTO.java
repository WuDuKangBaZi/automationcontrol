package com.felixstudio.automationcontrol.dto.auth;

import com.felixstudio.automationcontrol.entity.auth.Users;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UsersDTO extends Users {
    private String roleNames;
}
