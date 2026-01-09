package com.felixstudio.automationcontrol.service.robot.impl;

import com.alibaba.fastjson2.JSON;
import com.felixstudio.automationcontrol.dto.robot.RobotHeartbeatDTO;
import com.felixstudio.automationcontrol.entity.robot.RobotStatusHistory;
import com.felixstudio.automationcontrol.mapper.robot.RobotStatusHistoryMapper;
import com.felixstudio.automationcontrol.service.robot.RobotHeartbeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RobotHeartbeatServiceImpl implements RobotHeartbeatService {

    private final StringRedisTemplate stringRedisTemplate;
    private final RobotStatusHistoryMapper historyMapper;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String HEARTBEAT_KEY_PREFIX = "robot:heartbeat:";
    private static final String VIEW_KEY_PREFIX = "robot:view:";
    private static final Duration HEARTBEAT_TTL = Duration.ofSeconds(15);

    @Override
    public void handleHeartbeat(RobotHeartbeatDTO dto) {
        long now = dto.getTimestamp() != null ? dto.getTimestamp() : System.currentTimeMillis();
        String robotId = dto.getRobotId();
        String heartbeatKey = HEARTBEAT_KEY_PREFIX + robotId;

        // 1. 更新心跳 TTL
        stringRedisTemplate.opsForValue().set(
                heartbeatKey,
                "1",
                HEARTBEAT_TTL
        );

        String viewKey = VIEW_KEY_PREFIX + robotId;

        // 2. 获取旧状态
        Map<Object, Object> oldView = stringRedisTemplate.opsForHash().entries(viewKey);
        boolean wasOffline = oldView.isEmpty() || !"true".equals(oldView.get("online"));
        String oldStatus = (String) oldView.get("status");
        String oldStep = defaultStr((String) oldView.get("currentStep"));

        String newStatus = dto.getStatus();
        String newStep = defaultStr(dto.getCurrentStep());

        boolean statusChanged = !Objects.equals(oldStatus, newStatus);
        boolean stepChanged = !Objects.equals(oldStep, newStep);
        boolean needStateChange = wasOffline || statusChanged || stepChanged;

        // 3. 更新 Redis 状态
        Map<String, String> newView = new HashMap<>();
        newView.put("robotId", robotId);
        newView.put("robotName", dto.getRobotName() != null ? dto.getRobotName() : "");
        newView.put("status", newStatus);
        newView.put("currentTask", defaultStr(dto.getCurrentTask()));
        newView.put("currentStep", defaultStr(newStep));
        newView.put("lastHeartbeat", String.valueOf(now));
        newView.put("message", dto.getMessage() != null ? dto.getMessage() : "");
        newView.put("memory", dto.getMemory() != null ? dto.getMemory() : "");
        newView.put("online", "true");
        // 如果状态未变，保持原来的 statusSince，否则更新
        newView.put("statusSince", (statusChanged || stepChanged)  ? String.valueOf(now) : (String) oldView.getOrDefault("statusSince", String.valueOf(now)));

        stringRedisTemplate.opsForHash().putAll(viewKey, newView);

        // 4. 如果状态或步骤有变化，推送到前端
        if (needStateChange) {
            log.info("推送机器人状态变更: {}", JSON.toJSONString(newView));
//            pushAllRobotStates();
            pushRobotState(viewKey);
        }
    }


    @Override
    public void checkOfflineRobots() {

        Set<String> viewKeys = stringRedisTemplate.keys("robot:view:*");
        if (viewKeys.isEmpty()) {
            return;
        }

        long now = System.currentTimeMillis();

        for (String viewKey : viewKeys) {

            String robotId = viewKey.substring("robot:view:".length());
            String heartbeatKey = "robot:heartbeat:" + robotId;

            // Redis 心跳 key 不存在 = 离线
            if (!stringRedisTemplate.hasKey(heartbeatKey)) {

                Object onlineVal =
                        stringRedisTemplate.opsForHash().get(viewKey, "online");

                if (!"true".equals(onlineVal)) {
                    // 已经是离线状态，不重复处理
                    continue;
                }

                Map<Object, Object> view =
                        stringRedisTemplate.opsForHash().entries(viewKey);

                String fromStatus = (String) view.get("status");
                Long statusSince = view.get("statusSince") != null
                        ? Long.valueOf(view.get("statusSince").toString())
                        : null;

                // ---------- 1️⃣ 写状态历史（MyBatis-Plus） ----------
                if (fromStatus != null) {

                    RobotStatusHistory history = new RobotStatusHistory();
                    history.setRobotId(robotId);
                    history.setFromStatus(fromStatus);
                    history.setToStatus("OFFLINE");
                    history.setChangedAt(now);

                    if (statusSince != null) {
                        history.setDurationMs(now - statusSince);
                    }

                    historyMapper.insert(history);
                }

                // ---------- 2️⃣ 更新 Redis 视图 ----------
                stringRedisTemplate.opsForHash().putAll(viewKey, Map.of(
                        "status", "OFFLINE",
                        "online", "false",
                        "statusSince", String.valueOf(now),
                        "lastOfflineTime", String.valueOf(now)
                ));
                // 推送该机器人
                pushRobotState(viewKey);
            }
        }
    }

    @Override
    public void pushAllRobotStates() {
        Set<String> keys = stringRedisTemplate.keys(VIEW_KEY_PREFIX + "*");
        if (keys == null || keys.isEmpty()) return;

        for (String key : keys) {
            Map<Object, Object> view = stringRedisTemplate.opsForHash().entries(key);
            if (!view.isEmpty()) {
                messagingTemplate.convertAndSend("/topic/robot/state", view);
            }
        }
        log.info("已推送所有存量机器人状态");
    }


    private void handleStatusChange(
            String robotId,
            String fromStatus,
            String toStatus,
            Map<Object, Object> view,
            long now
    ) {

        Long statusSince = view.get("statusSince") != null
                ? Long.valueOf(view.get("statusSince").toString())
                : null;

        if (fromStatus != null) {

            RobotStatusHistory history = new RobotStatusHistory();
            history.setRobotId(robotId);
            history.setFromStatus(fromStatus);
            history.setToStatus(toStatus);
            history.setChangedAt(now);

            if (statusSince != null) {
                history.setDurationMs(now - statusSince);
            }

            historyMapper.insert(history);
        }

        log.info("Robot [{}] status changed: {} -> {}",
                robotId, fromStatus, toStatus);
    }

    private String defaultStr(String value) {
        return value == null ? "" : value;
    }
    private void pushRobotState(String viewKey) {
        log.info("推送消息到前端，key={}", viewKey);
        Map<Object, Object> view =
                stringRedisTemplate.opsForHash().entries(viewKey);

        if (view.isEmpty()){
            log.info("视图数据为空，跳过推送");
            return;
        }

        messagingTemplate.convertAndSend(
                "/topic/robot/state",
                view
        );
    }
}
