package com.example.demo.controller;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService service;
    public AuthController(UserService service) { this.service = service; }
    @PostMapping("/register")
    public Object register(@RequestBody User u) { return service.saveUser(u); }
    @PostMapping("/login")
    public Object login(@RequestBody User u) { return service.findByEmail(u.getEmail()); }
}