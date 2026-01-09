package com.felixstudio.automationcontrol.controller;

import com.felixstudio.automationcontrol.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plugin")
public class LoadingController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping(value = "/status",method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResponse<String> status() {
        return ApiResponse.success("Service is up and running");
    }

    @PostMapping("/pub/testRedis")
    public ApiResponse<String> testRedis() {
        String testKey = "testKey";
        String testValue = "Hello, Redis!";
        stringRedisTemplate.opsForValue().set(testKey, testValue);
        String retrievedValue = stringRedisTemplate.opsForValue().get(testKey);
        if (testValue.equals(retrievedValue)) {
            return ApiResponse.success("Redis is working properly");
        } else {
            return ApiResponse.failure(500, "Redis test failed");
        }
    }
}
