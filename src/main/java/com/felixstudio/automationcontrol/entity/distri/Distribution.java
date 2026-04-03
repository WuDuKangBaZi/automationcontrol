package com.felixstudio.automationcontrol.entity.distri;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "distribution", autoResultMap = true)
public class Distribution {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private LocalDate configDate;

    private String keyword;

    private double price;
    @TableField(value = "store", typeHandler = com.felixstudio.automationcontrol.config.mybatis.StringArrayTypeHandler.class)
    private List<String> store;
    @TableField(value="store_id",typeHandler = com.felixstudio.automationcontrol.config.mybatis.IntegerArrayTypeHandler.class)
    private List<Integer> storeId;

    private String importance;
    private LocalDate initTime;
    private LocalDate targetTime;
    private String productId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
