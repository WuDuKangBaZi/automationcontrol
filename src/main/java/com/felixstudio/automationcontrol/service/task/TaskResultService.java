package com.felixstudio.automationcontrol.service.task;

import com.baomidou.mybatisplus.extension.service.IService;
import com.felixstudio.automationcontrol.entity.task.TaskResults;

public interface TaskResultService extends IService<TaskResults> {
    boolean checkTaskKey(String taskKey);

    int submit(TaskResults taskResults);
}
