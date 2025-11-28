package com.felixstudio.automationcontrol.dingTalk;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// 构建钉钉消息的工具类
@Slf4j
@Component
public class DingTalkMessageBuilder {

    private final String robotCode;

    public DingTalkMessageBuilder(@Value("${dingtalk.robotCode}") String robotCode) {
        this.robotCode = robotCode;
    }

    public JSONObject sampleText(String message,String openConversationId){
        JSONObject messageJson = new JSONObject();
        messageJson.put("content", message);
        JSONObject msgObject = new JSONObject();
        msgObject.put("msgParam", messageJson.toString());
        msgObject.put("msgKey", "sampleText");
        msgObject.put("robotCode",robotCode);
        msgObject.put("openConversationId", openConversationId);
        return msgObject;
    }

    public JSONObject buildFileMessage(String mediaId, String originalFilename, String fileType,String openConversationId) {
        JSONObject messageJson = new JSONObject();
        messageJson.put("mediaId", mediaId);
        messageJson.put("fileName", originalFilename);
        messageJson.put("fileType", fileType);
        JSONObject bodyObject = new JSONObject();
        bodyObject.put("msgParam", messageJson.toString());
        bodyObject.put("msgKey", "sampleFile");
        bodyObject.put("robotCode", robotCode);
        bodyObject.put("openConversationId", openConversationId);
        return bodyObject;
    }
}
