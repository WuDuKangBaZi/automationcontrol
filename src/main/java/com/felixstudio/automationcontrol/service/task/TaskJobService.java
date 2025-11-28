package com.felixstudio.automationcontrol.service.task;


import com.baomidou.mybatisplus.extension.service.IService;
import com.felixstudio.automationcontrol.entity.shop.ShopInfo;
import com.felixstudio.automationcontrol.entity.task.TaskJob;

import java.util.List;

public interface TaskJobService extends IService<TaskJob> {
    void batchInsertTaskJobs(List<TaskJob> jobList);

    TaskJob getNextTask(String taskType, ShopInfo shopInfo);
}
