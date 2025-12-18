package com.example.demo.controller;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.ComplaintService;
import com.example.demo.dto.ComplaintRequest;
@RestController
@RequestMapping("/complaints")
public class ComplaintController {
    private final ComplaintService service;
    public ComplaintController(ComplaintService service) { this.service = service; }
    @PostMapping("/submit")
    public Object submit(@RequestBody ComplaintRequest r) { return service.submitComplaint(r); }
    @GetMapping("/user/{id}")
    public Object user(@PathVariable Long id) { return service.getUserComplaints(id); }
    @GetMapping("/prioritized")
    public Object prioritized() { return service.getPrioritizedComplaints(); }
    @PutMapping("/status/{id}")
    public void status(@PathVariable Long id, @RequestParam String status) { service.updateComplaintStatus(id, status); }
}
