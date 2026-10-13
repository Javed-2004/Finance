package com.finance.dto;

public record OtpVerifyRequest(
        String challengeId,
        String otp
) {
}