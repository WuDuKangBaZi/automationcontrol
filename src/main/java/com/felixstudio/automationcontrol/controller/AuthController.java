package com.felixstudio.automationcontrol.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.dto.LoginResponseDTO;
import com.felixstudio.automationcontrol.dto.auth.UserInfoDTO;
import com.felixstudio.automationcontrol.dto.auth.UserRegisterDTO;
import com.felixstudio.automationcontrol.dto.auth.UsersDTO;
import com.felixstudio.automationcontrol.entity.auth.Users;
import com.felixstudio.automationcontrol.security.jwt.JwtUtils;
import com.felixstudio.automationcontrol.service.auth.PermissionsService;
import com.felixstudio.automationcontrol.service.auth.UserRolesService;
import com.felixstudio.automationcontrol.service.auth.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

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
                        return ApiResponse.<LoginResponseDTO>failure(401, "该账户已被禁用!");
                    }
                    String token = jwtUtils.generateAccessToken(dbUser.getAccount());
                    String refreshToken = jwtUtils.generateRefreshToken(dbUser.getAccount());
                    LoginResponseDTO dto = LoginResponseDTO.builder()
                            .token(token)
                            .refreshToken(refreshToken)
                            .build();

                    return ApiResponse.success(dto);
                })
                .orElse(ApiResponse.failure(401, "账号或密码错误!"));
    }

    @PostMapping("/info")
    public ApiResponse<?> userInfo(@RequestBody JsonNode node) {
        String token = node.get("token").asText();
        if (!jwtUtils.validateToken(token)) {
            return ApiResponse.failure(401, "无效的令牌");
        }
        String account = jwtUtils.extractUsername(token);
        Users user = userService.getUserByAccount(account);
        if (user == null) {
            return ApiResponse.failure(404, "用户不存在");
        }
        List<String> roles = userRolesService.getRolesByUserId(user.getId());
        List<String> permissions = permissionService.getPermissionsByUserId(user.getId());
        String groupName = userService.getDepartmentNameByUserId(user.getAccount());
        UserInfoDTO userDTO = new UserInfoDTO().forEntity(user);
        userDTO.setRoles(roles);
        userDTO.setPermissions(permissions);
        userDTO.setGroupName(groupName);
        return ApiResponse.success(userDTO);
    }

    @PostMapping("/refreshToken")
    public ApiResponse<?> refreshToken(@RequestBody JsonNode node) {
        String refreshToken = node.get("refreshToken").asText();

        // 校验 Refresh Token
        if (!jwtUtils.validateToken(refreshToken)) {
            return ApiResponse.failure(401, "无效的刷新令牌");
        }
        String username = jwtUtils.extractUsername(refreshToken);
        String newAccessToken = jwtUtils.generateAccessToken(username);
        log.info("Generating new access token for user: {}", username);
        String newRefreshToken = jwtUtils.generateRefreshToken(username);

        // 返回双 Token 结构
        return ApiResponse.success(Map.of(
                "accessToken", newAccessToken,
                "refreshToken", newRefreshToken,
                "expiresAt", System.currentTimeMillis() + jwtUtils.getAccessTokenExpiration()
        ));
    }

    @PostMapping("/update")
    public ApiResponse<?> updateUser(@RequestBody Users userInput) {
        if (Objects.equals(userInput.getAvatarUrl(), "")) {
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
            if (userService.checkExists(user.getUsers().getAccount())) {
                return ApiResponse.failure(400, "账号已存在");
            }
            if (Objects.equals(user.getUsers().getAvatarUrl(), "")) {
                user.getUsers().setAvatarUrl(null);
            }
            boolean saveUser = userService.register(user.getUsers(), user.getDeptId());
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
            if (!dir.exists()) {
                boolean createRet = dir.mkdirs();
                if (!createRet) {
                    return ApiResponse.failure(500, "创建上传目录失败");
                }
            }
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
