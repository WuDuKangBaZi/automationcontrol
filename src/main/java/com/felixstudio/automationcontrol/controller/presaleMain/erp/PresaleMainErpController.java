package com.felixstudio.automationcontrol.controller.presaleMain.erp;

import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.service.presale.erp.PresaleErpCodeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/presaleErp")
public class PresaleMainErpController {
    private final PresaleErpCodeService presaleErpCodeService;

    public PresaleMainErpController(PresaleErpCodeService presaleErpCodeService) {
        this.presaleErpCodeService = presaleErpCodeService;
    }

    @GetMapping("/m/{id}")
    public ApiResponse<?> queryByPresaleId(@PathVariable String id) {
        return ApiResponse.success(presaleErpCodeService.queryByPresaleId(id));
    }
}
