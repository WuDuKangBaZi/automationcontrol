package com.felixstudio.automationcontrol.entity.verify;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("business_sms_info")
public class BusinessSmsInfo {

    @TableId("business_id")
    private String businessId;  // 主键 UUID

    @TableField("short_code")
    private String shortCode;

    @TableField("verify_code")
    private String verifyCode;

    @TableField("flow_name")
    private String flowName;

    @TableField("phone_number")
    private String phoneNumber;

    @TableField("submit_user")
    private String submitUser;

    @TableField("create_at")
    private LocalDateTime createAt;

    @TableField("update_at")
    private LocalDateTime updateAt;
}

