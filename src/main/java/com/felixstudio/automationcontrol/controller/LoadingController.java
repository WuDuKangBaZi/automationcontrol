package com.felixstudio.automationcontrol.controller;

import com.felixstudio.automationcontrol.common.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plugin")
public class LoadingController {

    @RequestMapping(value = "/status",method = {RequestMethod.GET, RequestMethod.POST})
    public ApiResponse<String> status() {
        return ApiResponse.success("Service is up and running");
    }
}
