package com.felixstudio.automationcontrol.service.auth;

import com.felixstudio.automationcontrol.mapper.auth.PermissionsMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionsService {
    private final PermissionsMapper permissionsMapper;

    public PermissionsService(PermissionsMapper permissionsMapper) {
        this.permissionsMapper = permissionsMapper;
    }

    public List<String> getPermissionsByUserId(Long id) {
        return this.permissionsMapper.getPermissionNamesByUserId(id);
    }
}
