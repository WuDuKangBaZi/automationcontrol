// Java
package com.felixstudio.automationcontrol.config;

import com.alibaba.fastjson2.JSONObject;
import com.dingtalk.open.app.api.OpenDingTalkClient;
import com.dingtalk.open.app.api.OpenDingTalkStreamClientBuilder;
import com.dingtalk.open.app.api.callback.OpenDingTalkCallbackListener;
import com.dingtalk.open.app.api.security.AuthClientCredential;
import com.felixstudio.automationcontrol.service.dingTalk.DingTalkMenu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class DingTalkStreamConfig {

    @Value("${dingtalk.appKey}")
    private String appKey;
    @Value("${dingtalk.appSecret}")
    private String appSecret;
    private final DingTalkMenu dingTalkMenu;

    public DingTalkStreamConfig(DingTalkMenu dingTalkMenu) {
        this.dingTalkMenu = dingTalkMenu;
    }

    @Bean
    public OpenDingTalkCallbackListener<JSONObject, JSONObject> botMessageListener() {
        return payload -> {
//            log.debug(payload.toString());
            JSONObject textObject = payload.getJSONObject("text");
            String openThreadId = payload.getString("openThreadId");
            if (textObject.containsKey("isReplyMsg")) {
                //回复型消息
                String replyText = textObject.getJSONObject("repliedMsg").getJSONObject("content").getString("text");
                String menuTag = replyText.split("&")[1];
                if ("vek#".equals(menuTag.substring(0, 4))) {
                    dingTalkMenu.saveVerifyMenu(menuTag.substring(4), textObject.getString("content"), payload.getString("senderNick"), openThreadId);
                }

            }
            String content = textObject.getString("content").trim();
            if (content.startsWith("注册")) {
                String shortName = "";
                if (content.split("#").length > 1) {
                    // 注册带有参数的逻辑
                    shortName = content.split("#")[1];
                }
                dingTalkMenu.saveGroupInfo(openThreadId, payload.getString("conversationTitle"), shortName, openThreadId);
            } else if (content.startsWith("webhook")) {
                String webhookUrl = content.substring(7).trim();
                dingTalkMenu.saveWebhookUrl(webhookUrl, openThreadId);
            } else {
                log.info("收到普通消息: {}", textObject.getString("content"));
            }
            JSONObject ack = new JSONObject();
            ack.put("success", true);
            return ack;
        };
    }

    @Bean
    public ApplicationRunner startDingTalkStream(OpenDingTalkCallbackListener<JSONObject, JSONObject> botMessageListener) {
        return args -> {
            OpenDingTalkClient client = OpenDingTalkStreamClientBuilder
                    .custom()
                    .credential(new AuthClientCredential(appKey, appSecret))
                    .registerCallbackListener("/v1.0/im/bot/messages/get", botMessageListener)
                    .build();
            client.start();
            log.info("钉钉 Stream Client 已启动");
        };
    }
}
