package com.felixstudio.automationcontrol.entity.task;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.felixstudio.automationcontrol.config.mybatis.JsonObjectTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("task_job")
public class TaskJob {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 任务类型，例如：FETCH_ERP_DATA、PRESALE_MAIN_TASK 等
     */
    private String taskType;

    /**
     * 关联来源类型：main / erp
     */
    private String refType;

    /**
     * 关联主键 ID
     * presale_main.id 或 presale_erp_code.id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long refId;

    /**
     * 店铺 ID，可空
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long shopId;

    /**
     * 任务状态：0=待执行 1=执行中 2=成功 3=失败
     */
    private Integer taskStatus;

    /**
     * 任务参数 JSON：MyBatis-Plus + PG 可直接映射 Map
     */
    @TableField(typeHandler = JsonObjectTypeHandler.class)
    private JSONObject taskParams;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 错误消息
     */
    private String errorMessage;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

}
