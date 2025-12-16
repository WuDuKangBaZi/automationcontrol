package com.felixstudio.automationcontrol.dto.changeCode;

import com.felixstudio.automationcontrol.dto.PagerDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChangeCodeQueryDTO extends PagerDTO {
    private LocalDate date;
    private String oldCode;
    private String newCode;
}
