package com.felixstudio.automationcontrol.config.webSocket;

import com.felixstudio.automationcontrol.interceptor.GroupMembershipInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.messaging.access.intercept.MessageAuthorizationContext;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtStompInterceptor jwtStompInterceptor;
    private final GroupMembershipInterceptor groupMembershipInterceptor;

    public WebSocketConfig(JwtStompInterceptor jwtStompInterceptor,
                           GroupMembershipInterceptor groupMembershipInterceptor) {
        this.jwtStompInterceptor = jwtStompInterceptor;
        this.groupMembershipInterceptor = groupMembershipInterceptor;
    }

    /**
     * WebSocket 连接端点
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
        registry.addEndpoint("/ws/robot")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    /**
     * STOMP Broker 配置
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // 客户端发送消息的前缀（@MessageMapping）
        registry.setApplicationDestinationPrefixes("/app");

        // 服务端推送给客户端的前缀
        registry.enableSimpleBroker("/topic", "/queue");

        // 点对点 user 前缀
        registry.setUserDestinationPrefix("/user");
    }

    /**
     * ⭐ 核心：STOMP 入站拦截器
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {

        registration.interceptors(
                jwtStompInterceptor,          // ① 先认证（CONNECT 阶段 setUser）
                groupMembershipInterceptor   // ② 再做业务鉴权（使用 user）
        );
    }

    /**
     * STOMP 消息级别授权（Spring Security 6.4+）
     */
    @Bean
    AuthorizationManager<Message<?>> messageAuthorizationManager() {
        return MessageMatcherDelegatingAuthorizationManager.builder()

                // CONNECT / HEARTBEAT / DISCONNECT 永远放行
                .simpTypeMatchers(
                        SimpMessageType.CONNECT,
                        SimpMessageType.HEARTBEAT,
                        SimpMessageType.DISCONNECT
                ).permitAll()

                // 客户端发送到 @MessageMapping
                .simpDestMatchers("/app/**").authenticated()

                // 订阅 topic / user
                .simpDestMatchers("/topic/**", "/user/**").authenticated()

                .anyMessage().denyAll()
                .build();
    }
}
