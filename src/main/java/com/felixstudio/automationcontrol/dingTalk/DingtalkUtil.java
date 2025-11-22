package com.felixstudio.automationcontrol.dingTalk;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class DingtalkUtil {
    @Value("${dingtalk.appKey}")
    private String appKey;
    @Value("${dingtalk.appSecret}")
    private String appSecret;
    private static final String TOKEN_URL = "https://api.dingtalk.com/v1.0/oauth2/accessToken";
    private static final long REFRESH_BUFFER_SECONDS = 120;
    private final RestTemplate restTemplate = new RestTemplate();

    // 简单内存缓存
    private volatile String cachedToken;
    private volatile long expireAtEpochMs;
    private final Object lock = new Object();

    public String getAccessToken() {
        long now = System.currentTimeMillis();
        if (cachedToken != null && now < expireAtEpochMs) {
            return cachedToken;
        }
        synchronized (lock) {
            now = System.currentTimeMillis();
            if (cachedToken != null && now < expireAtEpochMs) {
                return cachedToken;
            }
            refreshToken();
            return cachedToken;
        }
    }

    public void applyAuthHeader(HttpHeaders headers) {
        headers.set("x-acs-dingtalk-access-token", getAccessToken());
    }

    public JSONObject sendMessage(JSONObject message) {
        String url = "https://api.dingtalk.com/v1.0/robot/groupMessages/send";
        HttpHeaders headers = new HttpHeaders();
        applyAuthHeader(headers);
        headers.setContentType(MediaType.APPLICATION_JSON);
        log.debug(message.toString());
        HttpEntity<JSONObject> entity = new HttpEntity<>(message, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        return JSONObject.parseObject(responseEntity.getBody());
    }
    private void refreshToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject body = new JSONObject();
        appKey = "dingfhq42qsw2izwrkb4";
        appSecret = "pr91tjS3U7B5VygDYCdFsxvc2O42o0p90ismzIek7HpwW2aZY3cd7nqEAWauAzao";
        body.put("appKey", appKey);
        body.put("appSecret", appSecret);

        HttpEntity<JSONObject> req = new HttpEntity<>(body, headers);
        ResponseEntity<JSONObject> resp = restTemplate.postForEntity(TOKEN_URL, req, JSONObject.class);

        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
            throw new IllegalStateException("获取钉钉 accessToken 失败，HTTP=" + resp.getStatusCode());
        }

        String accessToken = resp.getBody().getString("accessToken");
        long expireIn = resp.getBody().getLongValue("expireIn"); // 单位：秒

        if (accessToken == null) {
            throw new IllegalStateException("获取钉钉 accessToken 返回数据异常");
        }

        long buffer = Math.min(REFRESH_BUFFER_SECONDS, Math.max(0, expireIn / 10)); // 动态留出缓冲
        long ttlSec = Math.max(1, expireIn - buffer);

        this.cachedToken = accessToken;
        this.expireAtEpochMs = System.currentTimeMillis() + ttlSec * 1000L;
    }

    public RestOperations getRestTemplate() {
        return this.restTemplate;
    }
}
