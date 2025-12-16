package com.felixstudio.automationcontrol.controller.invoice;

import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.dto.invoice.InvoiceQueryDTO;
import com.felixstudio.automationcontrol.entity.invoice.InvoiceOperationHistory;
import com.felixstudio.automationcontrol.service.invoice.InvoiceOperationHistoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/invoice")
public class InvoiceOperationHistoryController {
    private final InvoiceOperationHistoryService invoiceOperationHistoryService;


    public InvoiceOperationHistoryController(InvoiceOperationHistoryService invoiceOperationHistoryService) {
        this.invoiceOperationHistoryService = invoiceOperationHistoryService;
    }

    @PostMapping("/batchInster")
    public ApiResponse<?> batchInster(@RequestBody List<InvoiceOperationHistory> histories){
        int res = invoiceOperationHistoryService.batchInstallInvoiceOperationHistories(histories).toArray().length;
        return ApiResponse.success(res);
    }
    @PostMapping("/add")
    public ApiResponse<?> add(@RequestBody InvoiceOperationHistory history) {
        boolean res = invoiceOperationHistoryService.save(history);
        return ApiResponse.success(res);
    }
    @PostMapping("/query")
    public ApiResponse<?> query(@RequestBody InvoiceQueryDTO queryDTO) {
        return null;
    }
}
