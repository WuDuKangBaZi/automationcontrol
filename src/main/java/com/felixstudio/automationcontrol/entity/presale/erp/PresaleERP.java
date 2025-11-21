package com.felixstudio.automationcontrol.entity.presale.erp;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PresaleERP {
    private Long id;
    private Long presaleMainId; // presaleMain表的id
    private String erpCode; // ERP编码
    private String erpName; // ERP名称
    private LocalDate collectedAt; // 采集时间
}
