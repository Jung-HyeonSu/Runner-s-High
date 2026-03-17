package com.runnershigh.presentation.common;

import com.runnershigh.application.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final ErrorResponse error;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode, String message) {
        return new ApiResponse<>(false, null, new ErrorResponse(errorCode.name(), message));
    }

    @Getter
    @AllArgsConstructor
    public static class ErrorResponse {
        private final String code;
        private final String message;
    }
}
