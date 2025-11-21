package com.felixstudio.automationcontrol.service.dingTalk;

import com.felixstudio.automationcontrol.entity.dingtalkEntity.ChatGroupInfo;
import com.felixstudio.automationcontrol.mapper.dingTalk.ChatGroupInfoMapper;
import org.springframework.stereotype.Service;

@Service
public class ChatGroupInfoService {
    private final ChatGroupInfoMapper chatGroupInfoMapper;

    public ChatGroupInfoService(ChatGroupInfoMapper chatGroupInfoMapper) {
        this.chatGroupInfoMapper = chatGroupInfoMapper;
    }

    public ChatGroupInfo getGroupByShortName(String shortName) {
        return chatGroupInfoMapper.selectByMap(
                java.util.Map.of("short_name", shortName)
        ).stream().findFirst().orElse(null);
    }
}
