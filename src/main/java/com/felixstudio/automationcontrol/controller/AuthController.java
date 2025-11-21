package com.felixstudio.automationcontrol.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.dto.LoginResponseDTO;
import com.felixstudio.automationcontrol.dto.auth.UserRegisterDTO;
import com.felixstudio.automationcontrol.dto.auth.UsersDTO;
import com.felixstudio.automationcontrol.entity.auth.Users;
import com.felixstudio.automationcontrol.security.jwt.JwtUtils;
import com.felixstudio.automationcontrol.service.auth.PermissionsService;
import com.felixstudio.automationcontrol.service.auth.UserRolesService;
import com.felixstudio.automationcontrol.service.auth.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtUtils jwtUtils;
    private final UsersService userService;
    private final UserRolesService userRolesService;
    private final PermissionsService permissionService;
    @Value("${file.upload-dir}")
    private String uploadDir;

    public AuthController(JwtUtils jwtUtils, UsersService userService, UserRolesService userRolesService, PermissionsService permissionService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
        this.userRolesService = userRolesService;
        this.permissionService = permissionService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponseDTO> login(@RequestBody Users userInput) {
        return userService.validateLogin(userInput.getAccount(), userInput.getPassword())
                .map(dbUser -> {
                    if (dbUser.getStatus() != null && dbUser.getStatus() == 0) {
                        return ApiResponse.<LoginResponseDTO>failure(403, "该账户已被禁用!");
                    }

                    List<String> roles = userRolesService.getRolesByUserId(dbUser.getId());
                    List<String> permissions = permissionService.getPermissionsByUserId(dbUser.getId());

                    String token = jwtUtils.generateToken(dbUser.getAccount());
                    long expiresAt = System.currentTimeMillis() + jwtUtils.getExpiration();

                    LoginResponseDTO dto = LoginResponseDTO.builder()
                            .userId(dbUser.getId().toString())
                            .account(dbUser.getAccount())
                            .username(dbUser.getUsername())
                            .roles(roles)
                            .permissions(permissions)
                            .token(token)
                            .expiresAt(expiresAt)
                            .avatar(dbUser.getAvatarUrl())
                            .phone(dbUser.getPhone())
                            .build();

                    return ApiResponse.success(dto);
                })
                .orElse(ApiResponse.<LoginResponseDTO>failure(401, "账号或密码错误!"));
    }

    @PostMapping("/update")
    public ApiResponse<?> updateUser(@RequestBody Users userInput) {
        if(Objects.equals(userInput.getAvatarUrl(), "")){
            userInput.setAvatarUrl(null);
        }
        int updateResult = userService.updateUser(userInput);
        if (updateResult > 0) {
            return ApiResponse.success(null, "更新成功");
        } else {
            return ApiResponse.failure(500, "更新失败");
        }
    }

    @PostMapping("/deleteUser")
    public ApiResponse<?> deleteUser(@RequestBody JsonNode node) {
        Long userId = Long.valueOf(node.get("id").asText());
        int deleteResult = userService.deleteByUserId(userId);
        if (deleteResult > 0) {
            return ApiResponse.success(null, "删除成功");
        } else {
            return ApiResponse.failure(500, "删除失败");
        }
    }
    @PostMapping("/saveUser")
    public ApiResponse<?> update(@RequestBody UserRegisterDTO user) {
        if (user.getUsers().getId() == null) {
            if(userService.checkExists(user.getUsers().getAccount())){
                return ApiResponse.failure(400, "账号已存在");
            }
            if(Objects.equals(user.getUsers().getAvatarUrl(), "")){
                user.getUsers().setAvatarUrl(null);
            }
            boolean saveUser = userService.register(user.getUsers(),user.getDeptId());
            if (saveUser) {
                // 关联用户和分组
                return ApiResponse.success(null, "保存成功");
            } else {
                return ApiResponse.failure(500, "保存失败");
            }

        } else {
            int updateResult = userService.updateUser(user.getUsers());
            if (updateResult > 0) {
                return ApiResponse.success(null, "更新成功");
            } else {
                return ApiResponse.failure(500, "更新失败");
            }
        }

    }

    @PostMapping("/avatar/{id}")
    public ApiResponse<?> uploadAvatar(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ApiResponse.failure(400, "上传文件不能为空");
            }
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();
            // 生成唯一文件名
            String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String filename = UUID.randomUUID() + "." + ext;
            File dest = new File(dir, filename);
            file.transferTo(dest);

            // 拼接访问路径
            String fileUrl = "/uploads/" + filename;

            // 更新数据库
            Users user = new Users();
            user.setId(id);
            user.setAvatarUrl(fileUrl);
            int updateResult = userService.updateUser(user);
            if (updateResult <= 0) {
                return ApiResponse.failure(500, "更新用户头像失败");
            }

            return ApiResponse.success(fileUrl);
        } catch (Exception e) {
            return ApiResponse.failure(500, "上传失败: " + e.getMessage());
        }
    }

    @PostMapping("/listByDept")
    public ApiResponse<List<UsersDTO>> listByDept(@RequestBody JsonNode node) {
        Long deptId = node.get("deptId").asLong();
        List<UsersDTO> users = userService.listUsersByDept(deptId);
        return ApiResponse.success(users);
    }

    @PostMapping("/setRoles")
    public ApiResponse<?> setRoles(@RequestBody JsonNode node) {
        Long userId = node.get("userId").asLong();
        ArrayNode rolesArray = (ArrayNode) node.path("roles");
        List<String> roleIds = new ArrayList<>();
        for (JsonNode item : rolesArray) {
            roleIds.add(item.asText());
        }
        log.info("roleIds: {}", roleIds);
        log.info("userId = {}", userId);
        boolean result = userRolesService.setUserRoles(userId, roleIds);
        if (!result) {
            return ApiResponse.failure(500, "设置角色失败");
        }
        return ApiResponse.success("ok");
    }
}
