package com.felixstudio.automationcontrol.mapper.changeCode;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.felixstudio.automationcontrol.dto.changeCode.ChangeCodeDTO;
import com.felixstudio.automationcontrol.dto.changeCode.ChangeCodeQueryDTO;
import com.felixstudio.automationcontrol.entity.changeCode.ChangeCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChangeCodeMapper extends BaseMapper<ChangeCode> {
    IPage<ChangeCodeDTO> queryByDTO(IPage<ChangeCodeDTO> page, @Param("changeCodeQueryDTO")ChangeCodeQueryDTO changeCodeQueryDTO);
}
