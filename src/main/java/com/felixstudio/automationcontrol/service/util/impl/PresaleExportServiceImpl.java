package com.felixstudio.automationcontrol.service.util.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.felixstudio.automationcontrol.dto.export.presaleMain.PresaleDetailsExportDTO;
import com.felixstudio.automationcontrol.dto.export.presaleMain.PresaleExportQueryDTO;
import com.felixstudio.automationcontrol.dto.export.presaleMain.PresaleSummaryDTO;
import com.felixstudio.automationcontrol.mapper.util.PresaleExportMapper;
import com.felixstudio.automationcontrol.service.util.PresaleExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PresaleExportServiceImpl extends ServiceImpl<PresaleExportMapper, PresaleDetailsExportDTO> implements PresaleExportService {
    private final PresaleExportMapper presaleExportMapper;
    @Value("${server.prefix}")
    private String serverPrefix;
    public PresaleExportServiceImpl(PresaleExportMapper presaleExportMapper) {
        this.presaleExportMapper = presaleExportMapper;
    }

    @Override
    public List<?> exportPresaleMainExcel(PresaleExportQueryDTO presaleExportQueryDTO) {
        if(Objects.equals(presaleExportQueryDTO.getExportType(), "明细")){
            // 查询明细数据并导出
            return presaleExportMapper.exportPresaleMainExcel(presaleExportQueryDTO,serverPrefix);
        } else if (Objects.equals(presaleExportQueryDTO.getExportType(),"汇总")) {
            return presaleExportMapper.exportPresaleSummaryExcel(presaleExportQueryDTO);
        }else{
            throw new IllegalArgumentException("未知的导出类型: " + presaleExportQueryDTO.getExportType());
        }

    }
}
