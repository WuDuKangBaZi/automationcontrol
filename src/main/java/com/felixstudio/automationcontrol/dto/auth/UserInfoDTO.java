package com.felixstudio.automationcontrol.dto.auth;

import com.felixstudio.automationcontrol.entity.auth.Users;
import lombok.Data;

import java.util.List;

@Data
public class UserInfoDTO {
    private String userId;
    private String account;
    private String username;
    private List<String> roles;
    private List<String> permissions;
    private String avatar;
    private String phone;
    private String groupName;

    public UserInfoDTO forEntity(Users user) {
        this.userId = String.valueOf(user.getId());
        this.account = user.getAccount();
        this.username = user.getUsername();
        this.avatar = user.getAvatarUrl();
        this.phone = user.getPhone();
        return this;
    }
}
