package com.felixstudio.automationcontrol.service.presale.erp.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.felixstudio.automationcontrol.entity.presale.erp.PresaleErpCode;
import com.felixstudio.automationcontrol.entity.task.TaskJob;
import com.felixstudio.automationcontrol.mapper.presale.erp.PresaleErpCodeMapper;
import com.felixstudio.automationcontrol.service.presale.erp.PresaleErpCodeService;
import com.felixstudio.automationcontrol.service.shop.ShopInfoService;
import com.felixstudio.automationcontrol.service.task.TaskJobService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PresaleErpCodeServiceImpl extends ServiceImpl<PresaleErpCodeMapper, PresaleErpCode> implements PresaleErpCodeService {

    private final TaskJobService taskJobService;
    private final ShopInfoService shopInfoService;

    public PresaleErpCodeServiceImpl(TaskJobService taskJobService, ShopInfoService shopInfoService) {
        this.taskJobService = taskJobService;
        this.shopInfoService = shopInfoService;
    }
    @Transactional
    @Override
    public Integer batchInsertPresaleErpCodeAndCreateTaskJob(TaskJob taskJob, JSONArray taskResult) {
        List<String> allCodes = new ArrayList<>();
        List<PresaleErpCode> erpCodeList = new ArrayList<>();
        for (int i = 0; i < taskResult.size(); i++) {
            JSONObject item = taskResult.getJSONObject(i);
            String erpCode = item.getString("erpCode");
            String erpName = item.getString("erpName");
            allCodes.add(erpCode);
            // 插入到presale_erp_code表
            PresaleErpCode presaleErpCode = new PresaleErpCode();
            presaleErpCode.setPresaleId(taskJob.getRefId());
            presaleErpCode.setErpCode(erpCode);
            presaleErpCode.setErpName(erpName);
            erpCodeList.add(presaleErpCode);
        }
        // 全部erpcode组合一个erp
        String allErpCodes = String.join(",", allCodes);
        PresaleErpCode allErpCodeEntry = new PresaleErpCode();
        allErpCodeEntry.setPresaleId(taskJob.getRefId());
        allErpCodeEntry.setErpCode(allErpCodes);
        allErpCodeEntry.setErpName("全部ERP编码");
        erpCodeList.add(allErpCodeEntry);
        this.baseMapper.insert(erpCodeList);
        // 全部erpCode来创建任务
        for (PresaleErpCode presaleErpCode : erpCodeList) {
            List<TaskJob> erpTaskJob = getTaskJob(presaleErpCode,allErpCodes);
            taskJobService.saveBatch(erpTaskJob);
        }
        return erpCodeList.size();
    }

    private List<TaskJob> getTaskJob( PresaleErpCode presaleErpCode,String allErpCodes) {
        // 此处需要独立为每个店铺都创建一个任务
        List<TaskJob> jobs = new ArrayList<>();
        shopInfoService.getShopsByType("预售").forEach(shop -> {
            TaskJob erpTaskJob = new TaskJob();
            erpTaskJob.setTaskType("预售.ERP数据处理");
            erpTaskJob.setRefType("erp");
            erpTaskJob.setRefId(presaleErpCode.getId());
            erpTaskJob.setShopId(shop.getId());
            JSONObject params = new JSONObject();
            params.put("erpCode", presaleErpCode.getErpCode());
            params.put("erpName", presaleErpCode.getErpName());
            params.put("presaleId", presaleErpCode.getPresaleId());
            params.put("allErpCodes", allErpCodes);
            erpTaskJob.setTaskParams(params);
            erpTaskJob.setTaskStatus(0);
            jobs.add(erpTaskJob);
        });
        return jobs;
    }
}
