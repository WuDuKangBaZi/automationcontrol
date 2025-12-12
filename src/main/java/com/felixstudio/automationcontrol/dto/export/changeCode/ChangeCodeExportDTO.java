package com.felixstudio.automationcontrol.dto.export.changeCode;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.felixstudio.automationcontrol.mapper.changeCode.ChangeCodeExportMapper;
import lombok.Data;

@Data
public class ChangeCodeExportDTO{
    @ExcelProperty("配置日期")
    private String configDate;
    @ExcelProperty("店铺名称")
    private String shop;
    @ExcelProperty("旧编码")
    private String oldCode;
    @ExcelProperty("新编码")
    private String newCode;
    @ExcelProperty("配置时商品名称")
    private String configGoodsName;
    @ExcelProperty("备注")
    private String remark;
    @ExcelProperty("通知人")
    private String notifiedPerson;
    @ExcelProperty("状态")
    private String taskStatus;
    @ExcelProperty("商品ID")
    private String goodsId;
    @ExcelProperty("店铺商品名称")
    private String goodsName;
    @ExcelProperty("系统款式编码")
    private String systemStyleCode;
    @ExcelProperty("操作时间")
    private String operatorTime;
    @ExcelProperty("操作类型")
    private String operatorType;
    @ExcelProperty("操作平台")
    private String platform;
    @ExcelProperty("操作结果")
    private String statusText;
    @ExcelProperty("错误日志")
    private String errorMessage;


}
