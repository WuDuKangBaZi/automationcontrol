package com.felixstudio.automationcontrol.service.presale.erp;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import com.felixstudio.automationcontrol.dto.presale.erp.ErpCodesDTO;
import com.felixstudio.automationcontrol.entity.presale.erp.PresaleErpCode;
import com.felixstudio.automationcontrol.entity.task.TaskJob;

import java.util.List;

public interface PresaleErpCodeService extends IService<PresaleErpCode> {
    Integer batchInsertPresaleErpCodeAndCreateTaskJob(TaskJob taskJob, JSONArray taskResult);

    List<ErpCodesDTO> queryByPresaleId(String id);
}
