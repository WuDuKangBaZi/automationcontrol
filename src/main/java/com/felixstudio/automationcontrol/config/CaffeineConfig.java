package com.felixstudio.automationcontrol.config;

import com.felixstudio.automationcontrol.entity.verify.SmSCodeEntity;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineConfig {
    @Bean
    public Cache<String, SmSCodeEntity> smsCodeCache(){
        return Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10000)
                .build();
    }
}
