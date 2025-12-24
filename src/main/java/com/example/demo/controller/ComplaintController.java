package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.ComplaintRequest;
import com.example.demo.entity.Complaint;
import com.example.demo.entity.User;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.ComplaintService;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public ComplaintController(ComplaintService complaintService,
                               UserService userService,
                               JwtUtil jwtUtil) {
        this.complaintService = complaintService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // ✅ SUBMIT COMPLAINT (TEST EXPECTED)
    @PostMapping("/submit")
    public Complaint submitComplaint(@RequestBody ComplaintRequest request,
                                     HttpServletRequest httpRequest) {

        String authHeader = httpRequest.getHeader("Authorization");
        String token = authHeader.substring(7); // remove "Bearer "
        String email = jwtUtil.extractEmail(token);

        User user = userService.findByEmail(email);
        return complaintService.submitComplaint(request, user);
    }

    // ✅ GET USER COMPLAINTS (JWT BASED)
    @GetMapping("/user")
    public List<Complaint> getUserComplaints(HttpServletRequest httpRequest) {

        String authHeader = httpRequest.getHeader("Authorization");
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);

        User user = userService.findByEmail(email);
        return complaintService.getComplaintsForUser(user);
    }

    // ✅ PRIORITIZED COMPLAINTS
    @GetMapping("/prioritized")
    public List<Complaint> prioritized() {
        return complaintService.getPrioritizedComplaints();
    }

    // ✅ UPDATE STATUS (OPTIONAL TEST)
    @PutMapping("/status/{id}")
    public Complaint updateStatus(@PathVariable Long id,
                                  @RequestParam Complaint.Status status) {
        return complaintService.updateStatus(id, status);
    }
}
