package com.felixstudio.automationcontrol.config.webSocket;

import com.felixstudio.automationcontrol.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtStompInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtUtils jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // ① 非 STOMP 消息直接放行
        if (accessor == null || accessor.getCommand() == null) {
            return message;
        }

        // ② 只在 CONNECT 阶段处理认证
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                //直接拒绝未携带 token 的连接
                throw new AccessDeniedException("Missing Authorization header");
            }
            String token = authHeader.substring(7);
            try {
                // ③ 校验 token
                if (!jwtUtil.validateToken(token)) {
                    throw new AccessDeniedException("Invalid JWT token");
                }

                // ④ 构建 Authentication（推荐用 UserDetails）
                String username = jwtUtil.extractUsername(token);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_USER"))
                        );
                accessor.setUser(authentication);

            } catch (Exception e) {
                // ❗ 不要 RuntimeException，会被吞并导致 protocol error
                throw new AccessDeniedException("JWT authentication failed", e);
            }
        }

        return message;
    }
}

