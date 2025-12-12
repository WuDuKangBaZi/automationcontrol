package com.felixstudio.automationcontrol.controller.changeCode;

import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.dto.changeCode.ChangeCodeQueryDTO;
import com.felixstudio.automationcontrol.entity.changeCode.ChangeCode;
import com.felixstudio.automationcontrol.service.changeCode.ChangeCodeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cc")
public class ChangeCodeController {
    private final ChangeCodeService changeCodeService;


    public ChangeCodeController(ChangeCodeService changeCodeService) {
        this.changeCodeService = changeCodeService;
    }

    @PostMapping("/batch")
    public ApiResponse<?> batchInsertChangeCode(@RequestBody List<ChangeCode> changeCodeList) {
        this.changeCodeService.batchInsertChangeCode(changeCodeList);
        return ApiResponse.success("批量插入成功");
    }
    @PostMapping("/query")
    public ApiResponse<?> queryChangeCode(@RequestBody ChangeCodeQueryDTO changeCodeQueryDTO){
        return ApiResponse.success(this.changeCodeService.queryByDTO(changeCodeQueryDTO));
    }
}
