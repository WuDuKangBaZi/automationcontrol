package com.felixstudio.automationcontrol.mapper.robot;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.felixstudio.automationcontrol.entity.robot.RobotStatusHistory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RobotStatusHistoryMapper
        extends BaseMapper<RobotStatusHistory> {
}

