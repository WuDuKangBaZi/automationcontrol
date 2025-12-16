package com.felixstudio.automationcontrol.controller.task;

import com.alibaba.fastjson2.JSONObject;
import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.entity.shop.ShopInfo;
import com.felixstudio.automationcontrol.entity.task.TaskJob;
import com.felixstudio.automationcontrol.service.shop.ShopInfoService;
import com.felixstudio.automationcontrol.service.task.TaskJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TaskJobService taskJobService;
    private final ShopInfoService shopInfoService;

    public TaskController(TaskJobService taskJobService, ShopInfoService shopInfoService) {
        this.taskJobService = taskJobService;
        this.shopInfoService = shopInfoService;
    }

    @PostMapping("/pub/getTask")
    public ApiResponse<?> getTask(@RequestBody JSONObject params) {
        // 传入参数中可能包含shopName 需要注意
        String taskType = params.getString("taskType");
        String shopName;
        String businessType;
        ShopInfo shopInfo = null;
        if (params.containsKey("shopName")) {
            shopName = params.getString("shopName");
            businessType = params.getString("businessType");
            shopInfo = shopInfoService.getShopByNameAndBusinessType(shopName, businessType);
        }
        try {
            TaskJob job = taskJobService.getNextTask(taskType, shopInfo);
            JSONObject resultJson = new JSONObject();
            resultJson.put("taskId", job.getId().toString());
            resultJson.put("taskParams", job.getTaskParams());
            return ApiResponse.success(resultJson);
        } catch (NullPointerException e) {
            return ApiResponse.failure(404, "没有找到符合条件的任务");
        }

    }

    @PostMapping("/pub/submit")
    public ApiResponse<?> submit(@RequestBody JSONObject params) {
        // 提交任务处理结果
        Long taskId = Long.parseLong(params.getString("taskId"));
        TaskJob job = taskJobService.getById(taskId);
        if (job != null) {
            job.setTaskStatus(params.getIntValue("status")); //
            if (params.containsKey("message")) {
                job.setErrorMessage(params.getString("message"));
            }
            taskJobService.updateById(job);
            return ApiResponse.success("任务结果提交成功");
        }
        return ApiResponse.failure(404, "未找到对应的任务");
    }
}
