package com.felixstudio.automationcontrol.dto.dingTalk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private String message;
    private String groupShort;
    private String fileType;

}
