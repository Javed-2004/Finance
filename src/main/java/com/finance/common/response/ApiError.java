package com.finance.common.response;

public record ApiError(
        String code,
        String field,
        String message
) {
}