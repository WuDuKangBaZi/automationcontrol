package com.felixstudio.automationcontrol.service.invoice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.felixstudio.automationcontrol.entity.invoice.InvoiceOperationHistory;
import com.felixstudio.automationcontrol.mapper.invoice.InvoiceOperationHistoryMapper;
import com.felixstudio.automationcontrol.service.invoice.InvoiceOperationHistoryService;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class InvoiceOperationHistoryServiceimpl extends ServiceImpl<InvoiceOperationHistoryMapper, InvoiceOperationHistory> implements InvoiceOperationHistoryService {
    @Override
    public List<BatchResult> batchInstallInvoiceOperationHistories(List<InvoiceOperationHistory> histories) {
        return this.getBaseMapper().insert(histories);
    }
}
