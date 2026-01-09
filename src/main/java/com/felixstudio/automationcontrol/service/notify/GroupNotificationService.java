package com.felixstudio.automationcontrol.service.notify;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class GroupNotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final StringRedisTemplate redisTemplate;

    public GroupNotificationService(
            SimpMessagingTemplate messagingTemplate,
            StringRedisTemplate redisTemplate
    ) {
        this.messagingTemplate = messagingTemplate;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 向指定 groupId 的在线成员推送
     */
    public void sendToGroup(String groupName, Object payload) {
        messagingTemplate.convertAndSend(
                "/topic/group/" + groupName,
                payload
        );
    }

    /**
     * 获取 group 在线人数
     */
    public long getOnlineCount(Long groupId) {
        Long count = redisTemplate.opsForSet()
                .size("ws:group:" + groupId + ":onlineUsers");
        return count == null ? 0 : count;
    }
}

