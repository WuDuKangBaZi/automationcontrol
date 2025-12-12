package com.felixstudio.automationcontrol.service.verify;

import com.alibaba.fastjson2.JSONObject;
import com.felixstudio.automationcontrol.dingTalk.DingTalkMessageBuilder;
import com.felixstudio.automationcontrol.dingTalk.DingtalkUtil;
import com.felixstudio.automationcontrol.entity.dingtalkEntity.ChatGroupInfo;
import com.felixstudio.automationcontrol.entity.verify.BusinessSmsInfo;
import com.felixstudio.automationcontrol.entity.verify.SmSCodeEntity;
import com.felixstudio.automationcontrol.mapper.dingTalk.ChatGroupInfoMapper;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class VerifyService {
    private final BusinessSmSInfoService businessSmSInfoService;
    private final SmsShortCodeService smsShortCodeService;
    private final ChatGroupInfoMapper chatGroupInfoMapper;
    private final DingTalkMessageBuilder dingTalkMessageBuilder;
    private final Cache<String, SmSCodeEntity> smsCodeCache;

    public VerifyService(BusinessSmSInfoService businessSmSInfoService, SmsShortCodeService smsShortCodeService, ChatGroupInfoMapper chatGroupInfoMapper, DingTalkMessageBuilder dingTalkMessageBuilder, Cache<String, SmSCodeEntity> smsCodeCache) {
        this.businessSmSInfoService = businessSmSInfoService;
        this.smsShortCodeService = smsShortCodeService;
        this.chatGroupInfoMapper = chatGroupInfoMapper;
        this.dingTalkMessageBuilder = dingTalkMessageBuilder;
        this.smsCodeCache = smsCodeCache;
    }

    // 发起短信验证码请求
    public String sendSmSCode(String flowName, String verifyPhone, String shortName, List<String> atMobiles) {
        // 流程名称、手机号、发送到指定的ID的群聊 该ID在网页端可以查询到
        String businessId = UUID.randomUUID().toString();
        // 根据shortName查询到对应的chanelId
        List<ChatGroupInfo> list = chatGroupInfoMapper.selectByMap(Map.of("short_name", shortName));
        Optional<ChatGroupInfo> opt = list.stream().findFirst();
        if (opt.isEmpty()) {
            throw new RuntimeException("未找到对应的群聊信息");
        }
        String shortCode = smsShortCodeService.createShortCode(businessId);
        // 调用钉钉接口发送短信
        DingtalkUtil dingtalkUtil = new DingtalkUtil();
        if (atMobiles == null || atMobiles.isEmpty()) {
            JSONObject msgObject = dingTalkMessageBuilder.sampleText("流程" + flowName + "需要的验证码已经发送到:" + verifyPhone + "，请直接使用回复功能回复本条消息来发送验证码&vek#" + shortCode, opt.get().getGroupId());
            dingtalkUtil.sendMessage(msgObject);
        }else{
            JSONObject messageBody = dingTalkMessageBuilder.buildAtMessage("流程" + flowName + "需要的验证码已经发送到:" + verifyPhone + "，请直接使用回复功能回复本条消息来发送验证码&vek#" + shortCode, atMobiles);
            dingtalkUtil.sendWebHookMessage(opt.get().getWebhookUrl(), messageBody);
        }
        BusinessSmsInfo businessSmSInfo = new BusinessSmsInfo();
        businessSmSInfo.setBusinessId(businessId);
        businessSmSInfo.setFlowName(flowName);
        businessSmSInfo.setPhoneNumber(verifyPhone);
        businessSmSInfo.setShortCode(shortCode);
        businessSmSInfoService.save(businessSmSInfo);
        return shortCode;
    }

    public String queryByShortCode(String shortCode) {
        SmSCodeEntity entity = smsCodeCache.getIfPresent(shortCode);
        if(entity==null){
            throw new NullPointerException("验证码已过期!");
        }
        log.info(entity.toString());
        BusinessSmsInfo businessSmsInfo = businessSmSInfoService.getById(entity.getBusinessId());
        String verifyCode = businessSmsInfo != null ? businessSmsInfo.getVerifyCode() : null;
        if(verifyCode != null){
            businessSmsInfo.setShortCode(shortCode+"_get");
            businessSmSInfoService.updateById(businessSmsInfo);
        }
        return verifyCode;
    }
}
