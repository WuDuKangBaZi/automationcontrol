package com.felixstudio.automationcontrol.dto.changeCode;

import lombok.Data;

import java.util.List;

@Data
public class ChangeCodeDetailsDTO {
    private String shop;
    private String status;
    private String goodsId;
    private String operator;
    private String platform;
    private String goodsName;
    private String errorMessage;
    private String operatorTime;
    private String operatorType;
    private String systemStyleCode;
}