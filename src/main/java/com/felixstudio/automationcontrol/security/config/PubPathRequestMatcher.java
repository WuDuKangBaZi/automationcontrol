package com.felixstudio.automationcontrol.security.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;
@Slf4j
public class PubPathRequestMatcher implements RequestMatcher {
    private static final List<String> PUBLIC_PATHS = List.of("/pub/","/verify/");
    @Override
    public boolean matches(HttpServletRequest request) {
        String path = request.getServletPath();
        if(path == null){
            return false;
        }
        return PUBLIC_PATHS.stream().anyMatch(path::contains);
    }
}
