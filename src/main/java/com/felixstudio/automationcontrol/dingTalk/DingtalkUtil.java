package com.felixstudio.automationcontrol.dingTalk;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class DingtalkUtil {
    @Value("${dingtalk.appKey}")
    private String appKey;
    @Value("${dingtalk.appSecret}")
    private String appSecret;
    @Value("${dingtalk.captch}")
    private String captcha;
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

        Map<String, String> body = new HashMap<>();
        appKey = "dingfhq42qsw2izwrkb4";
        appSecret = "pr91tjS3U7B5VygDYCdFsxvc2O42o0p90ismzIek7HpwW2aZY3cd7nqEAWauAzao";
        body.put("appKey", appKey);
        body.put("appSecret", appSecret);

        HttpEntity<Map<String, String>> req = new HttpEntity<>(body, headers);
        ResponseEntity<Map> resp = restTemplate.postForEntity(TOKEN_URL, req, Map.class);

        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
            throw new IllegalStateException("获取钉钉 accessToken 失败，HTTP=" + resp.getStatusCode());
        }

        Object tokenObj = resp.getBody().get("accessToken");
        Object expireInObj = resp.getBody().get("expireIn"); // 单位：秒

        if (!(tokenObj instanceof String) || !(expireInObj instanceof Number)) {
            throw new IllegalStateException("获取钉钉 accessToken 返回数据异常");
        }

        String token = (String) tokenObj;
        long expireInSec = ((Number) expireInObj).longValue();
        long buffer = Math.min(REFRESH_BUFFER_SECONDS, Math.max(0, expireInSec / 10)); // 动态留出缓冲
        long ttlSec = Math.max(1, expireInSec - buffer);

        this.cachedToken = token;
        this.expireAtEpochMs = System.currentTimeMillis() + ttlSec * 1000L;
    }

    public RestOperations getRestTemplate() {
        return this.restTemplate;
    }
}
