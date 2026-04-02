package com.felixstudio.automationcontrol.entity.genericRevamp;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.felixstudio.automationcontrol.config.mybatis.JsonObjectTypeHandler;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


/**
 * 通版更新
 *
 * @author FelixYu
 * @date 2026-01-10
 */
@Data
@TableName(value = "general_version_edit", autoResultMap = true)
public class GeneralVersionEdit {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 日期
     */
    private LocalDate date;
    /**
     * 编辑
     */
    private String editor;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 新代码
     */
    private String newCode;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 新价格
     */
    private String newPrice;
    /**
     * 类型
     */
    private String type;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Map<String,String>> operationRemark;
    /**
     * 备注
     */
    private String remark;

    /**
     * 创建于
     */
    private LocalDateTime createdAt;
    /**
     * 更新于
     */
    private LocalDateTime updatedAt;
}
