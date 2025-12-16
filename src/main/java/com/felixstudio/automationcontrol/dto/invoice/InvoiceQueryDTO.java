package com.felixstudio.automationcontrol.dto.invoice;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InvoiceQueryDTO {
    private String orderNo;
    private String platform;
    private String shopName;
    private LocalDateTime createdAt;
}
