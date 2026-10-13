package com.finance.common.response;

public record ApiMeta(
        Integer page,
        Integer size,
        Long totalElements,
        Integer totalPages
) {
}