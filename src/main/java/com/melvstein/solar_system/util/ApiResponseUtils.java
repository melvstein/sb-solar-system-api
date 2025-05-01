package com.melvstein.solar_system.util;

import com.melvstein.solar_system.constant.ApiConstants;
import com.melvstein.solar_system.dto.ApiResponse;
import org.springframework.util.StringUtils;

public class ApiResponseUtils {
    private ApiResponseUtils() {}

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                ApiConstants.RESPONSE_SUCCESS.get("code"),
                ApiConstants.RESPONSE_SUCCESS.get("message"),
                data);
    }

    public static <T> ApiResponse<T> success(String code, String message, T data) {
        return new ApiResponse<>(
                StringUtils.hasText(code) ? code : ApiConstants.RESPONSE_SUCCESS.get("code"),
                StringUtils.hasText(message) ? message : ApiConstants.RESPONSE_SUCCESS.get("message"),
                data);
    }

    public static <T> ApiResponse<T> error(T data) {
        return new ApiResponse<>(
                ApiConstants.RESPONSE_ERROR.get("code"),
                ApiConstants.RESPONSE_ERROR.get("message"),
                data);
    }

    public static <T> ApiResponse<T> error(String code, String message, T data) {
        return new ApiResponse<>(
                StringUtils.hasText(code) ? code : ApiConstants.RESPONSE_ERROR.get("code"),
                StringUtils.hasText(message) ? message : ApiConstants.RESPONSE_ERROR.get("message"),
                data);
    }

    public static <T> ApiResponse<T> error() {
        return error(null);
    }
}
