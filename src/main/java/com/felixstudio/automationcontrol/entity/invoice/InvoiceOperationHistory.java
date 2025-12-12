package com.felixstudio.automationcontrol.entity.invoice;

import com.baomidou.mybatisplus.annotation.*;
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

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("order_no")
    private String orderNo;

    @TableField("platform")
    private String platform;

    @TableField("shop_name")
    private String shopName;

    @TableField("verified_in_jst")
    private Boolean verifiedInJst;

    @TableField("invoiced")
    private Boolean invoiced;

    @TableField("registered_sendback")
    private Boolean registeredSendback;

    @TableField("sendback_done")
    private Boolean sendbackDone;

    @TableField("mark_in_jst")
    private Boolean markInJst;

    @TableField("invoice_pdf_path")
    private String invoicePdfPath;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "update_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateAt;
}
