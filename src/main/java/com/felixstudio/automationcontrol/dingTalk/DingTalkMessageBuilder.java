package com.felixstudio.automationcontrol.dingTalk;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// 构建钉钉消息的工具类
@Slf4j
@Component
public class DingTalkMessageBuilder {
    public JSONObject sampleText(String message,String openConversationId){
        JSONObject messageJson = new JSONObject();
        messageJson.put("content", message);
        JSONObject msgObject = new JSONObject();
        msgObject.put("msgParam", messageJson.toString());
        msgObject.put("msgKey", "sampleText");
        msgObject.put("robotCode","dingfhq42qsw2izwrkb4");
        msgObject.put("openConversationId", openConversationId);
        log.info(msgObject.toString());
        return msgObject;
    }
    public JSONObject sampleMarkdown(String title, String text){
        JSONObject messageJson = new JSONObject();
        messageJson.put("title", title);
        messageJson.put("text", text);
        return messageJson;
    }
    public JSONObject sampleImageMsg(String base64, String md5){
        JSONObject messageJson = new JSONObject();
        messageJson.put("base64", base64);
        messageJson.put("md5", md5);
        return messageJson;
    }
}
