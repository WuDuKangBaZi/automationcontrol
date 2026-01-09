package com.felixstudio.automationcontrol.service.robot;

import com.felixstudio.automationcontrol.dto.robot.RobotHeartbeatDTO;

public interface RobotHeartbeatService {
    void handleHeartbeat(RobotHeartbeatDTO dto);
    void checkOfflineRobots();

    void pushAllRobotStates();
}
