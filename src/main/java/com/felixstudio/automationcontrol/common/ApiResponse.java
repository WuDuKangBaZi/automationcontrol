package com.felixstudio.automationcontrol.common;

import lombok.Data;

@Data
public class ApiResponse <T> {
    private int code;
    private String message;
    private T data;

    // 成功返回
    public static <T> ApiResponse<T> success(T data){
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Success");
        response.setData(data);
        return response;
    }
    // 成功返回带自定义消息
    public static <T> ApiResponse<T> success(T data, String message){
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage(message);
        response.setData(data);
        return response;
    }
    // 失败返回
    public static <T> ApiResponse<T> failure(int code, String message){
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setData(null);
        return response;
    }
}
