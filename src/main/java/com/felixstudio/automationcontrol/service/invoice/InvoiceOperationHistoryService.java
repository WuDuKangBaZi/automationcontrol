package com.felixstudio.automationcontrol.service.invoice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.dao.invoice.invoiceUpdateDAO;
import com.felixstudio.automationcontrol.dto.invoice.InvoiceQueryDTO;
import com.felixstudio.automationcontrol.entity.invoice.InvoiceOperationHistory;
import org.apache.ibatis.executor.BatchResult;

import java.util.List;

public interface InvoiceOperationHistoryService extends IService<InvoiceOperationHistory> {
    List<BatchResult> batchInstallInvoiceOperationHistories(List<InvoiceOperationHistory> histories);

    boolean findUnfinishedInvoiceOperationHistoryByPlatformAndShopName(InvoiceQueryDTO queryDTO);

    List<InvoiceOperationHistory> getNextByStep(InvoiceQueryDTO queryDTO);

    List<String> getPlatforms();

    List<String> getShops(String platform);

    IPage<InvoiceOperationHistory> queryByDTO(InvoiceQueryDTO queryDTO);

    void updateByStep(invoiceUpdateDAO updateDAO);
}
