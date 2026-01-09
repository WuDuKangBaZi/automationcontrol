package com.felixstudio.automationcontrol.scheduler;

import com.felixstudio.automationcontrol.entity.robot.RobotStatusHistory;
import com.felixstudio.automationcontrol.service.robot.RobotHeartbeatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RobotOfflineCheckScheduler {

    private final RobotHeartbeatService robotHeartbeatService;

    public RobotOfflineCheckScheduler(RobotHeartbeatService robotHeartbeatService) {
        this.robotHeartbeatService = robotHeartbeatService;
    }

    @Scheduled(fixedDelay = 5000)
    public void checkOffline() {

        robotHeartbeatService.checkOfflineRobots();
    }
}

