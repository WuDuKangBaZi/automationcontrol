package com.felixstudio.automationcontrol.controller.task;

import com.alibaba.fastjson2.JSONObject;
import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.entity.task.TaskResults;
import com.felixstudio.automationcontrol.service.task.TaskResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/api/task/result")
public class TaskResultController {
    private final TaskResultService taskResultService;

    public TaskResultController(TaskResultService taskResultService) {
        this.taskResultService = taskResultService;
    }

    @PostMapping("/pub/submit")
    public ApiResponse<?> submitTaskResult(@RequestBody  TaskResults taskResults) {
        // 处理任务结果提交的逻辑
        try{
            log.debug(taskResults.toString());
            return ApiResponse.success(taskResultService.submit(taskResults).toString());
        }catch (Exception e){
            return ApiResponse.failure(500,"提交任务结果时发生错误: "+e.getMessage());
        }
    }
    @PostMapping("/pub/check")
    public ApiResponse<?> checkTaskResult(@RequestBody JSONObject taskKey) {
        try{
            if(taskResultService.checkTaskKey(taskKey.getString("key"))){
                return ApiResponse.success("任务可以执行");
            }else{
                return ApiResponse.failure(201,"任务已经执行过，不能重复执行");
            }
        }catch(Exception e){
            return ApiResponse.failure(500,"检查任务Key时发生错误: "+e.getMessage());
        }
    }
}
