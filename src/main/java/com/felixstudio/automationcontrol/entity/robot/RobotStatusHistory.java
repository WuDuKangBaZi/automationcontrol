package com.felixstudio.automationcontrol.entity.robot;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("robot_status_history")
public class RobotStatusHistory {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String robotId;

    private String fromStatus;

    private String toStatus;

    private Long durationMs;

    /**
     * 状态变更时间（毫秒时间戳）
     */
    private Long changedAt;
}

