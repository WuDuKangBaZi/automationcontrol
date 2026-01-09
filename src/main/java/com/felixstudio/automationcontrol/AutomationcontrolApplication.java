package com.felixstudio.automationcontrol;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableScheduling
@MapperScan("com.felixstudio.automationcontrol.mapper")
public class AutomationcontrolApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(AutomationcontrolApplication.class, args);

    }
}
