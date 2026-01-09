package com.felixstudio.automationcontrol.service.presale;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.felixstudio.automationcontrol.dto.PagerDTO;
import com.felixstudio.automationcontrol.dto.presale.PresaleMainDTO;
import com.felixstudio.automationcontrol.dto.presale.PresaleMainQueryDTO;
import com.felixstudio.automationcontrol.dto.presale.PresaleMainTaskInfoDTO;
import com.felixstudio.automationcontrol.entity.presale.PresaleMain;

import java.util.List;

public interface PresaleMainService extends IService<PresaleMain> {
    Integer batchInsertPresaleMainAndCreateTaskJob(List<PresaleMain> presaleMains);

    JSONObject getErpCodes();

    Object setErpResult(Long taskId, int status, JSONArray taskResult);

    Page<PresaleMainDTO> search(PresaleMainQueryDTO queryDTO);

    Page<PresaleMainTaskInfoDTO> getTaskInfoByPresaleId(String presaleId, PagerDTO pagerDTO);

    Object checkPresaleMain(List<PresaleMain> presaleMain);

    boolean deletePresaleMainByPresaleId(String presaleId);
}
