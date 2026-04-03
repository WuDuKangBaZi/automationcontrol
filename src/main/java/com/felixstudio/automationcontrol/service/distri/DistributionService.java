package com.felixstudio.automationcontrol.service.distri;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.felixstudio.automationcontrol.dao.distri.DistributionDAO;
import com.felixstudio.automationcontrol.dto.distri.DistributionQueryDTO;
import com.felixstudio.automationcontrol.entity.distri.Distribution;

import java.util.List;

public interface DistributionService extends IService<Distribution> {
    IPage<DistributionDAO> queryByDto(DistributionQueryDTO queryDTO);

    int batInsert(List<Distribution> distributions);
}
