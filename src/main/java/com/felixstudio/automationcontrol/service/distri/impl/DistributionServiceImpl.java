package com.felixstudio.automationcontrol.service.distri.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.felixstudio.automationcontrol.dao.distri.DistributionDAO;
import com.felixstudio.automationcontrol.dto.distri.DistributionQueryDTO;
import com.felixstudio.automationcontrol.entity.distri.Distribution;
import com.felixstudio.automationcontrol.mapper.distri.DistributionMapper;
import com.felixstudio.automationcontrol.service.distri.DistributionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistributionServiceImpl extends ServiceImpl<DistributionMapper, Distribution> implements DistributionService {
    @Override
    public IPage<DistributionDAO> queryByDto(DistributionQueryDTO queryDTO) {
        long current = queryDTO.getPageNo() != null ? queryDTO.getPageNo() : 1L;
        long size = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10L;
        Page<Distribution> page = new Page<>(current, size);
        return this.baseMapper.queryByDto(page, queryDTO);
    }

    @Override
    public int batInsert(List<Distribution> distributions) {
        return this.baseMapper.insert(distributions).size();
    }
}
