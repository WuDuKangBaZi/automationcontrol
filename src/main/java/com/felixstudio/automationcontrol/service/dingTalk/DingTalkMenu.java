package com.felixstudio.automationcontrol.service.dingTalk;

import com.felixstudio.automationcontrol.dingTalk.DingTalkMessageBuilder;
import com.felixstudio.automationcontrol.dingTalk.DingtalkUtil;
import com.felixstudio.automationcontrol.entity.dingtalkEntity.ChatGroupInfo;
import com.felixstudio.automationcontrol.mapper.dingTalk.ChatGroupInfoMapper;
import com.felixstudio.automationcontrol.service.verify.SmsShortCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DingTalkMenu {
    private final SmsShortCodeService smsShortCodeService;
    private final ChatGroupInfoMapper chatGroupInfoMapper;
    public void saveVerifyMenu(String shortCode,String verifyCode,String senderNick,String channelId){
        log.info("收到验证码: {} 来自用户: {}",verifyCode,senderNick);
        smsShortCodeService.submitSmS(shortCode,verifyCode,senderNick);
        DingtalkUtil dingtalkUtil = new DingtalkUtil();
        DingTalkMessageBuilder dingTalkMessageBuilder = new DingTalkMessageBuilder();
        dingtalkUtil.sendMessage(dingTalkMessageBuilder.sampleText("感谢您的配合，验证码已收到并提交给流程！",channelId));
    }

    public boolean saveGroupInfo(String openThreadId, String conversationTitle,String shortName,String channleId) {
        ChatGroupInfo info = new ChatGroupInfo();
        DingtalkUtil dingtalkUtil = new DingtalkUtil();
        DingTalkMessageBuilder dingTalkMessageBuilder = new DingTalkMessageBuilder();
        List<ChatGroupInfo> infoList =  chatGroupInfoMapper.selectByMap(Map.of("group_id", openThreadId));
        if(!infoList.isEmpty()){
            log.info("群聊已存在: 群聊ID={}, 群聊名称={}, 简称={}", openThreadId, conversationTitle,shortName);
            info = infoList.get(0);
            info.setGroupName(conversationTitle);
            info.setShortName(shortName);
            info.setPlatform("钉钉");
            chatGroupInfoMapper.updateById(info);
            dingtalkUtil.sendMessage(dingTalkMessageBuilder.sampleText("群聊信息已更新！在RPA中可通过指定接口调用消息通知",channleId));
            return true;
        }
        info.setGroupId(openThreadId);
        info.setGroupName(conversationTitle);
        info.setShortName(shortName);
        info.setPlatform("钉钉");
        chatGroupInfoMapper.insert(info);
        log.info("保存群聊信息: 群聊ID={}, 群聊名称={}, 简称={}", openThreadId, conversationTitle,shortName);
        dingtalkUtil.sendMessage(dingTalkMessageBuilder.sampleText("群聊已注册！在RPA中可通过指定接口调用消息通知",channleId));
        return true;
    }
}
