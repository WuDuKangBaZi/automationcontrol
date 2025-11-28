package com.felixstudio.automationcontrol.entity.task;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.felixstudio.automationcontrol.config.mybatis.JsonObjectTypeHandler;
import com.felixstudio.automationcontrol.mapper.task.TaskResultMapper;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskResults{
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long taskId;
    private String taskKey;
    @TableField(typeHandler = JsonObjectTypeHandler.class)
    private JSONObject resultData;
    private int status;
    private String message;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
