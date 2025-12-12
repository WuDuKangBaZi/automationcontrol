package com.felixstudio.automationcontrol.dto.presale;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.felixstudio.automationcontrol.config.mybatis.JsonObjectTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PresaleMainTaskInfoDTO {
    private String shopName;
    private String goodsId;
    private Integer taskStatus;
    @TableField(typeHandler = JsonObjectTypeHandler.class)
    private JSONObject resultData;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;
    @TableField(typeHandler = JsonObjectTypeHandler.class)
    private JsonNode filePaths;
}
