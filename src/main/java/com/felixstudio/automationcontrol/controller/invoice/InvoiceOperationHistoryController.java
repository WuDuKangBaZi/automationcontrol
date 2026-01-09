package com.felixstudio.automationcontrol.controller.invoice;

import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.dao.invoice.invoiceUpdateDAO;
import com.felixstudio.automationcontrol.dto.invoice.InvoiceQueryDTO;
import com.felixstudio.automationcontrol.entity.invoice.InvoiceOperationHistory;
import com.felixstudio.automationcontrol.service.invoice.InvoiceOperationHistoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceOperationHistoryController {
    private final InvoiceOperationHistoryService invoiceOperationHistoryService;


    public InvoiceOperationHistoryController(InvoiceOperationHistoryService invoiceOperationHistoryService) {
        this.invoiceOperationHistoryService = invoiceOperationHistoryService;
    }

    /**
     * 通过批量传入发票历史数据来达到批量插入到数据库的效果
     *
     * @param histories
     * @return {@link ApiResponse }<{@link ? }>
     */
    @PostMapping("/pub/batchInster")
    public ApiResponse<?> batchInster(@RequestBody List<InvoiceOperationHistory> histories){
        int res = invoiceOperationHistoryService.batchInstallInvoiceOperationHistories(histories).toArray().length;
        return ApiResponse.success(res);
    }

    /**
     *
     *
     * @param history
     * @return {@link ApiResponse }<{@link ? }>
     */
    @PostMapping("/pub/add")
    public ApiResponse<?> add(@RequestBody InvoiceOperationHistory history) {
        boolean res = invoiceOperationHistoryService.save(history);
        return ApiResponse.success(res);
    }

    /**
     *
     *
     * @param queryDTO
     * @return {@link ApiResponse }<{@link ? }>
     */
    @PostMapping("/query")
    public ApiResponse<?> query(@RequestBody InvoiceQueryDTO queryDTO) {
        return ApiResponse.success(invoiceOperationHistoryService.queryByDTO(queryDTO));
    }

    /**
     * 根据平台和店铺来判断是否还存在未完成的发票操作记录
     *
     * @param queryDTO
     * @return {@link ApiResponse }<{@link ? }>
     */
    @PostMapping("/pub/findUnfinished")
    public ApiResponse<?> findUnfinished(@RequestBody InvoiceQueryDTO queryDTO){
        return ApiResponse.success(invoiceOperationHistoryService.findUnfinishedInvoiceOperationHistoryByPlatformAndShopName(queryDTO));
    }
    /**
     * 根据步骤获取下一个步骤的发票操作记录
     *
     * @param queryDTO
     * @return {@link ApiResponse }<{@link ? }>
     */
    @PostMapping("/pub/getNextByStep")
    public ApiResponse<?> getNextByStep(@RequestBody InvoiceQueryDTO queryDTO){
        return ApiResponse.success(invoiceOperationHistoryService.getNextByStep(queryDTO));
    }

    @PostMapping("/pub/update" )
    public ApiResponse<?> update(@RequestBody InvoiceOperationHistory history) {
        boolean res = invoiceOperationHistoryService.updateById(history);
        return ApiResponse.success(res);
    }
    @PostMapping("/pub/updateByStep")
    public ApiResponse<?> updateByStep(@RequestBody invoiceUpdateDAO updateDAO){
        invoiceOperationHistoryService.updateByStep(updateDAO);
        return ApiResponse.success("success");
    }
    @GetMapping("/getPlatforms")
    public ApiResponse<?> getPlatforms() {
        List<String> platforms = invoiceOperationHistoryService.getPlatforms();
        return ApiResponse.success(platforms);
    }
    @GetMapping("/getShops/{platform}" )
    public ApiResponse<?> getShops(@PathVariable String platform) {
        List<String> shops = invoiceOperationHistoryService.getShops(platform);
        return ApiResponse.success(shops);
    }
}
