package com.felixstudio.automationcontrol.service.changeCode.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.felixstudio.automationcontrol.dto.changeCode.ChangeCodeDTO;
import com.felixstudio.automationcontrol.dto.changeCode.ChangeCodeDetailsDTO;
import com.felixstudio.automationcontrol.dto.changeCode.ChangeCodeQueryDTO;
import com.felixstudio.automationcontrol.entity.changeCode.ChangeCode;
import com.felixstudio.automationcontrol.entity.task.TaskJob;
import com.felixstudio.automationcontrol.mapper.changeCode.ChangeCodeMapper;
import com.felixstudio.automationcontrol.service.changeCode.ChangeCodeService;
import com.felixstudio.automationcontrol.service.task.TaskJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ChangeCodeServiceImpl extends ServiceImpl<ChangeCodeMapper, ChangeCode> implements ChangeCodeService {
    private final TaskJobService taskJobService;

    public ChangeCodeServiceImpl(TaskJobService taskJobService) {
        this.taskJobService = taskJobService;
    }
    @Transactional
    @Override
    public void batchInsertChangeCode(List<ChangeCode> changeCodeList) {
        this.saveBatch(changeCodeList);
        // 批量生成Task
        List<TaskJob> taskJobList = new ArrayList<>();
        for (ChangeCode changeCode : changeCodeList) {
            TaskJob taskJob = new TaskJob();
            taskJob.setRefId(changeCode.getId());
            taskJob.setTaskType("改编码");
            taskJob.setRefType("change_code");
            JSONObject taskParams = new JSONObject();
            taskParams.put("oldCode", changeCode.getOldCode());
            taskParams.put("newCode", changeCode.getNewCode());
            taskJob.setTaskParams(taskParams);
            taskJobList.add(taskJob);
        }
        taskJobService.batchInsertTaskJobs(taskJobList);
    }

    @Override
    public IPage<ChangeCodeDTO> queryByDTO(ChangeCodeQueryDTO changeCodeQueryDTO) {
        IPage<ChangeCodeDTO> pager = new Page<>(changeCodeQueryDTO.getPageNo(), changeCodeQueryDTO.getPageSize());

        return this.baseMapper.queryByDTO(pager,changeCodeQueryDTO);
    }

    @Override
    public List<?> queryDetailsById(Long id) {
        List<ChangeCodeDetailsDTO> details = this.baseMapper.queryDetailsById(id);
        log.info(details.toString());
        return details;
    }
}
