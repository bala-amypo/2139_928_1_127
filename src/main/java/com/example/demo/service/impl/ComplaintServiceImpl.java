package com.example.demo.service.impl;
import com.example.demo.repository.ComplaintRepository;
import com.example.demo.service.*;
import com.example.demo.dto.ComplaintRequest;
import com.example.demo.entity.*;
import java.util.List;

public class ComplaintServiceImpl implements ComplaintService {
    private final ComplaintRepository repo;
    private final PriorityRuleService ruleService;
    private final UserService userService;
    public ComplaintServiceImpl(ComplaintRepository repo, PriorityRuleService ruleService, UserService userService) {
        this.repo = repo;
        this.ruleService = ruleService;
        this.userService = userService;
    }
    public Complaint submitComplaint(ComplaintRequest request) {
        User user = userService.findById(request.getUserId());
        Complaint c = new Complaint();
        c.setTitle(request.getTitle());
        c.setDescription(request.getDescription());
        c.setCategory(request.getCategory());
        c.setPriorityScore(ruleService.calculatePriority(request.getCategory()));
        c.setUser(user);
        return repo.save(c);
    }
    public List<Complaint> getUserComplaints(Long userId) {
        return repo.findByUserId(userId);
    }
    public List<Complaint> getPrioritizedComplaints() {
        return repo.findAllOrderByPriorityScoreDescCreatedAtAsc();
    }
    public void updateComplaintStatus(Long id, String status) {}
}