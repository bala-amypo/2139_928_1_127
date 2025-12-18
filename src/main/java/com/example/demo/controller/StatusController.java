package com.example.demo.controller;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.ComplaintStatusService;
@RestController
@RequestMapping("/status")
public class StatusController {
    private final ComplaintStatusService service;
    public StatusController(ComplaintStatusService service) { this.service = service; }
    @GetMapping("/history/{id}")
    public Object history(@PathVariable Long id) { return service.getStatusHistory(id); }
}