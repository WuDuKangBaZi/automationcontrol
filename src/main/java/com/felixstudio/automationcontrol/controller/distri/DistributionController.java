package com.felixstudio.automationcontrol.controller.distri;

import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.dto.distri.DistributionQueryDTO;
import com.felixstudio.automationcontrol.entity.distri.Distribution;
import com.felixstudio.automationcontrol.service.distri.DistributionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/distri")
public class DistributionController {
    private final DistributionService distributionService;

    public DistributionController(DistributionService distributionService) {
        this.distributionService = distributionService;
    }

    @PostMapping("/query")
    public ApiResponse<?> query(@RequestBody DistributionQueryDTO dto){
        return ApiResponse.success(distributionService.queryByDto(dto));
    }
    @PostMapping("/batch_insert")
    public ApiResponse<?> batchInsert(@RequestBody List<Distribution> distributions){
        return ApiResponse.success(distributionService.batInsert(distributions));
    }
}
