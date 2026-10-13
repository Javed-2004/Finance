package com.finance.controller;

import com.finance.common.response.ApiResponse;
import com.finance.dto.UserResponse;
import com.finance.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {

        List<UserResponse> users = userService.getAllUsers();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Users fetched successfully",
                        users
                )
        );
    }
}