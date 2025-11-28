package com.felixstudio.automationcontrol.controller.util;

import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.entity.task.TaskLogFiles;
import com.felixstudio.automationcontrol.security.FileSecurity;
import com.felixstudio.automationcontrol.service.task.TaskLogFilesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
public class FileUtilController {
    @Value("${file.upload-dir}")
    private String uploadDir;

    private final TaskLogFilesService taskLogFilesService;

    public FileUtilController(TaskLogFilesService taskLogFilesService) {
        this.taskLogFilesService = taskLogFilesService;
    }

    // 文件上传接口，返回文件存储路径
    @PostMapping("/pub/uploadsLogsFile/{taskId}")
    public ApiResponse<?> uploadLogsFile(@PathVariable String taskId,
                                         @RequestParam("file")MultipartFile file) {
        try{
            if(file.isEmpty()){
                return ApiResponse.failure(500, "上传文件为空");
            }
            File dir = new File(uploadDir + "/logs/" + FileSecurity.sanitizeFileName(taskId));
            if(!dir.exists()){
                boolean createRet = dir.mkdirs();
                if(!createRet){
                    return ApiResponse.failure(500, "创建文件目录失败");
                }
            }
            String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID() + "." + ext;
            File dest = new File(dir, FileSecurity.sanitizeFileName(fileName));
            file.transferTo(dest);
            // 返回文件的相对路径
            String filePath = "/uploads/logs/" + FileSecurity.sanitizeFileName(taskId) + "/" + FileSecurity.sanitizeFileName(fileName);
            // 存储TaskId和filePath的关联关系
            TaskLogFiles taskLogFiles = new TaskLogFiles();
            taskLogFiles.setFileSize(file.getSize());
            taskLogFiles.setOriginalName(file.getOriginalFilename());
            taskLogFiles.setFilePath(filePath);
            taskLogFiles.setTaskId(Long.parseLong(taskId));
            taskLogFilesService.save(taskLogFiles);
            return ApiResponse.success(filePath);
        } catch (IOException e) {
            return ApiResponse.failure(500, "文件上传失败: " + e.getMessage());
        }

    }
}
