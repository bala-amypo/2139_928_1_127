package com.example.demo.service.impl;

import java.util.*;
import org.springframework.stereotype.Service;
import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.*;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository repo;
    private final UserService userService;
    private final ComplaintStatusService statusService;
    private final PriorityRuleService ruleService;

    // 🔥 EXACT constructor required by tests
    public ComplaintServiceImpl(
            ComplaintRepository repo,
            UserService userService,
            ComplaintStatusService statusService,
            PriorityRuleService ruleService) {
        this.repo = repo;
        this.userService = userService;
        this.statusService = statusService;
        this.ruleService = ruleService;
    }

    @Override
    public Complaint submitComplaint(ComplaintRequest request, User user) {
        Complaint c = new Complaint();
        c.setTitle(request.getTitle());
        c.setDescription(request.getDescription());
        c.setCategory(request.getCategory());
        c.setChannel(request.getChannel());
        c.setSeverity(request.getSeverity());
        c.setUrgency(request.getUrgency());
        c.setCustomer(user);

        int score = ruleService.computePriorityScore(c);
        c.setPriorityScore(score);

        return repo.save(c);
    }

    @Override
    public List<Complaint> getComplaintsForUser(User user) {
        return repo.findByCustomer(user);
    }

    @Override
    public List<Complaint> getPrioritizedComplaints() {
        return repo.findAllOrderByPriorityScoreDescCreatedAtAsc();
    }

    @Override
    public Complaint updateStatus(Long id, Complaint.Status status) {
        Complaint c = repo.findById(id).orElseThrow();
        c.setStatus(status);
        return repo.save(c);
    }
}
