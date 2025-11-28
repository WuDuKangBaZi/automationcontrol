package com.felixstudio.automationcontrol.mapper.task;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.felixstudio.automationcontrol.entity.task.TaskResults;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskResultMapper extends BaseMapper<TaskResults> {
}
