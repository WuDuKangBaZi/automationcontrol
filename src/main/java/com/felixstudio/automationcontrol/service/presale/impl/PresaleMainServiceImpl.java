package com.felixstudio.automationcontrol.service.presale.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.felixstudio.automationcontrol.dto.PagerDTO;
import com.felixstudio.automationcontrol.dto.presale.PresaleMainDTO;
import com.felixstudio.automationcontrol.dto.presale.PresaleMainQueryDTO;
import com.felixstudio.automationcontrol.dto.presale.PresaleMainTaskInfoDTO;
import com.felixstudio.automationcontrol.entity.presale.PresaleMain;
import com.felixstudio.automationcontrol.entity.task.TaskJob;
import com.felixstudio.automationcontrol.mapper.presale.PresaleMainMapper;
import com.felixstudio.automationcontrol.service.presale.PresaleMainService;
import com.felixstudio.automationcontrol.service.presale.erp.PresaleErpCodeService;
import com.felixstudio.automationcontrol.service.task.TaskJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class PresaleMainServiceImpl extends ServiceImpl<PresaleMainMapper, PresaleMain> implements PresaleMainService {


    private final TaskJobService taskJobService;
    private final PresaleErpCodeService presaleErpCodeService;

    public PresaleMainServiceImpl(TaskJobService taskJobService, @Lazy PresaleErpCodeService presaleErpCodeService) {
        this.taskJobService = taskJobService;
        this.presaleErpCodeService = presaleErpCodeService;
    }

    @Override
    @Transactional
    public Integer batchInsertPresaleMainAndCreateTaskJob(List<PresaleMain> presaleMains) {
        this.saveBatch(presaleMains);
        List<TaskJob> jobList = new ArrayList<>();
        for (PresaleMain presaleMain : presaleMains) {
            JSONObject params = new JSONObject();
            params.put("id", presaleMain.getId());
            params.put("goodsCode", presaleMain.getGoodsCode());
            params.put("goodsName", presaleMain.getGoodsName());
            TaskJob taskJob = new TaskJob();
            taskJob.setTaskType("预售.ERP查询");
            taskJob.setRefType("presale_main");
            taskJob.setRefId(presaleMain.getId());
            taskJob.setTaskStatus(0);
            taskJob.setTaskParams(params);
            jobList.add(taskJob);
            log.info("创建预售ERP查询任务: {}", params.toJSONString());
        }
        taskJobService.batchInsertTaskJobs(jobList);
        return jobList.size();
    }

    @Override
    public JSONObject getErpCodes() {
        List<TaskJob> jobList = taskJobService.getBaseMapper().selectList(
                new LambdaQueryWrapper<TaskJob>()
                        .eq(TaskJob::getTaskType, "预售.ERP查询")
                        .in(TaskJob::getTaskStatus, Arrays.asList(0, 1))
                        .last("limit 1")
        );
        log.info(jobList.toString());
        if (!jobList.isEmpty()) {
            TaskJob taskJob = jobList.get(0);
            taskJob.setTaskStatus(1);
            taskJobService.updateById(taskJob);
            JSONObject resultJson = new JSONObject();
            resultJson.put("taskId", taskJob.getId());
            resultJson.put("taskParams", taskJob.getTaskParams());
            return resultJson;
        }
        return null;
    }

    @Transactional
    @Override
    public Integer setErpResult(Long taskId, int status, JSONArray taskResult) {
        // 根据taskId获取到任务的关联数据
        TaskJob taskJob = taskJobService.getById(taskId);
        if (taskJob == null) {
            log.error("任务ID {} 未找到对应的任务", taskId);
            throw new RuntimeException("任务未找到");
        }
        // 更新任务状态
        taskJob.setTaskStatus(status);
        taskJobService.updateById(taskJob);
        return presaleErpCodeService.batchInsertPresaleErpCodeAndCreateTaskJob(taskJob, taskResult);
    }

    @Override
    public Page<PresaleMainDTO> search(PresaleMainQueryDTO queryDTO) {
        log.info(String.valueOf(queryDTO.getConfigDate()));
        log.info("pageNo={}, pageSize={}", queryDTO.getPageNo(), queryDTO.getPageSize());
        if (Objects.equals(queryDTO.getHandlingMethod(), "全部")) {
            queryDTO.setHandlingMethod(null);
        }
        Page<PresaleMainDTO> page = new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize());
        return this.getBaseMapper().queryPresaleMainDTOs(page, queryDTO);
    }

    @Override
    public Page<PresaleMainTaskInfoDTO> getTaskInfoByPresaleId(String presaleId, PagerDTO pagerDTO) {
        Page<PresaleMainTaskInfoDTO> page = new Page<>(pagerDTO.getPageNo(), pagerDTO.getPageSize());
        return this.getBaseMapper().queryPresaleMainTaskInfoDTOs(page, Long.parseLong(presaleId));
    }

    @Override
    public Object checkPresaleMain(List<PresaleMain> presaleMains) {

        return this.getBaseMapper().checkPresaleMain(presaleMains);
    }


}
