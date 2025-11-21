package com.felixstudio.automationcontrol.service.auth;

import com.felixstudio.automationcontrol.entity.auth.Roles;
import com.felixstudio.automationcontrol.mapper.auth.RolesMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolesService {
    private final RolesMapper rolesMapper;

    public RolesService(RolesMapper rolesMapper) {
        this.rolesMapper = rolesMapper;
    }

    public List<Roles> getAllRoles() {
        return rolesMapper.selectList(null);
    }
}
