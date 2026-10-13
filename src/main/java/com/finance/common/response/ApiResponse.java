package com.finance.common.response;

import java.util.List;

public record ApiResponse<T>( boolean success, String message, T data, List<ApiError> errors, ApiMeta meta) {

    public static <T> ApiResponse<T> success( String message, T data ) {
        return new ApiResponse<>(
                true,
                message,
                data,
                null,
                null
        );
    }

    public static <T> ApiResponse<T> success( String message, T data, ApiMeta meta ) {
        return new ApiResponse<>(
                true,
                message,
                data,
                null,
                meta
        );
    }

    public static <T> ApiResponse<T> error( String message, List<ApiError> errors ) {
        return new ApiResponse<>(
                false,
                message,
                null,
                errors,
                null
        );
    }
}