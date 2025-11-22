package com.felixstudio.automationcontrol.service.dingTalk;

import com.alibaba.fastjson2.JSONObject;
import com.dingtalk.open.app.api.OpenDingTalkClient;
import com.felixstudio.automationcontrol.dingTalk.DingTalkMessageBuilder;
import com.felixstudio.automationcontrol.dingTalk.DingtalkUtil;
import com.felixstudio.automationcontrol.dto.dingTalk.MessageDTO;
import com.felixstudio.automationcontrol.entity.dingtalkEntity.ChatGroupInfo;
import com.felixstudio.automationcontrol.mapper.dingTalk.ChatGroupInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
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

    public JSONObject sendMsg(String message, String groupShort) {
        if (groupShort == null || groupShort.isBlank()) {
            return null;
        }
        List<ChatGroupInfo> list = chatGroupInfoMapper.selectByMap(Map.of("short_name", groupShort));
        Optional<ChatGroupInfo> opt = list.stream().findFirst();
        if (opt.isEmpty()) {
            return null;
        } else {
            ChatGroupInfo chatGroupInfo = opt.get();
            return dingtalkUtil.sendMessage(
                    messageBuilder.sampleText(message, chatGroupInfo.getGroupId())
            );
        }
    }

    private String uploadFile(MultipartFile file, String type) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件为空");
        }
        // 调用钉钉文件上传
        String url = "https://oapi.dingtalk.com/media/upload?access_token=" + dingtalkUtil.getAccessToken();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("type", type);
        ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        body.add("media", resource);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<JSONObject> response = dingtalkUtil.getRestTemplate().postForEntity(url, request, JSONObject.class);
        if (response.getBody() != null) {
            int errorCode = response.getBody().getIntValue("errcode");
            if (errorCode == 0) {
                return response.getBody().getString("media_id");
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public boolean sendFileMsg(MultipartFile file, MessageDTO messageDTO) {
        try {
            if (messageDTO.getFileType().isBlank() || messageDTO.getGroupShort().isBlank()) {
                throw new IllegalArgumentException("文件类型或群聊简称不能为空");
            }
            String mediaId = this.uploadFile(file, messageDTO.getFileType());
            if (mediaId == null) {
                throw new IllegalArgumentException("文件上传失败，无法获取mediaId");
            }
            List<ChatGroupInfo> list = chatGroupInfoMapper.selectByMap(Map.of("short_name", messageDTO.getGroupShort()));
            Optional<ChatGroupInfo> opt = list.stream().findFirst();
            if (opt.isEmpty()) {
                throw new IllegalArgumentException("未找到对应的群聊信息");
            }
            ChatGroupInfo chatGroupInfo = opt.get();
            JSONObject fileMsgObj = messageBuilder.buildFileMessage(mediaId, file.getOriginalFilename(), messageDTO.getFileType(),chatGroupInfo.getGroupId());
            dingtalkUtil.sendMessage(fileMsgObj);
            if(!messageDTO.getMessage().isBlank()){
                JSONObject messageJson = messageBuilder.sampleText(messageDTO.getMessage(), chatGroupInfo.getGroupId());
                dingtalkUtil.sendMessage(messageJson);
            }
            return true;
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败:" + e.getMessage());
        }
    }
}
