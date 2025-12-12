package com.felixstudio.automationcontrol.dto.changeCode;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ChangeCodeDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private LocalDate configDate;
    private String oldCode;
    private String goodsName;
    private String newCode;
    private String remark;
    private String notifiedPerson;
    private String taskStatus;
    private String errorMessage;
    private Integer resultCount;
    private Integer successCount;
    private Integer failureCount;
}
