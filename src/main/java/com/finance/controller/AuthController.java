package com.finance.controller;

import com.finance.common.response.ApiResponse;
import com.finance.dto.AuthResponse;
import com.finance.dto.AuthResponseAuth;
import com.finance.dto.LoginRequest;
import com.finance.dto.OtpVerifyRequest;
import com.finance.dto.RegisterRequest;
import com.finance.service.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
            @RequestBody RegisterRequest request
    ) {

        authService.register(request, "USER");

        return ResponseEntity.ok(
                ApiResponse.success(
                        "User registered successfully",
                        null
                )
        );
    }



    @PostMapping("/registerAuth")
    public ResponseEntity<ApiResponse<?>> registerAuth(
            @RequestBody RegisterRequest request
    ) {

        String challengeId =
                authService.registerAuth(
                        request,
                        "USER"
                );

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(
                        ApiResponse.success(
                                "OTP sent to your email",
                                challengeId
                        )
                );
    }

    @PostMapping("/register/verify-otp")
    public ResponseEntity<ApiResponse<Void>> verifyRegistrationOtp(
            @RequestBody OtpVerifyRequest request
    ) {

        authService.verifyRegistrationOtp(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Email verified successfully",
                        null
                )
        );
    }


    @PostMapping("/registerManager")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerManager(
            @RequestBody RegisterRequest request
    ) {

        authService.register(request,"MANAGER");

        return ResponseEntity.ok(
                "User registered successfully"
        );
    }
    @PostMapping("/registerAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerAdmin(
            @RequestBody RegisterRequest request
    ) {

        authService.register(request,"ADMIN");

        return ResponseEntity.ok(
                "User registered successfully"
        );
    }





    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request
    ) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }

    @PostMapping("/loginAuth")
    public ResponseEntity<ApiResponse<?>> loginAuth(
            @RequestBody LoginRequest request
    ) {

        String challengeId =
                authService.loginAuth(request);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(
                        ApiResponse.success(
                                "OTP sent to your email",
                                challengeId
                        )
                );
    }


    @PostMapping("/login/verify-otp")
    public ResponseEntity<ApiResponse<AuthResponseAuth>>
    verifyLoginOtp(
            @RequestBody OtpVerifyRequest request
    ) {

        AuthResponseAuth response =
                authService.verifyLoginOtp(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Login successful",
                        response
                )
        );
    }
}