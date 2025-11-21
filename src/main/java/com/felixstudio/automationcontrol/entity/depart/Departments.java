package com.felixstudio.automationcontrol.entity.depart;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Departments {
    @TableId(value="id",type = IdType.AUTO)
    private Long id;
    private String deptName;
    private Long parentId;
    private String description;
    private LocalDate createdTime;
}
