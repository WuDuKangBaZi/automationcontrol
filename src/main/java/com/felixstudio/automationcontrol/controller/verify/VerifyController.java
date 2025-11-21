package com.felixstudio.automationcontrol.controller.verify;

import com.alibaba.fastjson2.JSONObject;
import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.entity.dingtalkEntity.VerifyMsg;
import com.felixstudio.automationcontrol.service.verify.VerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController()
@RequestMapping("/api/verify")
public class VerifyController {
    @Autowired
    private VerifyService verifyService;
    // 发起短信验证码请求
    @PostMapping("/sendSmSCode")
    public ApiResponse<?> sendSmSCode(@RequestBody VerifyMsg msg){
        // 流程名称、手机号、发送到指定的ID的群聊 该ID在网页端可以查询到
        return ApiResponse.success(verifyService.sendSmSCode(msg.getFlowName(), msg.getVerifyPhone(), msg.getChanelId()));
    }
    @GetMapping("/query/{shortCode}")
    public ApiResponse<?> queryByShortCode(@PathVariable String shortCode) {
        return ApiResponse.success(verifyService.queryByShortCode(shortCode));
    }

}
