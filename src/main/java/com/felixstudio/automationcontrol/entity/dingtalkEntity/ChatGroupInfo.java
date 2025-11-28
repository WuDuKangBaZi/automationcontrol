package com.felixstudio.automationcontrol.entity.dingtalkEntity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("chat_group_info")
public class ChatGroupInfo {

    @TableId("id")
    private Long id;  // 自增主键

    @TableField("group_id")
    private String groupId;

    @TableField("short_name")
    private String shortName;  // 开发者使用的短名称

    @TableField("group_name")
    private String groupName;

    @TableField("platform")
    private String platform;

    @TableField("webhook_url")
    private String webhookUrl;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}

