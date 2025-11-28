package com.felixstudio.automationcontrol.entity.presale;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@TableName("presale_main")
@Data
public class PresaleMain {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("config_date")
    private LocalDate configDate;

    @JsonFormat(pattern = "H:mm:ss")
    @TableField("config_time")
    private LocalTime configTime;

    @TableField("goods_code")
    private String goodsCode;

    @TableField("goods_name")
    private String goodsName;

    @TableField("shortage_reason")
    private String shortageReason;

    @TableField("presale_end_time")
    private LocalDate presaleEndTime;

    @TableField("handling_method")
    private String handlingMethod;

    @TableField("person_in_charge")
    private String personInCharge;
    @TableField("is_active")
    private Boolean isActive;
    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("updated_at")
    private LocalDateTime updatedAt;
}


