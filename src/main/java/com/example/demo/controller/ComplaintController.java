package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

import com.example.demo.service.ComplaintService;
import com.example.demo.dto.ComplaintRequest;

@RestController
@RequestMapping("/complaints")
public class ComplaintController {

    private final ComplaintService service;

    public ComplaintController(ComplaintService service) {
        this.service = service;
    }

    // ✅ POST – show success message in Swagger
    @PostMapping("/submit")
    public Map<String, String> submit(@RequestBody ComplaintRequest r) {
        service.submitComplaint(r);   // data stored in DB
        return Map.of("message", "Complaint submitted successfully");
    }

    @GetMapping("/user/{id}")
    public Object user(@PathVariable Long id) {
        return service.getUserComplaints(id);
    }

    @GetMapping("/prioritized")
    public Object prioritized() {
        return service.getPrioritizedComplaints();
    }

    @PutMapping("/status/{id}")
    public Map<String, String> status(@PathVariable Long id,
                                      @RequestParam String status) {
        service.updateComplaintStatus(id, status);
        return Map.of("message", "Status updated successfully");
    }
}
