package com.felixstudio.automationcontrol.dto.dingTalk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private String message;
    private String groupShort;
    private String fileType;
    private List<String> atMobiles;

}
