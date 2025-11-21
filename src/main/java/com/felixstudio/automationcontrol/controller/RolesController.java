package com.felixstudio.automationcontrol.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.entity.auth.Roles;
import com.felixstudio.automationcontrol.service.auth.RolesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/roles")
public class RolesController {

    private final RolesService rolesService;


    public RolesController(RolesService rolesService) {
        this.rolesService = rolesService;
    }

    @GetMapping("/all")
    public ApiResponse<List<Roles>> getAllRoles() {
        return ApiResponse.success(this.rolesService.getAllRoles());
    }


}
