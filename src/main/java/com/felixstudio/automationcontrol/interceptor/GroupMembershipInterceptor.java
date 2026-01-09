package com.felixstudio.automationcontrol.interceptor;

import com.felixstudio.automationcontrol.service.auth.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Set;
@Slf4j
@Component
public class GroupMembershipInterceptor implements ChannelInterceptor {


    private final StringRedisTemplate redisTemplate;
    private final UsersService usersService;

    public GroupMembershipInterceptor(StringRedisTemplate redisTemplate, UsersService usersService) {
        this.redisTemplate = redisTemplate;
        this.usersService = usersService;
    }

    private String getUserGroup(String account) {
        return usersService.getDepartmentNameByUserId(account);
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String userId = accessor.getUser().getName();
            log.info(userId);
            String sessionId = accessor.getSessionId();
            String groupName = getUserGroup(userId);
            log.info("groupName ->" + groupName);
            // 1. 记录用户所属组（String）
            redisTemplate.opsForValue().set(
                    "ws:user:" + userId + ":group",
                    groupName,
                    Duration.ofHours(24)
            );

            // 2. 将用户加入该组的在线集合（Set）
            redisTemplate.opsForSet().add(
                    "ws:group:" + groupName + ":onlineUsers",
                    userId
            );
            redisTemplate.expire("ws:group:" + groupName + ":onlineUsers", Duration.ofHours(24));

            // 3. 记录 sessionId → userId（用于断连）
            redisTemplate.opsForValue().set(
                    "ws:session:" + sessionId,
                    userId,
                    Duration.ofHours(24)
            );
        }
        return message;
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel,
                                    boolean sent, Exception ex) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.DISCONNECT.equals(accessor.getCommand()) || ex != null) {
            String sessionId = accessor.getSessionId();
            String userId = redisTemplate.opsForValue().get("ws:session:" + sessionId);

            if (userId != null) {
                // 1. 获取用户所属组
                String groupName = redisTemplate.opsForValue().get("ws:user:" + userId + ":group");

                // 2. 从组中移除用户
                if (groupName != null) {
                    redisTemplate.opsForSet().remove("ws:group:" + groupName + ":onlineUsers", userId);
                }

                // 3. 清理所有相关 key
                redisTemplate.delete(
                        "ws:session:" + sessionId
                );
                redisTemplate.delete(
                        "ws:user:" + userId + ":group"
                );
            }
        }
    }
}