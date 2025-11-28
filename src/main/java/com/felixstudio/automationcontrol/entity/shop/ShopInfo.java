package com.felixstudio.automationcontrol.entity.shop;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("shop_info")
public class ShopInfo {

    @TableId(type = IdType.AUTO)
    private Long id;                    // 主键ID

    private String shopName;            // 店铺名称

    private String loginAccount;        // 登录账号

    private String loginPassword;       // 登录密码（建议密文保存）

    private String businessType;        // 业务分类

    private Integer status;             // 状态（1 启用，0 禁用）

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;    // 创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;    // 更新时间

    private String remark;              // 备注

    private String verifyPhone; // 接收验证码的手机号
}