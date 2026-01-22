package com.felixstudio.automationcontrol.mapper.genericRevamp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.felixstudio.automationcontrol.dao.generic.GenericDAO;
import com.felixstudio.automationcontrol.dto.generic.GenericQueryDTO;
import com.felixstudio.automationcontrol.dto.presale.PresaleMainDTO;
import com.felixstudio.automationcontrol.entity.genericRevamp.GeneralVersionEdit;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GeneralVersionEditMapper extends BaseMapper<GeneralVersionEdit> {
    int updateOpertaionRemarkById(GeneralVersionEdit generalVersionEdit);

    IPage<GenericDAO> queryByDto(IPage<GenericDAO> page,GenericQueryDTO genericQueryDTO);
}
