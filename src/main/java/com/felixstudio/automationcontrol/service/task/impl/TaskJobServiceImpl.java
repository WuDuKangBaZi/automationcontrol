package com.felixstudio.automationcontrol.service.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.felixstudio.automationcontrol.entity.shop.ShopInfo;
import com.felixstudio.automationcontrol.entity.task.TaskJob;
import com.felixstudio.automationcontrol.mapper.task.TaskJobMapper;
import com.felixstudio.automationcontrol.service.task.TaskJobService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
@Service
public class TaskJobServiceImpl extends ServiceImpl<TaskJobMapper, TaskJob> implements TaskJobService {
    @Override
    @Transactional
    public void batchInsertTaskJobs(List<TaskJob> jobList){
        this.saveBatch(jobList);
    }

    @Override
    @Transactional
    public TaskJob getNextTask(String taskType, ShopInfo shopInfo) {
        // 根据taskType和shopName获取下一个任务的逻辑实现
        LambdaQueryWrapper<TaskJob> queryWrapper = new LambdaQueryWrapper<TaskJob>()
                .eq(TaskJob::getTaskType,taskType)
                .in(TaskJob::getTaskStatus,Arrays.asList(0,1));
        if(shopInfo!=null){
            queryWrapper.eq(TaskJob::getShopId,shopInfo.getId());
        }
        queryWrapper.orderByAsc(TaskJob::getCreatedAt);
        queryWrapper.last("for update skip locked limit 1");
        System.out.println("sqlSegment: " + queryWrapper.getSqlSegment());
        System.out.println("params: " + queryWrapper.getParamNameValuePairs());
        List<TaskJob> list = this.baseMapper.selectList(queryWrapper);
        if(list == null || list.isEmpty()){
            throw new NullPointerException("没有找到符合条件的任务");
        }
        TaskJob taskJob = list.get(0);
        taskJob.setTaskStatus(1);
        this.baseMapper.updateById(taskJob);
        return taskJob;
    }
}
