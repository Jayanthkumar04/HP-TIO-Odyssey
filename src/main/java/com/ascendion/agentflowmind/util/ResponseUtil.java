package com.ascendion.agentflowmind.util;

public class ResponseUtil {

    public static <T> ApiResponse<T> success(int statusCode, String message, T data) {
        return new ApiResponse<>(statusCode, message, data);
    }

    public static <T> ApiResponse<T> error(int statusCode, String message, T data) {
        return new ApiResponse<>(statusCode, message, data);
    }

    public static <T> ApiResponse<T> customReponse(int statusCode, String message, String name, T data) {
        return new ApiResponse<>(statusCode, message, name, data);
    }
}