package com.felixstudio.automationcontrol.controller.changeCode;

import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.dto.changeCode.ChangeCodeQueryDTO;
import com.felixstudio.automationcontrol.entity.changeCode.ChangeCode;
import com.felixstudio.automationcontrol.service.changeCode.ChangeCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cc")
@Tag(name = "改编码", description = "改编码相关接口")
public class ChangeCodeController {
    private final ChangeCodeService changeCodeService;


    public ChangeCodeController(ChangeCodeService changeCodeService) {
        this.changeCodeService = changeCodeService;
    }

    @Operation(
            summary = "新增改编码数据",
            description = "批量插入改编码数据"
    )
    @PostMapping("/batch")
    public ApiResponse<?> batchInsertChangeCode(@RequestBody List<ChangeCode> changeCodeList) {
        this.changeCodeService.batchInsertChangeCode(changeCodeList);
        return ApiResponse.success("批量插入成功");
    }

    @Operation(
            summary = "查询改编码数据",
            description = "根据查询条件查询改编码数据"
    )
    @PostMapping("/query")
    public ApiResponse<?> queryChangeCode(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "查询条件DTO,包含日期、旧编码、新编码及分页信息",
            required = true,
            content = @Content(schema = @Schema(implementation = ChangeCodeQueryDTO.class))
    ) @RequestBody ChangeCodeQueryDTO changeCodeQueryDTO) {
        return ApiResponse.success(this.changeCodeService.queryByDTO(changeCodeQueryDTO));
    }
    @GetMapping("/queryDetails/{id}")
    public ApiResponse<?> queryChangeCodeDetails(@PathVariable Long id) {
        return ApiResponse.success(this.changeCodeService.queryDetailsById(id));
    }

}
