package com.felixstudio.automationcontrol.service.task.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.felixstudio.automationcontrol.entity.task.TaskResults;
import com.felixstudio.automationcontrol.mapper.task.TaskResultMapper;
import com.felixstudio.automationcontrol.service.task.TaskResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TaskResultServiceImpl extends ServiceImpl<TaskResultMapper,TaskResults> implements TaskResultService {
    private final TaskResultMapper taskResultMapper;

    public TaskResultServiceImpl(TaskResultMapper taskResultMapper) {
        this.taskResultMapper = taskResultMapper;
    }

    @Override
    public boolean checkTaskKey(String taskKey) {
        // 检查任务key是否已经存在了 任务key自定义一个 例如预售的就是 presale_{presaleId}_{shopId}_{goodsId} 来检查这个店铺下的任务是否已经执行过了 根据任务状态类获取
        try {
            TaskResults results = taskResultMapper.selectOne(new LambdaQueryWrapper<TaskResults>()
                    .eq(TaskResults::getTaskKey,taskKey),true);
            if(results!=null && results.getStatus() == -1){
                return false;
            }else{
                return true;
            }
        }catch (Exception e){
            throw new IndexOutOfBoundsException("该关键Key超出了预期数量!");
        }


    }

    @Override
    public int submit(TaskResults taskResults) {
        return taskResultMapper.insert(taskResults);
    }
}
