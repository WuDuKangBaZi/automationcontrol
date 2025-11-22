package com.felixstudio.automationcontrol.controller.dingTalk;

import com.alibaba.fastjson2.JSONObject;
import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.entity.dingtalkEntity.VerifyMsg;
import com.felixstudio.automationcontrol.service.dingTalk.DingTalkService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dingtalk")
public class DingTalkController {

    private final DingTalkService dingTalkService;

    public DingTalkController(DingTalkService dingTalkService) {
        this.dingTalkService = dingTalkService;
    }

    @PostMapping("/sendMsg")
    public ApiResponse<?> sendMsg(@RequestBody JSONObject message){
        String msgContent = message.getString("message");
        String shortName = message.getString("groupShort");
        return ApiResponse.success(dingTalkService.sendMsg(msgContent,shortName));
    }

        }
    }
}
