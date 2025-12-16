package com.felixstudio.automationcontrol.service.invoice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.felixstudio.automationcontrol.entity.invoice.InvoiceOperationHistory;
import org.apache.ibatis.executor.BatchResult;

import java.util.List;

public interface InvoiceOperationHistoryService extends IService<InvoiceOperationHistory> {
    List<BatchResult> batchInstallInvoiceOperationHistories(List<InvoiceOperationHistory> histories);
}
