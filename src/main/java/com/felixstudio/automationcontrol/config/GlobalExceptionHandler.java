package com.felixstudio.automationcontrol.config;

import com.felixstudio.automationcontrol.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ApiResponse.failure(500, "服务器内部错误: " + e.getMessage());
    }
}
