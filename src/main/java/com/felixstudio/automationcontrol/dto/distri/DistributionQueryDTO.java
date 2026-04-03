package com.felixstudio.automationcontrol.dto.distri;

import com.felixstudio.automationcontrol.dto.PagerDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DistributionQueryDTO extends PagerDTO {
    private LocalDate configDate;
    private String keyword;
    private LocalDate initTime;
}
