package com.felixstudio.automationcontrol.dto.export.presaleMain;

import com.felixstudio.automationcontrol.dto.presale.PresaleMainQueryDTO;
import lombok.Data;

@Data
public class PresaleExportQueryDTO {
    private PresaleMainQueryDTO presaleMainQueryDTO;
    private String exportType;
    private String exportOptions;
}
