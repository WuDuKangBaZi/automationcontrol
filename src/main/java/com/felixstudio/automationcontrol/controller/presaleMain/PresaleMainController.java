package com.felixstudio.automationcontrol.controller.presaleMain;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.entity.presale.PresaleMain;
import com.felixstudio.automationcontrol.service.presale.PresaleMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/presaleMain")
public class PresaleMainController {

    @Autowired
    private PresaleMainService service;

    @PostMapping("/batchInsert")
    public ApiResponse<?> batchInsert(@RequestBody List<PresaleMain> presaleMains) {
        return ApiResponse.success(service.batchInsertPresaleMainAndCreateTaskJob(presaleMains));
    }

    @GetMapping("/pub/getErpCodes")
    public ApiResponse<?> getErpCodes() {
        return ApiResponse.success(service.getErpCodes());
    }
    @PostMapping("/pub/setErpCodesResult")
    public ApiResponse<?> setErpCodesResult(@RequestBody JSONObject json) {
        Long taskId = json.getLong("taskId");
        int status = json.getIntValue("status");
        JSONArray taskResult = json.getJSONArray("result");
        return ApiResponse.success(service.setErpResult(taskId,status,taskResult));
    }
}
