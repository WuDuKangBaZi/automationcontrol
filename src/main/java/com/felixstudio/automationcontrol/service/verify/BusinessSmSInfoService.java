package com.felixstudio.automationcontrol.service.verify;

import com.felixstudio.automationcontrol.entity.verify.BusinessSmsInfo;
import com.felixstudio.automationcontrol.mapper.verify.BusinessSmSInfoMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BusinessSmSInfoService {
    private final BusinessSmSInfoMapper businessSmSInfoMapper;

    public BusinessSmSInfoService(BusinessSmSInfoMapper businessSmSInfoMapper) {
        this.businessSmSInfoMapper = businessSmSInfoMapper;
    }
    public void save(BusinessSmsInfo businessSmSInfo) {
        businessSmSInfoMapper.insert(businessSmSInfo);
    }

    public BusinessSmsInfo getByShortCode(String shortCode) {
        try{
            return businessSmSInfoMapper.selectByMap(Map.of("short_code", shortCode)).get(0);
        }catch (IndexOutOfBoundsException e){
            return null;
        }
    }
    public BusinessSmsInfo updateById(BusinessSmsInfo businessSmSInfo) {
        businessSmSInfoMapper.updateById(businessSmSInfo);
        return businessSmSInfo;
    }

    public BusinessSmsInfo getById(String businessId) {
        return businessSmSInfoMapper.selectById(businessId);
    }
}
