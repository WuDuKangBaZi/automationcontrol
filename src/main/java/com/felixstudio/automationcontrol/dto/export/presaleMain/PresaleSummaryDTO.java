package com.felixstudio.automationcontrol.dto.export.presaleMain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class PresaleSummaryDTO {
    @ExcelProperty("店铺名称")
    private String shopName;
    @ExcelProperty("商品编码")
    private String goodsCode;
    @ExcelProperty("商品名称")
    private String goodsName;
    @ExcelProperty("ERP编码数")
    private Long erpCount = 0L;
    @ExcelProperty("任务总数")
    private Long taskCount = 0L;
    @ExcelProperty("成功任务数")
    private Long taskSuccess = 0L;
    @ExcelProperty("失败任务数")
    private Long taskFail = 0L;
    @ExcelProperty("进行中任务数")
    private Long taskDoing = 0L;
    @ExcelProperty("待处理任务数")
    private Long taskWaiting = 0L;
    @ExcelProperty("结果总数")
    private Long resultCount = 0L;
}
