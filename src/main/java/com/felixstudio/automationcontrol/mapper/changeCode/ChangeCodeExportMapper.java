package com.felixstudio.automationcontrol.mapper.changeCode;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.felixstudio.automationcontrol.dto.changeCode.ChangeCodeQueryDTO;
import com.felixstudio.automationcontrol.dto.export.changeCode.ChangeCodeExportDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChangeCodeExportMapper extends BaseMapper<ChangeCodeExportDTO> {
    List<ChangeCodeExportDTO> exportChangeCodeExcel(ChangeCodeQueryDTO changeCodeQueryDTO);
}
