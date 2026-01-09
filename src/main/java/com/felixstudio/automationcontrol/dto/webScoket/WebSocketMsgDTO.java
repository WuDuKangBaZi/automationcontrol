package com.felixstudio.automationcontrol.dto.webScoket;

import lombok.Data;

@Data
public class WebSocketMsgDTO {
    private String groupName;
    private String message;
    private String messageType;
    private String messageTitle;
}
