package com.felixstudio.automationcontrol.service.task.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.felixstudio.automationcontrol.entity.task.TaskLogFiles;
import com.felixstudio.automationcontrol.mapper.task.TaskLogFilesMapper;
import com.felixstudio.automationcontrol.service.task.TaskLogFilesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskLogFilesServiceImpl extends ServiceImpl<TaskLogFilesMapper, TaskLogFiles> implements TaskLogFilesService {
}
