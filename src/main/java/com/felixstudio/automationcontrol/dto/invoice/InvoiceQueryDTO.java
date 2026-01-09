package com.felixstudio.automationcontrol.dto.invoice;

import com.felixstudio.automationcontrol.dto.PagerDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InvoiceQueryDTO extends PagerDTO {
    private String orderNo;
    private String platform;
    private String shopName;
    private LocalDateTime createdAt;
    private String step;
}
