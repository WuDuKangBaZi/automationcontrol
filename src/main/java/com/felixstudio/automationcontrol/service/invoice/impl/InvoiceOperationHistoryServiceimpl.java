package com.felixstudio.automationcontrol.service.invoice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.dao.invoice.invoiceUpdateDAO;
import com.felixstudio.automationcontrol.dto.invoice.InvoiceQueryDTO;
import com.felixstudio.automationcontrol.entity.invoice.InvoiceOperationHistory;
import com.felixstudio.automationcontrol.mapper.invoice.InvoiceOperationHistoryMapper;
import com.felixstudio.automationcontrol.service.invoice.InvoiceOperationHistoryService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class InvoiceOperationHistoryServiceimpl extends ServiceImpl<InvoiceOperationHistoryMapper, InvoiceOperationHistory> implements InvoiceOperationHistoryService {
    @Override
    public List<BatchResult> batchInstallInvoiceOperationHistories(List<InvoiceOperationHistory> histories) {
        return this.getBaseMapper().insert(histories);
    }

    @Override
    public boolean findUnfinishedInvoiceOperationHistoryByPlatformAndShopName(InvoiceQueryDTO queryDTO) {
        // 查询各个步骤下是否存在未完成任务
        return !this.getBaseMapper().findUnfinished(queryDTO).isEmpty();

    }

    @Override
    public List<InvoiceOperationHistory> getNextByStep(InvoiceQueryDTO queryDTO) {
        return this.getBaseMapper().getNextByStep(queryDTO);
    }

    @Override
    public List<String> getPlatforms() {
        return this.getBaseMapper().getPlatforms();
    }

    @Override
    public List<String> getShops(String platform) {
        return this.getBaseMapper().getShops(platform);
    }

    @Override
    public IPage<InvoiceOperationHistory> queryByDTO(InvoiceQueryDTO queryDTO) {
        LambdaQueryWrapper<InvoiceOperationHistory> queryWrapper = new LambdaQueryWrapper<>();
        log.info(queryDTO.toString());
        if (queryDTO.getOrderNo() != null && !queryDTO.getOrderNo().isEmpty()) {
            queryWrapper.eq(InvoiceOperationHistory::getOrderNo, queryDTO.getOrderNo());
        }
        if (queryDTO.getPlatform() != null && !queryDTO.getPlatform().isEmpty()) {
            queryWrapper.eq(InvoiceOperationHistory::getPlatform, queryDTO.getPlatform());
        }
        if (queryDTO.getShopName() != null && !queryDTO.getShopName().isEmpty()) {
            queryWrapper.eq(InvoiceOperationHistory::getShopName, queryDTO.getShopName());
        }
        if (queryDTO.getCreatedAt() != null) {
            queryWrapper.eq(InvoiceOperationHistory::getCreatedAt, queryDTO.getCreatedAt());
        }
        if (queryDTO.getStatus() != null) {
            queryWrapper.eq(InvoiceOperationHistory::getStatus, queryDTO.getStatus());

        }
        Page<InvoiceOperationHistory> page = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize());
        return this.page(page, queryWrapper);
    }

    @SneakyThrows
    @Override
    public void updateByStep(invoiceUpdateDAO updateDAO) {
        // 根据步骤来更新
        InvoiceOperationHistory history = this.getBaseMapper().selectById(Long.valueOf(updateDAO.getId()));
        if (history == null) {
            throw new ArrayIndexOutOfBoundsException("未找到对应的发票操作记录，ID：" + updateDAO.getId());
        } else {
            // 根据步骤更新状态
            switch (updateDAO.getStep()) {
                case "失败":
                    history.setStatus(2);
                    history.setFailReason(updateDAO.getResult());
                    break;
                case "聚水潭验证":
                    history.setVerifiedInJstTime(LocalDateTime.now());
                    boolean invoiceB = Objects.equals(updateDAO.getResult(), "需要开票");
                    history.setInvoicedB(invoiceB);
                    if (!invoiceB) {
                        history.setStatus(1);
                    }
                    break;
                case "开票":
                    history.setInvoicedTime(LocalDateTime.now());
                    history.setInvoicePdfPath(updateDAO.getResult());
                    break;
                case "登记回传":
                    history.setRegisteredSendbackTime(LocalDateTime.now());
                    break;
                case "回传完成":
                    history.setSendbackDoneTime(LocalDateTime.now());
                    break;
                case "标记完成":
                    history.setMarkInJstTime(LocalDateTime.now());
                    history.setStatus(1);
                    break;
                default:
                    throw new AccountNotFoundException("未知的步骤：" + updateDAO.getStep());
            }
            log.info(history.toString());
            this.getBaseMapper().updateById(history);
        }
    }

    @Override
    public Object setManual(List<String> orderNos) {
        List<String> req = new ArrayList<>();
        for (String order : orderNos){
            LambdaQueryWrapper<InvoiceOperationHistory> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(InvoiceOperationHistory::getOrderNo, order);
            List<InvoiceOperationHistory> histories = this.list(queryWrapper);
            if (histories.isEmpty()) {
                log.warn("未找到订单号为 {} 的发票操作记录，无法设置为人工处理", order);
                req.add("订单号 " + order + " 未找到发票操作记录");
                continue;
            }
            for (InvoiceOperationHistory history : histories) {
                history.setStatus(-1); // -1表示人工处理
                this.updateById(history);
                req.add("订单号 " + order + " 的发票操作记录已设置为人工处理");
                log.info("订单号 {} 的发票操作记录已设置为人工处理", order);
            }
        }
        return req;
    }
}
