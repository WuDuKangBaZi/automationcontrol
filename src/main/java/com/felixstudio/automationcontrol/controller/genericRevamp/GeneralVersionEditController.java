package com.felixstudio.automationcontrol.controller.genericRevamp;

import com.alibaba.fastjson2.JSONObject;
import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.dto.generic.GenericQueryDTO;
import com.felixstudio.automationcontrol.entity.genericRevamp.GeneralVersionEdit;
import com.felixstudio.automationcontrol.service.genericRevamp.GeneralVersionEditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/generic")
public class GeneralVersionEditController {
    private final  GeneralVersionEditService generalVersionEditService ;

    public GeneralVersionEditController(GeneralVersionEditService generalVersionEditService) {
        this.generalVersionEditService = generalVersionEditService;
    }

    @PostMapping("/add")
    public ApiResponse<?> addGeneric(@RequestBody List<GeneralVersionEdit> generalVersionEdits){
//        return ApiResponse.success(generalVersionEditService.saveBatch(generalVersionEdits));
        return ApiResponse.success(generalVersionEditService.saveBatchGenericsAndCreateTasks(generalVersionEdits));
    }
    @PostMapping("/query")
    public ApiResponse<?> queryGeneric(@RequestBody GenericQueryDTO genericQueryDTO){
        log.info("Received generic query request");
        return ApiResponse.success(generalVersionEditService.queryByDto(genericQueryDTO));
    }
    @PostMapping("/saveOperationRemark")
    public ApiResponse<?> saveOperationRemark(@RequestBody GeneralVersionEdit generalVersionEdit){
        log.info("Received request to save operation remark");
        return ApiResponse.success(generalVersionEditService.updateOperationRemark(generalVersionEdit));
    }
    @PostMapping("/delete")
    public ApiResponse<?> deleteGeneric(@RequestBody JSONObject body){
        //ToDo 删除记录
        return ApiResponse.success(generalVersionEditService.deleteGeneric(body.getLong("id")));
    }
}
