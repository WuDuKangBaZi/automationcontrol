package com.felixstudio.automationcontrol.entity.presale.erp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ERPTask {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private Long mainId; // presaleMain表的id
    private int status; // 0:未处理(default) 1:已处理
    private LocalDate createdTime; // 任务创建时间 default 当前时间
    private LocalDate completedTime; // 任务完成时间
}
