package com.felixstudio.automationcontrol.dto.robot;

import lombok.Data;

@Data
public class RobotHeartbeatDTO {
    private String robotId;

    /**
     * IDLE / RUNNING / ERROR
     */
    private String status;

    private String currentTask;

    private String currentStep;

    private String message;

    private String robotName;

    /**
     * agent 上报时间
     */
    private Long timestamp;
    private String memory;
}
