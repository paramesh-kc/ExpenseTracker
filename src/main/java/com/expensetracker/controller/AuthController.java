package com.expensetracker.controller;

import com.expensetracker.dto.*;
import com.expensetracker.model.User;
import com.expensetracker.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = authService.register(request);
        // JWT will be added later — for now return dummy token
        return ResponseEntity.status(201).body(
                new AuthResponse("dummy-token", user.getName(), user.getEmail())
        );
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = authService.login(request);
        return ResponseEntity.ok(
                new AuthResponse("dummy-token", user.getName(), user.getEmail())
        );
    }
}