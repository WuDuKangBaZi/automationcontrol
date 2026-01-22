package com.felixstudio.automationcontrol.mapper.util;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.felixstudio.automationcontrol.dto.export.presaleMain.PresaleDetailsExportDTO;
import com.felixstudio.automationcontrol.dto.export.presaleMain.PresaleExportQueryDTO;
import com.felixstudio.automationcontrol.dto.export.presaleMain.PresaleSummaryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PresaleExportMapper extends BaseMapper<PresaleDetailsExportDTO> {
    List<PresaleDetailsExportDTO> exportPresaleMainExcel(@Param("presaleExportQueryDTO") PresaleExportQueryDTO presaleExportQueryDTO);

    List<PresaleSummaryDTO> exportPresaleSummaryExcel(@Param("presaleExportQueryDTO")  PresaleExportQueryDTO presaleExportQueryDTO);
}
