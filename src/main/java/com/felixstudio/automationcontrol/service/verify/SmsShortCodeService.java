package com.felixstudio.automationcontrol.service.verify;

import com.felixstudio.automationcontrol.entity.verify.BusinessSmsInfo;
import com.felixstudio.automationcontrol.entity.verify.SmSCodeEntity;
import com.felixstudio.automationcontrol.mapper.verify.BusinessSmSInfoMapper;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class SmsShortCodeService {

    private final Cache<String, SmSCodeEntity> smsCodeCache;
    private final Random random = new Random();
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private final BusinessSmSInfoMapper businessSmSInfoMapper;

    public SmsShortCodeService(Cache<String, SmSCodeEntity> smsCodeCache, BusinessSmSInfoMapper businessSmSInfoMapper) {
        this.smsCodeCache = smsCodeCache;
        this.businessSmSInfoMapper = businessSmSInfoMapper;
    }

    public String createShortCode(String businessId){
        String shortCode = randomShortCode();
        SmSCodeEntity entity = new SmSCodeEntity(businessId, shortCode, null, System.currentTimeMillis());
        smsCodeCache.put(shortCode, entity);
        return shortCode;
    }

    public boolean submitSmS(String shortCode, String sms, String senderNick) {
        log.info("标记为 {} 的验证码值为: {}", shortCode.trim(), sms);
        SmSCodeEntity entity = smsCodeCache.getIfPresent(shortCode);
        if(entity == null){
            log.warn("提交验证码失败，ShortCode: {} 不存在或已过期", shortCode.trim());
            return false;
        }
        entity.setCode(sms.trim());
        BusinessSmsInfo smsInfo = businessSmSInfoMapper.selectById(entity.getBusinessId());
        smsInfo.setVerifyCode(sms.trim());
        smsInfo.setSubmitUser(senderNick);
        businessSmSInfoMapper.updateById(smsInfo);
        return true;
    }

    public String consumeSmS(String shortCode) {
        SmSCodeEntity entity = smsCodeCache.getIfPresent(shortCode);
        if (entity == null) return null;
        smsCodeCache.invalidate(shortCode);
        return entity.getSms();
    }

    public boolean deleteShortCode(String shortCode) {
        SmSCodeEntity entity = smsCodeCache.getIfPresent(shortCode);
        if (entity == null) return false;
        smsCodeCache.invalidate(shortCode);
        return true;
    }

    private String randomShortCode() {
        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            sb.append(LETTERS.charAt(random.nextInt(LETTERS.length())));
        }
        return sb.toString();
    }
}