package com.felixstudio.automationcontrol.controller.util;

import com.alibaba.excel.EasyExcel;
import com.felixstudio.automationcontrol.dto.changeCode.ChangeCodeQueryDTO;
import com.felixstudio.automationcontrol.dto.export.presaleMain.PresaleExportQueryDTO;
import com.felixstudio.automationcontrol.service.util.ChangeCodeExportService;
import com.felixstudio.automationcontrol.service.util.PresaleExportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/pub/export")
public class ExportExcelController {

    private final PresaleExportService presaleExportService;
    private final ChangeCodeExportService changeCodeExportService;
    public ExportExcelController(PresaleExportService presaleExportService, ChangeCodeExportService changeCodeExportService) {
        this.presaleExportService = presaleExportService;
        this.changeCodeExportService = changeCodeExportService;
    }

    @PostMapping("/presaleMain")
    public void ExportPresaleMainExcel(@RequestBody PresaleExportQueryDTO presaleExportQueryDTO, HttpServletResponse response) {
        log.info(presaleExportQueryDTO.toString());
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        List<?> downloadExcels = presaleExportService.exportPresaleMainExcel(presaleExportQueryDTO);
        String fileType = "明细";
        if (presaleExportQueryDTO.getExportType().equals("汇总")) {
            fileType = "汇总";
        }
        String fileName = URLEncoder.encode("预售主数据导出" + fileType, java.nio.charset.StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        try {
            EasyExcel.write(response.getOutputStream(), downloadExcels.get(0).getClass()).sheet("预售主数据导出" + fileType).doWrite(downloadExcels);
        } catch (Exception e) {
            log.error(e.getMessage());
        }


    }

    @PostMapping("/changeCode")
    public void ExportChangeCodeExcel(@RequestBody ChangeCodeQueryDTO changeCodeQueryDTO, HttpServletResponse response) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        List<?> downloadExcels = changeCodeExportService.exportChangeCodeExcel(changeCodeQueryDTO);
        String fileName = URLEncoder.encode("改编码数据导出", java.nio.charset.StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + changeCodeQueryDTO.getDate().toString() + ".xlsx");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        try {
            EasyExcel.write(response.getOutputStream(), downloadExcels.get(0).getClass()).sheet("改编码数据导出").doWrite(downloadExcels);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }
}
