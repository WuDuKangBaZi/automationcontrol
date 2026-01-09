package com.felixstudio.automationcontrol.controller.robot;

import com.alibaba.fastjson2.JSON;
import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.dto.robot.RobotHeartbeatDTO;
import com.felixstudio.automationcontrol.service.robot.RobotHeartbeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/robot")
@RequiredArgsConstructor
@Slf4j
public class RobotController {
    private final StringRedisTemplate stringRedisTemplate;
    private final RobotHeartbeatService robotHeartbeatService;

    @PostMapping("/heartbeat")
    public ApiResponse<?> heartbeat(@RequestBody RobotHeartbeatDTO dto) {
        log.info(dto.toString());
        if (dto.getRobotId() == null || dto.getRobotId().isBlank()) {
            return ApiResponse.failure(400, "robotId is required");
        }
        robotHeartbeatService.handleHeartbeat(dto);
        return ApiResponse.success("");
    }
    @GetMapping("/state/init")
    public ApiResponse<?> pushInitialStates() {
        robotHeartbeatService.pushAllRobotStates();
        return ApiResponse.success("已推送当前所有机器人状态");
    }
    @GetMapping("/list")
    public ApiResponse<List<RobotHeartbeatDTO>> list(){
        Set<String> keys = stringRedisTemplate.keys("robot:heartbeat:*");
        if(keys.isEmpty()){
            return ApiResponse.success(List.of());
        }
        List<String> values = stringRedisTemplate.opsForValue().multiGet(keys);
        List<RobotHeartbeatDTO> dtos = values.stream()
                .filter(value -> value != null && !value.isEmpty())
                .map(value -> JSON.parseObject(value, RobotHeartbeatDTO.class))
                .toList();
        return ApiResponse.success(dtos);
    }
}
