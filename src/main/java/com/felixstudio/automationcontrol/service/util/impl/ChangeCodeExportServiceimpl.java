package com.felixstudio.automationcontrol.service.util.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.felixstudio.automationcontrol.dto.changeCode.ChangeCodeQueryDTO;
import com.felixstudio.automationcontrol.dto.export.changeCode.ChangeCodeExportDTO;
import com.felixstudio.automationcontrol.dto.export.presaleMain.PresaleExportQueryDTO;
import com.felixstudio.automationcontrol.mapper.changeCode.ChangeCodeExportMapper;
import com.felixstudio.automationcontrol.service.changeCode.ChangeCodeService;
import com.felixstudio.automationcontrol.service.util.ChangeCodeExportService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChangeCodeExportServiceimpl extends ServiceImpl<ChangeCodeExportMapper,ChangeCodeExportDTO> implements ChangeCodeExportService {
    private final ChangeCodeExportMapper changeCodeExportMapper;

    public ChangeCodeExportServiceimpl(ChangeCodeExportMapper changeCodeExportMapper) {
        this.changeCodeExportMapper = changeCodeExportMapper;
    }

    @Override
    public List<?> exportChangeCodeExcel(ChangeCodeQueryDTO changeCodeQueryDTO) {
        return changeCodeExportMapper.exportChangeCodeExcel(changeCodeQueryDTO);
    }
}
