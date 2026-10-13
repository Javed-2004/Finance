package com.finance.common.exception;

import com.finance.common.response.ApiError;
import com.finance.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailAlreadyExists(
            EmailAlreadyExistsException ex
    ) {

        ApiError error = new ApiError(
                "EMAIL_ALREADY_EXISTS",
                "email",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ApiResponse.error(
                                "Registration failed",
                                List.of(error)
                        )
                );
    }


    @ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidOtp(
            InvalidOtpException ex
    ) {

        ApiError error = new ApiError(
                "INVALID_OTP",
                "otp",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ApiResponse.error(
                                "OTP verification failed",
                                List.of(error)
                        )
                );
    }

    @ExceptionHandler(OtpExpiredException.class)
    public ResponseEntity<ApiResponse<Void>> handleOtpExpired(
            OtpExpiredException ex
    ) {

        ApiError error = new ApiError(
                "OTP_EXPIRED",
                "otp",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.GONE)
                .body(
                        ApiResponse.error(
                                "OTP expired",
                                List.of(error)
                        )
                );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidCredentials(
            InvalidCredentialsException ex
    ) {

        ApiError error = new ApiError(
                "INVALID_CREDENTIALS",
                null,
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ApiResponse.error(
                                "Authentication failed",
                                List.of(error)
                        )
                );
    }
}


