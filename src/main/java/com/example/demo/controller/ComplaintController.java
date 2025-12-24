package com.example.demo.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.ComplaintRequest;
import com.example.demo.entity.Complaint;
import com.example.demo.entity.User;
import com.example.demo.service.ComplaintService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;
    private final UserService userService;

    public ComplaintController(ComplaintService complaintService,
                               UserService userService) {
        this.complaintService = complaintService;
        this.userService = userService;
    }

    @PostMapping("/submit/{userId}")
    public Complaint submit(@PathVariable Long userId,
                             @RequestBody ComplaintRequest request) {

        User user = userService.findById(userId);
        return complaintService.submitComplaint(request, user);
    }

    @GetMapping("/user/{userId}")
    public List<Complaint> userComplaints(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return complaintService.getComplaintsForUser(user);
    }

    @GetMapping("/prioritized")
    public List<Complaint> prioritized() {
        return complaintService.getPrioritizedComplaints();
    }

    @PutMapping("/status/{id}")
    public Complaint updateStatus(@PathVariable Long id,
                                  @RequestParam Complaint.Status status) {
        return complaintService.updateStatus(id, status);
    }
}
