package com.felixstudio.automationcontrol.controller.webSocket;

import com.alibaba.fastjson2.JSONObject;
import com.felixstudio.automationcontrol.dto.webScoket.WebSocketMsgDTO;
import com.felixstudio.automationcontrol.service.notify.GroupNotificationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ws")
public class WebScoketController {

    private final GroupNotificationService groupNotificationService;
    public WebScoketController(GroupNotificationService groupNotificationService) {
        this.groupNotificationService = groupNotificationService;
    }

    @PostMapping("/pub/pushGroupNotification")
    public void pushGroupNotification(@RequestBody WebSocketMsgDTO webSocketMsgDTO){
        JSONObject payload = new JSONObject();
        payload.put("messageType", webSocketMsgDTO.getMessageType());
        payload.put("message", webSocketMsgDTO.getMessage());
        if (webSocketMsgDTO.getMessageTitle() != null) {
            payload.put("title", webSocketMsgDTO.getMessageTitle());
        }
        groupNotificationService.sendToGroup(
                webSocketMsgDTO.getGroupName(),
                payload
        );
    }
}
