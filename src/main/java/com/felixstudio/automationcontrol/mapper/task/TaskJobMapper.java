package com.felixstudio.automationcontrol.mapper.task;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.felixstudio.automationcontrol.dto.presale.PresaleTaskProgress;
import com.felixstudio.automationcontrol.dto.task.TaskProgress;
import com.felixstudio.automationcontrol.entity.task.TaskJob;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TaskJobMapper extends BaseMapper<TaskJob> {
    List<TaskProgress> queryProgress(String queryDay);
    List<JSONObject> queryProgressInfo(String queryDay);

    List<PresaleTaskProgress> queryPresaleProgress();
}
