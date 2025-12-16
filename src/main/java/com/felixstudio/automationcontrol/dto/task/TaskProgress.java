package com.felixstudio.automationcontrol.dto.task;

import lombok.Data;

@Data
public class TaskProgress {
    private String shopName;
    private Integer pending;
    private Integer running;
    private Integer success;
    private Integer failed;
    private Integer canceled;
    private Integer notGoods;
    private Integer successGoods;
    private Integer failedGoods;
    private Integer canceledGoods;
}
