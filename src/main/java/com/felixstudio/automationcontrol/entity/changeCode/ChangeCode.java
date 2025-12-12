package com.felixstudio.automationcontrol.entity.changeCode;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName("change_code")
public class ChangeCode {
    @TableId(type = IdType.ASSIGN_ID)
    private Long Id;
    @JsonFormat(pattern = "yyyy/M/d")
    @TableField("config_date")
    private LocalDate configDate;
    @TableField("old_code")
    private String oldCode;
    @TableField("goods_name")
    private String goodsName;
    @TableField("new_code")
    private String newCode;
    @TableField("remark")
    private String remark;
    @TableField("notified_person")
    private String notifiedPerson;
}
