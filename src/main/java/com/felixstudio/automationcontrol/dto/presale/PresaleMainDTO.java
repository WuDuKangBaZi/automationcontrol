package com.felixstudio.automationcontrol.dto.presale;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.felixstudio.automationcontrol.entity.presale.PresaleMain;
import lombok.Data;

@Data
public class PresaleMainDTO
{
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String configDateTime;
    private String goodsCode;
    private String goodsName;
    private String shortageReason;
    private String presaleEndTime;
    private String handlingMethod;
    private String personInCharge;
    // 扩展字段 统计erp中和task中数据量
    private Integer mainTaskTotal;
    private Integer mainTaskSuccess;
    private Integer mainTaskFail;
    private Integer mainTaskRunning;
    // 扩展字段 统计erp中数据
    private Integer erpTaskTotal;
    private Integer erpTaskSuccess;
    private Integer erpTaskFail;
    private Integer erpTaskRunning;
    private Integer erpTaskPending;
    private Integer erpTaskEmpty;
    private Integer erpTaskCancel;
}
