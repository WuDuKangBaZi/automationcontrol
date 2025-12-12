package com.felixstudio.automationcontrol.controller.dingTalk;

import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.dto.dingTalk.MessageDTO;
import com.felixstudio.automationcontrol.service.dingTalk.DingTalkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/dingtalk")
public class DingTalkController {

    private final DingTalkService dingTalkService;

    public DingTalkController(DingTalkService dingTalkService) {
        this.dingTalkService = dingTalkService;
    }

    @PostMapping("/send")
    public ApiResponse<?> sendMsg(@RequestBody MessageDTO messageDTO){
        return ApiResponse.success(dingTalkService.sendMsg(messageDTO.getMessage(), messageDTO.getGroupShort()));
    }
    @PostMapping("/sendWebHook")
    public ApiResponse<?> sendWebHookMsg(@RequestBody MessageDTO messageDTO){
        return ApiResponse.success(dingTalkService.sendWebHookMsg(messageDTO.getMessage(), messageDTO.getGroupShort(),messageDTO.getAtMobiles()));
    }
    @PostMapping(value = "/send",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> sendFileMsg(@RequestPart("file") MultipartFile file,@RequestPart("message") String message,
                                      @RequestPart("groupShort") String groupShort,
                                      @RequestPart("fileType") String fileType){
        try{
            MessageDTO messageDTO = new MessageDTO(){
                {
                    setMessage(message);
                    setGroupShort(groupShort);
                    setFileType(fileType);
                }
            };
            return ApiResponse.success(dingTalkService.sendFileMsg(file,messageDTO));
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return ApiResponse.failure(500,e.getMessage());
        }
    }
}
