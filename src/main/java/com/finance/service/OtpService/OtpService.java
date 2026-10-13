package com.finance.service.OtpService;

import com.finance.common.exception.InvalidOtpException;
import com.finance.common.exception.OtpExpiredException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    private static final long OTP_TTL = 5;
    private static final TimeUnit OTP_TTL_UNIT = TimeUnit.MINUTES;

    private final StringRedisTemplate redisTemplate;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom secureRandom;

    public OtpService(
            StringRedisTemplate redisTemplate,
            PasswordEncoder passwordEncoder
    ) {
        this.redisTemplate = redisTemplate;
        this.passwordEncoder = passwordEncoder;
        this.secureRandom = new SecureRandom();
    }

    /**
     * Creates and stores a new OTP.
     */
    public OtpData createOtp(String email, String purpose) {

        String normalizedEmail = email.trim().toLowerCase();
        String normalizedPurpose = normalizePurpose(purpose);

        String challengeId = UUID.randomUUID().toString();

        String otp = String.format(
                "%06d",
                secureRandom.nextInt(1_000_000)
        );

        System.out.print(otp);
        String otpHash = passwordEncoder.encode(otp);

        String key = buildKey(
                normalizedPurpose,
                challengeId
        );

        /*
         * Store email and hashed OTP in Redis.
         */
        String value = normalizedEmail + ":" + otpHash;

        redisTemplate.opsForValue().set(
                key,
                value,
                OTP_TTL,
                OTP_TTL_UNIT
        );

        return new OtpData(
                challengeId,
                otp
        );
    }

    /**
     * Verifies an OTP.
     *
     * @return email associated with the OTP
     */
    public String verifyOtp(
            String challengeId,
            String purpose,
            String otp
    ) {

        if (challengeId == null || challengeId.isBlank()) {
            throw new InvalidOtpException("Invalid OTP challenge");
        }

        if (otp == null || !otp.matches("\\d{6}")) {
            throw new InvalidOtpException("Invalid OTP");
        }

        String key = buildKey(
                normalizePurpose(purpose),
                challengeId
        );

        String value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            throw new OtpExpiredException(
                    "OTP expired or invalid"
            );
        }

        String[] parts = value.split(":", 2);

        if (parts.length != 2) {
            redisTemplate.delete(key);

            throw new InvalidOtpException(
                    "Invalid OTP data"
            );
        }

        String email = parts[0];
        String otpHash = parts[1];

        boolean valid = passwordEncoder.matches(
                otp,
                otpHash
        );

        if (!valid) {
            throw new InvalidOtpException(
                    "Invalid OTP"
            );
        }

        /*
         * OTP is single-use.
         */
        redisTemplate.delete(key);

        return email;
    }

    /**
     * Generates Redis key.
     */
    private String buildKey(
            String purpose,
            String challengeId
    ) {

        return "finance:otp:"
                + purpose
                + ":"
                + challengeId;
    }

    private String normalizePurpose(String purpose) {

        if (purpose == null || purpose.isBlank()) {
            throw new IllegalArgumentException(
                    "OTP purpose is required"
            );
        }

        return purpose
                .trim()
                .toLowerCase();
    }

    public record OtpData(
            String challengeId,
            String otp
    ) {
    }
}