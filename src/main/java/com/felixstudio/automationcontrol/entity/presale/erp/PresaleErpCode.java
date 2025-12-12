package com.felixstudio.automationcontrol.entity.presale.erp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PresaleErpCode {
    @TableId(value="id",type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private Long presaleId; // presaleMain表的id
    private String erpCode; // ERP编码
    private String erpName; // ERP名称
    private LocalDateTime collectedAt; // 采集时间
}
