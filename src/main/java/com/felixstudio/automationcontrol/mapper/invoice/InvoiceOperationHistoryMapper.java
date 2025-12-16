package com.felixstudio.automationcontrol.mapper.invoice;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.felixstudio.automationcontrol.entity.invoice.InvoiceOperationHistory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InvoiceOperationHistoryMapper extends BaseMapper<InvoiceOperationHistory> {
}
