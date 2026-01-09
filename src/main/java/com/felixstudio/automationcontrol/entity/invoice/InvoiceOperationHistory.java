package com.felixstudio.automationcontrol.entity.invoice;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("invoice_operation_history")
public class InvoiceOperationHistory {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("order_no")
    private String orderNo;

    @TableField("platform")
    private String platform;

    @TableField("shop_name")
    private String shopName;

    /**
     * 聚水潭验证时间
     * 非空即表示已验证
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @TableField("verified_in_jst_time")
    private LocalDateTime verifiedInJstTime;

    /**
     * 开票时间
     * 非空即表示已开票
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @TableField("invoiced_time")
    private LocalDateTime invoicedTime;

    /**
     * 登记回传时间
     * 非空即表示已登记回传
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @TableField("registered_sendback_time")
    private LocalDateTime registeredSendbackTime;

    /**
     * 回传完成时间
     * 非空即表示回传已完成
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @TableField("sendback_done_time")
    private LocalDateTime sendbackDoneTime;

    /**
     * 聚水潭标记时间
     * 非空即表示已在聚水潭标记
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @TableField("mark_in_jst_time")
    private LocalDateTime markInJstTime;

    @TableField("invoice_pdf_path")
    private String invoicePdfPath;
    /**
     * 执行状态
     * 0-处理中 (默认)
     * 1-全部完成
     * 2-异常失败 (RPA读取时应过滤掉此状态的订单)
     */
    @TableField("status")
    private Integer status;

    /**
     * 失败原因
     * 记录RPA在任一环节失败的具体报错
     */
    @TableField("fail_reason")
    private String failReason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(value = "invoiced_b")
    private boolean invoicedB;

}
