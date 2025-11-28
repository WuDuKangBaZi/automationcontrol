package com.felixstudio.automationcontrol.security.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class PubPathRequestMatcher implements RequestMatcher {
    @Override
    public boolean matches(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.contains("/pub/");
    }
}
