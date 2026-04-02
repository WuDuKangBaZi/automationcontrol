package com.felixstudio.automationcontrol.service.task.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.felixstudio.automationcontrol.dto.presale.PresaleTaskProgress;
import com.felixstudio.automationcontrol.entity.shop.ShopInfo;
import com.felixstudio.automationcontrol.entity.task.TaskJob;
import com.felixstudio.automationcontrol.mapper.task.TaskJobMapper;
import com.felixstudio.automationcontrol.service.notify.GroupNotificationService;
import com.felixstudio.automationcontrol.service.task.TaskJobService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class TaskJobServiceImpl extends ServiceImpl<TaskJobMapper, TaskJob> implements TaskJobService {

    private final StringRedisTemplate stringRedisTemplate;
    private final GroupNotificationService groupNotificationService;

    public TaskJobServiceImpl(StringRedisTemplate stringRedisTemplate, GroupNotificationService groupNotificationService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.groupNotificationService = groupNotificationService;
    }

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
        List<TaskJob> list = this.baseMapper.selectList(queryWrapper);
        if(list == null || list.isEmpty()){
            if (shopInfo != null) {
                checkAllTask(shopInfo.getShopName(),taskType);
            }
            throw new NullPointerException("没有找到符合条件的任务");
        }
        TaskJob taskJob = list.get(0);
        // 校验 如果是需要店铺的数据没有传入店铺则返回error
        if(Objects.equals(taskJob.getTaskStatus(),1) && shopInfo == null){
            throw new IllegalArgumentException("任务需要店铺信息，但未提供店铺信息");
        }
        taskJob.setTaskStatus(1);
        this.baseMapper.updateById(taskJob);
        return taskJob;
    }
    private void checkAllTask(String shopName,String taskType){
        if(shopName != null){
            Long removeCount = stringRedisTemplate.opsForSet().remove(taskType,shopName);
            if(removeCount != null && removeCount > 0){
                Long remaining = stringRedisTemplate.opsForSet().size(taskType);
                if(remaining != null && remaining == 0){
                    // todo ws发送信息到前端 此处暂时仅用于运营部
                    JSONObject payload = new JSONObject();
                    payload.put("messageType", "info");
                    payload.put("message", "所有店铺的"+taskType+"任务已完成");
                    payload.put("title", taskType+"任务完成通知");
                    groupNotificationService.sendToGroup("运营部", payload);
                }
            }
        }
    }
    @Override
    public List<PresaleTaskProgress> queryPresaleProgress(){
        return this.baseMapper.queryPresaleProgress();
    }

}
