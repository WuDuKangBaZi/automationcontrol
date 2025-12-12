package com.felixstudio.automationcontrol.service.util;

import com.baomidou.mybatisplus.extension.service.IService;
import com.felixstudio.automationcontrol.dto.changeCode.ChangeCodeQueryDTO;
import com.felixstudio.automationcontrol.dto.export.changeCode.ChangeCodeExportDTO;

import java.util.List;

public interface ChangeCodeExportService extends IService<ChangeCodeExportDTO> {
    List<?> exportChangeCodeExcel(ChangeCodeQueryDTO ChangeCodeQueryDTO);
}
