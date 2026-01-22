package com.felixstudio.automationcontrol.service.genericRevamp.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.felixstudio.automationcontrol.dao.generic.GenericDAO;
import com.felixstudio.automationcontrol.dto.generic.GenericQueryDTO;
import com.felixstudio.automationcontrol.entity.genericRevamp.GeneralVersionEdit;
import com.felixstudio.automationcontrol.entity.shop.ShopInfo;
import com.felixstudio.automationcontrol.entity.task.TaskJob;
import com.felixstudio.automationcontrol.mapper.genericRevamp.GeneralVersionEditMapper;
import com.felixstudio.automationcontrol.service.genericRevamp.GeneralVersionEditService;
import com.felixstudio.automationcontrol.service.shop.ShopInfoService;
import com.felixstudio.automationcontrol.service.task.TaskJobService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GeneralVersionEditServiceimpl extends ServiceImpl<GeneralVersionEditMapper, GeneralVersionEdit> implements GeneralVersionEditService {


    private final  GeneralVersionEditMapper generalVersionEditMapper;
    private final ShopInfoService shopInfoService;
    private final TaskJobService taskJobService;

    private final StringRedisTemplate stringRedisTemplate;

    public GeneralVersionEditServiceimpl(GeneralVersionEditMapper generalVersionEditMapper, ShopInfoService shopInfoService, TaskJobService taskJobService, StringRedisTemplate stringRedisTemplate) {
        this.generalVersionEditMapper = generalVersionEditMapper;
        this.shopInfoService = shopInfoService;
        this.taskJobService = taskJobService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * DTO查询
     *
     * @param genericQueryDTO 泛型查询 DTO
     * @return {@link List }<{@link GeneralVersionEdit }>
     */
    @Override
    public IPage<GenericDAO> queryByDto(GenericQueryDTO genericQueryDTO) {
        long current = genericQueryDTO.getPageNo() != null ? genericQueryDTO.getPageNo() : 1L;
        long size = genericQueryDTO.getPageSize() != null ? genericQueryDTO.getPageSize() : 10L;
        Page<GenericDAO> page = new Page<>(current, size);
        return this.generalVersionEditMapper.queryByDto(page,genericQueryDTO);
    }

    @SneakyThrows
    @Override
    public Object updateOperationRemark(GeneralVersionEdit generalVersionEdit) {
        return generalVersionEditMapper.updateOpertaionRemarkById(generalVersionEdit);

    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Object saveBatchGenericsAndCreateTasks(List<GeneralVersionEdit> generalVersionEdits) {
        this.saveBatch(generalVersionEdits);
        List<ShopInfo> shops = shopInfoService.getShopsByType("通版改新");
        List<TaskJob> jobs = new ArrayList<>(List.of());
        String monitorKey = "通版改新";
        stringRedisTemplate.opsForSet().add(monitorKey,shops.stream().map(ShopInfo::getShopName).toArray(String[]::new));
        stringRedisTemplate.expire(monitorKey, Duration.ofDays(7));
        // 获取所有编码，存储为一个param
//        List<String> allCodes = generalVersionEdits.stream().map(GeneralVersionEdit::getNewCode).toList();
        JSONObject allCodes = new JSONObject();
        generalVersionEdits.forEach( generic -> allCodes.put(generic.getNewCode(),generic.getNewPrice()));
        for(GeneralVersionEdit generic : generalVersionEdits) {
            for (ShopInfo shop : shops) {
                TaskJob job = new TaskJob();
                job.setRefId(generic.getId());
                job.setTaskType("通版改新");
                job.setRefType("generic");
                job.setShopId(shop.getId());
                job.setTaskStatus(0);
                JSONObject taskParam = new JSONObject();
                taskParam.put("fileName", generic.getFileName());
                taskParam.put("goodsName", generic.getGoodsName());
                taskParam.put("newCode", generic.getNewCode());
                taskParam.put("newPrice", generic.getNewPrice());
                taskParam.put("type", generic.getType());
                // 店铺级别的operationRemark可能会出现备注内容的异常
                taskParam.put("operationRemark",generic.getOperationRemark());
                taskParam.put("allCodes", allCodes);
                job.setTaskParams(taskParam);
                jobs.add(job);
            }
        }
        taskJobService.batchInsertTaskJobs(jobs);
        return true;
    }


    @Override
    public Object deleteGeneric(Long id) {
        taskJobService.remove(new LambdaQueryWrapper<TaskJob>().eq(TaskJob::getRefId,id));
        return this.removeById(id);
    }
}
