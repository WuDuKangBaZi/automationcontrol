package com.felixstudio.automationcontrol.dto.generic;

import com.felixstudio.automationcontrol.dto.PagerDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GenericQueryDTO extends PagerDTO {
    private LocalDate configDate;
    private String fileName;
    private String productName;
}
