package com.felixstudio.automationcontrol.service.dingTalk;

import com.alibaba.fastjson2.JSONObject;
import com.felixstudio.automationcontrol.dingTalk.DingTalkMessageBuilder;
import com.felixstudio.automationcontrol.dingTalk.DingtalkUtil;
import com.felixstudio.automationcontrol.entity.dingtalkEntity.ChatGroupInfo;
import com.felixstudio.automationcontrol.mapper.dingTalk.ChatGroupInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DingTalkService {

    private final DingtalkUtil dingtalkUtil;
    private final DingTalkMessageBuilder messageBuilder;
    private final ChatGroupInfoMapper chatGroupInfoMapper;
    public DingTalkService(DingtalkUtil dingtalkUtil, DingTalkMessageBuilder messageBuilder, ChatGroupInfoMapper chatGroupInfoMapper) {
        this.dingtalkUtil = dingtalkUtil;
        this.messageBuilder = messageBuilder;
        this.chatGroupInfoMapper = chatGroupInfoMapper;
    }

    public JSONObject sendMsg(String message, String groupShort){
        if(groupShort == null || groupShort.isBlank()){
            return null;
        }
        List<ChatGroupInfo> list = chatGroupInfoMapper.selectByMap(Map.of("short_name", groupShort));
        Optional<ChatGroupInfo> opt = list.stream().findFirst();
        if(opt.isEmpty()){
            return null;
        }else{
            ChatGroupInfo chatGroupInfo = opt.get();
            return dingtalkUtil.sendMessage(
                    messageBuilder.sampleText(message,chatGroupInfo.getGroupId())
            );
        }
    }
}
