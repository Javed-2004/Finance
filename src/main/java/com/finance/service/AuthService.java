package com.finance.service;

import com.finance.common.exception.EmailAlreadyExistsException;
import com.finance.common.exception.InvalidCredentialsException;
import com.finance.dto.AuthResponse;
import com.finance.dto.AuthResponseAuth;
import com.finance.dto.LoginRequest;
import com.finance.dto.OtpVerifyRequest;
import com.finance.dto.RegisterRequest;
import com.finance.entity.User;
import com.finance.repository.UserRepository;
import com.finance.service.OtpService.OtpService;

import jakarta.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OtpService otpService;
    private final EmailService emailService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            OtpService otpService,
            EmailService emailService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.otpService = otpService;
        this.emailService = emailService;
    }




    public void register(RegisterRequest request , String role) {

        if (userRepository.existsByEmail(request.getEmail())) {
                throw new EmailAlreadyExistsException(
                        "Email already registered"
                );
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // NEVER store plain password
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(role);

        userRepository.save(user);
    }




        public String registerAuth(
                        RegisterRequest request,
                        String role
                ) {

                if (userRepository.existsByEmail(
                        request.getEmail()
                )) {

                        throw new EmailAlreadyExistsException(
                                "Email already registered"
                        );
                }

                User user = new User();

                user.setName(request.getName());
                user.setEmail(request.getEmail());

                user.setPassword(
                        passwordEncoder.encode(
                                request.getPassword()
                        )
                );

                user.setRole(role);

                user.setEmailVerified(false);

                userRepository.save(user);

                OtpService.OtpData otpData =
                        otpService.createOtp(
                                user.getEmail(),
                                "REGISTER"
                        );

                emailService.sendOtp(
                        user.getEmail(),
                        otpData.otp()
                );

                return otpData.challengeId();
        }

        @Transactional
        public void verifyRegistrationOtp(
                        OtpVerifyRequest request
                ) {

                String email =
                        otpService.verifyOtp(
                                request.challengeId(),
                                "REGISTER",
                                request.otp()
                        );

                User user = userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

                user.setEmailVerified(true);

                userRepository.save(user);
        }





    public AuthResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password")
                );

        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!passwordMatches) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                token,
                jwtService.getExpiration()
        );
    }



    public String loginAuth(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid email or password"
                        )
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {

                throw new InvalidCredentialsException(
                        "Invalid email or password"
                );
        }

        if (!user.isEmailVerified()) {

                throw new InvalidCredentialsException(
                        "Email is not verified"
                );
        }

        OtpService.OtpData otpData =
                otpService.createOtp(
                        user.getEmail(),
                        "LOGIN"
                );

        emailService.sendOtp(
                user.getEmail(),
                otpData.otp()
        );

        return otpData.challengeId();
    }

    public AuthResponseAuth verifyLoginOtp(
                OtpVerifyRequest request
        ) {

        String email =
                otpService.verifyOtp(
                        request.challengeId(),
                        "LOGIN",
                        request.otp()
                );

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );

        String token =
                jwtService.generateToken(user);

        return new AuthResponseAuth(token);
    }
}