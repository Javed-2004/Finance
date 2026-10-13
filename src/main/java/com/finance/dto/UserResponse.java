package com.finance.dto;

public record UserResponse(
        Long id,
        String name,
        String email,
        String role,
        boolean emailVerified
) {
}