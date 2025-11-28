package com.felixstudio.automationcontrol.service.presale;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.felixstudio.automationcontrol.entity.presale.PresaleMain;

import java.util.List;

public interface PresaleMainService extends IService<PresaleMain> {
    Integer batchInsertPresaleMainAndCreateTaskJob(List<PresaleMain> presaleMains);

    JSONObject getErpCodes();

    Object setErpResult(Long taskId, int status, JSONArray taskResult);
}
