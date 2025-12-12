package com.felixstudio.automationcontrol.entity.presale.run;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
// 运行任务实体类
@Data
public class RunTask {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id; // 生成任务的ID
    private String shopName;
    private Long presaleId;
    private Long erpId;
    private int status; // 0:未处理(default) 1:已处理
    private String result; // 成功/失败
    private String message; // 任务处理信息
    private String screenshotPath; // 截图路径
}
