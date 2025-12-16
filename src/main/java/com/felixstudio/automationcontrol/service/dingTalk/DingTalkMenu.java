package com.felixstudio.automationcontrol.service.dingTalk;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.felixstudio.automationcontrol.dingTalk.DingTalkMessageBuilder;
import com.felixstudio.automationcontrol.dingTalk.DingtalkUtil;
import com.felixstudio.automationcontrol.dto.task.TaskProgress;
import com.felixstudio.automationcontrol.entity.dingtalkEntity.ChatGroupInfo;
import com.felixstudio.automationcontrol.mapper.dingTalk.ChatGroupInfoMapper;
import com.felixstudio.automationcontrol.mapper.task.TaskJobMapper;
import com.felixstudio.automationcontrol.service.verify.SmsShortCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service

public class DingTalkMenu {
    private final SmsShortCodeService smsShortCodeService;
    private final ChatGroupInfoMapper chatGroupInfoMapper;
    private final DingTalkMessageBuilder dingTalkMessageBuilder;
    private final DingtalkUtil dingtalkUtil ;
    private final TaskJobMapper taskJobMapper;

    public DingTalkMenu(SmsShortCodeService smsShortCodeService, ChatGroupInfoMapper chatGroupInfoMapper, DingTalkMessageBuilder dingTalkMessageBuilder, DingtalkUtil dingtalkUtil, TaskJobMapper taskJobMapper) {
        this.smsShortCodeService = smsShortCodeService;
        this.chatGroupInfoMapper = chatGroupInfoMapper;
        this.dingTalkMessageBuilder = dingTalkMessageBuilder;
        this.dingtalkUtil = dingtalkUtil;
        this.taskJobMapper = taskJobMapper;
    }

    public void saveVerifyMenu(String shortCode,String verifyCode,String senderNick,String channelId){
        log.info("收到验证码: {} 来自用户: {}",verifyCode,senderNick);
        boolean submit = smsShortCodeService.submitSmS(shortCode.trim(),verifyCode,senderNick);
        if(!submit){
            throw  new RuntimeException("提交验证码失败，可能验证码已过期或无效");
        }
        dingtalkUtil.sendMessage(dingTalkMessageBuilder.sampleText("感谢您的配合，验证码已收到并提交给流程！",channelId));
    }

    public boolean saveGroupInfo(String openThreadId, String conversationTitle, String shortName, String channelId) {
        if (openThreadId == null || openThreadId.isBlank()) {
            log.warn("保存群聊信息失败：openThreadId 为空或无效");
            return false;
        }
        try {
            ChatGroupInfo info = chatGroupInfoMapper.selectOne(new LambdaQueryWrapper<ChatGroupInfo>().eq(ChatGroupInfo::getGroupId, openThreadId));
            if (info != null) {
                log.info("群聊已存在: 群聊ID={}, 群聊名称={}, 简称={}", openThreadId, conversationTitle, shortName);
                info.setGroupName(conversationTitle);
                info.setShortName(shortName);
                info.setPlatform("钉钉");
                chatGroupInfoMapper.updateById(info);
                dingtalkUtil.sendMessage(dingTalkMessageBuilder.sampleText("群聊信息已更新！在RPA中可通过指定接口调用消息通知", channelId));
                return true;
            }
            info = new ChatGroupInfo();
            info.setGroupId(openThreadId);
            info.setGroupName(conversationTitle);
            info.setShortName(shortName);
            info.setPlatform("钉钉");
            chatGroupInfoMapper.insert(info);
            log.info("保存群聊信息: 群聊ID={}, 群聊名称={}, 简称={}", openThreadId, conversationTitle, shortName);
            dingtalkUtil.sendMessage(dingTalkMessageBuilder.sampleText("群聊已注册！在RPA中可通过指定接口调用消息通知", channelId));
            return true;
        } catch (Exception e) {
            log.error("保存或更新群聊信息失败: 群聊ID={}", openThreadId, e);
            return false;
        }
    }

    public void saveWebhookUrl(String webhookUrl, String openThreadId) {
        ChatGroupInfo info = chatGroupInfoMapper.selectOne(new LambdaQueryWrapper<ChatGroupInfo>().eq(ChatGroupInfo::getGroupId,openThreadId));
        if(info!=null){
            info.setWebhookUrl(webhookUrl);
            chatGroupInfoMapper.updateById(info);
            log.info("更新群聊Webhook地址: 群聊ID={}, Webhook={}", openThreadId, webhookUrl);
            dingtalkUtil.sendMessage(dingTalkMessageBuilder.sampleText("Webhook地址已更新！在RPA中可通过指定接口调用消息通知",openThreadId));
        }else{
            dingtalkUtil.sendMessage(dingTalkMessageBuilder.sampleText("未在服务器记录中找到当前群聊!请使用[注册#群聊简称]来注册群聊!",openThreadId));
        }
    }

    public void queryTaskProgress(String openThreadId, String content) {
        // 查询预售进度
        List<String> queryParam = List.of(content.split("#"));
        String queryDay;
        if(queryParam.size() > 1){
            // 有查询参数
            queryDay = queryParam.get(1).trim();
        }else {
            // 无查询参数，使用默认值
            LocalDate today = LocalDate.now();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            queryDay = today.format(fmt);
        }
        List<TaskProgress> taskJonsList = taskJobMapper.queryProgress(queryDay);
        if(taskJonsList.isEmpty()){
            dingtalkUtil.sendMessage(dingTalkMessageBuilder.sampleText("未查询到对应日期的任务进度信息，请确认日期是否正确！",openThreadId));
            return;
        }
        dingtalkUtil.sendMessage(dingTalkMessageBuilder.buildPresaleQueryMarkdownMessage(queryDay, taskJonsList, openThreadId));
    }

}
