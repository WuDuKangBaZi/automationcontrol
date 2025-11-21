package com.felixstudio.automationcontrol.service.auth;

import com.felixstudio.automationcontrol.entity.auth.Roles;
import com.felixstudio.automationcontrol.mapper.auth.RolesMapper;
import com.felixstudio.automationcontrol.mapper.auth.UserRolesMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserRolesService {
    private final UserRolesMapper userRolesMapper;
    private final RolesMapper rolesMapper;

    public UserRolesService(UserRolesMapper userRolesMapper, RolesMapper rolesMapper) {
        this.userRolesMapper = userRolesMapper;
        this.rolesMapper = rolesMapper;
    }

    public List<String> getRolesByUserId(Long id) {
        return this.userRolesMapper.getRoleNamesByUserId(id);
    }
    public boolean setUserRoles(Long userId, List<String> roleIds) {
        // 先删除用户已有角色
        Map<String,Object> cond = new HashMap<>();
        cond.put("user_id", userId);
        userRolesMapper.deleteByMap(cond);
        // 再批量插入新角色
        List<Long> roleIdList = roleIds.stream().map(roleName -> {
            Roles rols = rolesMapper.selectByMap(Map.of("role_name",roleName)).get(0);
            return rols.getId();
        }).toList();
        int inserted = userRolesMapper.insertUserRoles(userId, roleIdList);
        return inserted > 0;
    }
}
