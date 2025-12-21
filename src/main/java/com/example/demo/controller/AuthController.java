package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService service;

    public AuthController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public User register(@RequestBody User u) {
        return service.saveUser(u);
    }

    @PostMapping("/login")
    public User login(@RequestBody User u) {
        User user = service.findByEmail(u.getEmail());
        if (user == null) {
            throw new RuntimeException("Invalid email");
        }
        return user;
    }
}
