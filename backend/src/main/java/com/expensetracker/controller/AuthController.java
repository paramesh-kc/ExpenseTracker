package com.expensetracker.controller;

import com.expensetracker.dto.*;
import com.expensetracker.model.User;
import com.expensetracker.security.JwtUtil;
import com.expensetracker.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletResponse response) {

        User user = authService.register(request);
        String token = jwtUtil.generateToken(user.getEmail(), user.getId());

        // Set JWT as HttpOnly cookie
        addTokenCookie(response, token);

        return ResponseEntity.status(201).body(Map.of(
                "userId", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "message", "Registered successfully"
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {

        User user = authService.login(request);
        String token = jwtUtil.generateToken(user.getEmail(), user.getId());

        // Set JWT as HttpOnly cookie
        addTokenCookie(response, token);

        return ResponseEntity.ok(Map.of(
                "userId", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "message", "Login successful"
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletResponse response) {
        // Delete cookie by setting maxAge to 0
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);    // expires immediately = deleted!
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser(
            jakarta.servlet.http.HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String email = (String) request.getAttribute("userEmail");

        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Not authenticated"));
        }

        return ResponseEntity.ok(Map.of(
                "userId", userId,
                "email", email
        ));
    }

    private void addTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);       // JavaScript CANNOT read this cookie
        cookie.setSecure(false);        // false for localhost, true for production (HTTPS)
        cookie.setPath("/");            // cookie sent for ALL paths
        cookie.setMaxAge((int)(jwtExpiration / 1000));  // 24 hours in seconds
        // SameSite=Lax set via response header (Cookie class doesn't support it)
        response.addCookie(cookie);
        response.setHeader("Set-Cookie",
                String.format("token=%s; Path=/; HttpOnly; Max-Age=%d; SameSite=Lax",
                        token, (int)(jwtExpiration / 1000)));
    }
}