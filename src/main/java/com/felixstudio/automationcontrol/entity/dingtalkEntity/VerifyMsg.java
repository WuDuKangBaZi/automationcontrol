package com.felixstudio.automationcontrol.entity.dingtalkEntity;

import lombok.Data;

import java.util.List;

@Data
public class VerifyMsg {
    private String flowName;
    private String verifyPhone;
    private String shortName;
    private List<String> atMobiles;
}
