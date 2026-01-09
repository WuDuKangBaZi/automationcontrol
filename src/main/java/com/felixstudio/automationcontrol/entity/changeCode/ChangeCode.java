package com.felixstudio.automationcontrol.entity.changeCode;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName("change_code")
public class ChangeCode {

    @Schema(description = "主键ID,雪花算法生成")
    @TableId(type = IdType.ASSIGN_ID)
    private Long Id;

    @Schema(description = "配置日期，格式yyyy/M/d")
    @JsonFormat(pattern = "yyyy/M/d")
    @TableField("config_date")
    private LocalDate configDate;

    @Schema(description = "旧编码")
    @TableField("old_code")
    private String oldCode;

    @Schema(description = "商品名称")
    @TableField("goods_name")
    private String goodsName;

    @Schema(description = "新编码")
    @TableField("new_code")
    private String newCode;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;

    @Schema(description = "通知人")
    @TableField("notified_person")
    private String notifiedPerson;
}
