package com.felixstudio.automationcontrol.entity.verify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmSCodeEntity {
    private String businessId;
    private String code;
    private String sms;
    private Long createTime;
}
