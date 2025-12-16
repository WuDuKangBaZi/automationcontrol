package com.felixstudio.automationcontrol.dto.presale;

import com.felixstudio.automationcontrol.dto.PagerDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Validated
public class PresaleMainQueryDTO extends PagerDTO {
    @NotNull(message = "搜索日期不能为空")
    private LocalDate configDate;
    private String goodsCode;
    private String handlingMethod;
}
