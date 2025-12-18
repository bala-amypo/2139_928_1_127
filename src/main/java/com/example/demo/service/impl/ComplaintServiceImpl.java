package com.example.demo.service.impl;

import com.example.demo.repository.ComplaintRepository;
import com.example.demo.service.ComplaintService;
import com.example.demo.service.PriorityRuleService;
import com.example.demo.service.UserService;
import com.example.demo.dto.ComplaintRequest;
import com.example.demo.entity.Complaint;
import com.example.demo.entity.User;

import org.springframework.stereotype.Service;
import java.util.List;

@Service  
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository repo;
    private final PriorityRuleService ruleService;
    private final UserService userService;

    public ComplaintServiceImpl(
            ComplaintRepository repo,
            PriorityRuleService ruleService,
            UserService userService) {
        this.repo = repo;
        this.ruleService = ruleService;
        this.userService = userService;
    }

    @Override
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

    @Override
    public List<Complaint> getUserComplaints(Long userId) {
        return repo.findByUserId(userId);
    }

    @Override
    public List<Complaint> getPrioritizedComplaints() {
        return repo.findAllOrderByPriorityScoreDescCreatedAtAsc();
    }

    @Override
    public void updateComplaintStatus(Long id, String status) {
        // optional
    }
}
