package com.felixstudio.automationcontrol.entity.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

@Data
@TableName("task_log_files")
public class TaskLogFiles {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long taskId;

    private String filePath;

    private String originalName;

    private Long fileSize;

    private LocalDateTime createdAt;
}
