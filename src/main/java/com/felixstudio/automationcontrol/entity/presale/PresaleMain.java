package com.felixstudio.automationcontrol.entity.presale;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PresaleMain {
    private Long id;
    private String configDateTime;
    private String goodsCode;
    private String goodsName;
    private String shortageReason; // 缺货原因
    private String presaleEndTime; // 预售截止时间
    private String handlingMethod; // 处理方法
    private String personInCharge; // 责任人
    private LocalDate createdTime;
    private LocalDate updatedTime;
}
