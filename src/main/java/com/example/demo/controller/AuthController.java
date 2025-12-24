package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.AuthRequest;
import com.example.demo.entity.User;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerCustomer(
                user.getFullName(),
                user.getEmail(),
                user.getPassword()
        );
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request) {
        User user = userService.findByEmail(request.getEmail());
        return jwtUtil.generateToken(user.getEmail());
    }
}
