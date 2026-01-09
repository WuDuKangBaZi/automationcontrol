package com.felixstudio.automationcontrol.mapper.invoice;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.dto.invoice.InvoiceQueryDTO;
import com.felixstudio.automationcontrol.entity.invoice.InvoiceOperationHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InvoiceOperationHistoryMapper extends BaseMapper<InvoiceOperationHistory> {
    List<InvoiceOperationHistory> findUnfinished(InvoiceQueryDTO queryDTO);

    List<InvoiceOperationHistory> getNextByStep(InvoiceQueryDTO queryDTO);

    List<String> getPlatforms();

    List<String> getShops(String platform);
}
