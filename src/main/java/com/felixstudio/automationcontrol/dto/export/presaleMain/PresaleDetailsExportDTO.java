package com.felixstudio.automationcontrol.dto.export.presaleMain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PresaleDetailsExportDTO {
    @ExcelProperty("店铺名称")
    private String shopName;
    @ExcelProperty("配置日期")
    private String configDate;
    @ExcelProperty("配置时间")
    private String configTime;
    @ExcelProperty("配置编码")
    private String goodsCode;
    @ExcelProperty("商品名称")
    private String goodsName;
    @ExcelProperty("缺货原因")
    private String shortageReason;
    @ExcelProperty("预售截止时间")
    private String presaleEndTime;
    @ExcelProperty("处理方式")
    private String handlingMethod;
    @ExcelProperty("负责人")
    private String personInCharge;
    @ExcelProperty("ERP编码")
    private String erpCode;
    @ExcelProperty("ERP名称")
    private String erpName;
    @ExcelProperty("商品ID")
    private String goodsId;
    @ExcelProperty("SKU")
    private String sku;
    @ExcelProperty("SKU处理方式")
    private String skuHandlingMethod;
    @ExcelProperty("SKU预售日期")
    private String skuPresaleDate;
    @ExcelProperty("任务状态")
    private String taskStatus;
    @ExcelProperty("附件列表")
    private String taskFiles;
}
