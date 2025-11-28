package com.felixstudio.automationcontrol.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felixstudio.automationcontrol.common.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getServletPath();
        log.info("path={}", path);
        List<String> whiteList = List.of(
                "/api/auth/login",
                "/uploads/",
                "/images/",
                "/api/auth/refreshToken"
        );
        boolean isWhiteListed = whiteList.stream().anyMatch(path::startsWith);
        if(isWhiteListed || path.contains("/pub/")) {
            filterChain.doFilter(request, response);
            return;
        }
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                    new ObjectMapper().writeValueAsString(ApiResponse.failure(401, "该接口没有访问权限!"))
            );
            return;
        }

        String token = header.substring(7);
        try {
            if (!jwtUtils.validateToken(token)) {
                throw new JwtException("Token校验失败");
            }

            String username = jwtUtils.extractUsername(token);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                    new ObjectMapper().writeValueAsString(ApiResponse.failure(401, "Token expired"))
            );
            return;
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                    new ObjectMapper().writeValueAsString(ApiResponse.failure(401, e.getMessage()))
            );
            return;
        }
        filterChain.doFilter(request, response);
    }
}
