package com.felixstudio.automationcontrol.mapper.presale;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.felixstudio.automationcontrol.dto.presale.PresaleMainDTO;
import com.felixstudio.automationcontrol.dto.presale.PresaleMainQueryDTO;
import com.felixstudio.automationcontrol.entity.presale.PresaleMain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PresaleMainMapper extends BaseMapper<PresaleMain> {
    Page<PresaleMainDTO> queryPresaleMainDTOs(IPage<PresaleMainDTO> page, @Param("presaleMainQuery") PresaleMainQueryDTO presaleMain);
}
