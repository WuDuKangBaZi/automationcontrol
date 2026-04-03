package com.felixstudio.automationcontrol.dao.distri;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DistributionDAO {
    private LocalDate configDate;

    private String keyword;

    private double price;
    @TableField(value="store",typeHandler = com.felixstudio.automationcontrol.config.mybatis.StringArrayTypeHandler.class)
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
