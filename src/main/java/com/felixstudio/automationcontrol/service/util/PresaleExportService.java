package com.felixstudio.automationcontrol.service.util;

import com.baomidou.mybatisplus.extension.service.IService;
import com.felixstudio.automationcontrol.dto.export.presaleMain.PresaleDetailsExportDTO;
import com.felixstudio.automationcontrol.dto.export.presaleMain.PresaleExportQueryDTO;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;


public interface PresaleExportService extends IService<PresaleDetailsExportDTO> {
    List<?> exportPresaleMainExcel(PresaleExportQueryDTO presaleExportQueryDTO);
}
