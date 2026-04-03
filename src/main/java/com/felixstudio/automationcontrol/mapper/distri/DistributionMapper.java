package com.felixstudio.automationcontrol.mapper.distri;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.felixstudio.automationcontrol.dao.distri.DistributionDAO;
import com.felixstudio.automationcontrol.dto.distri.DistributionQueryDTO;
import com.felixstudio.automationcontrol.entity.distri.Distribution;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DistributionMapper extends BaseMapper<Distribution> {
    IPage<DistributionDAO> queryByDto(Page<Distribution> page, DistributionQueryDTO queryDTO);
}
