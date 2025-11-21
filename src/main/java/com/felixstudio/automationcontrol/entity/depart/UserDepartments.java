package com.felixstudio.automationcontrol.entity.depart;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDepartments {
    @TableId(type = IdType.AUTO)
    private Long id;
    private long userId;
    private long departmentId;
    private LocalDate createdTime;
}
