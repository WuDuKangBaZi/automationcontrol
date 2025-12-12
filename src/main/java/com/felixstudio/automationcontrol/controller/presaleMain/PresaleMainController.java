package com.felixstudio.automationcontrol.controller.presaleMain;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.dto.PagerDTO;
import com.felixstudio.automationcontrol.dto.presale.PresaleMainQueryDTO;
import com.felixstudio.automationcontrol.entity.presale.PresaleMain;
import com.felixstudio.automationcontrol.service.presale.PresaleMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/presaleMain")
public class PresaleMainController {


    private final PresaleMainService service;

    public PresaleMainController(PresaleMainService service) {
        this.service = service;
    }

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

    @PostMapping("/search")
    public ApiResponse<?> search(@RequestBody PresaleMainQueryDTO queryDTO) {
        return ApiResponse.success(service.search(queryDTO));
    }
    @PostMapping("/getTaskInfo/{presaleId}")
    public ApiResponse<?> getTaskInfo(@PathVariable String presaleId, @RequestBody PagerDTO pagerDTO) {
        return ApiResponse.success(service.getTaskInfoByPresaleId(presaleId,pagerDTO));
    }
    @PostMapping("/check")
    public ApiResponse<?> check(@RequestBody List<PresaleMain> presaleMains) {
        return ApiResponse.success(service.checkPresaleMain(presaleMains));
    }

}
