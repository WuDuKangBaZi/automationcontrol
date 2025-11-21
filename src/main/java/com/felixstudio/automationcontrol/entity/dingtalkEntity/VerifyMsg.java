package com.felixstudio.automationcontrol.entity.dingtalkEntity;

import lombok.Data;

@Data
public class VerifyMsg {
    private String flowName;
    private String verifyPhone;
    private String chanelId;
}
