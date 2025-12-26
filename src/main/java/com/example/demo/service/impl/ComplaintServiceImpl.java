package com.example.demo.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ComplaintRequest;
import com.example.demo.entity.Complaint;
import com.example.demo.entity.User;
import com.example.demo.repository.ComplaintRepository;
import com.example.demo.service.*;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository repo;
    private final PriorityRuleService ruleService;

    // 🔴 EXACT CONSTRUCTOR REQUIRED BY TEST
    public ComplaintServiceImpl(
            ComplaintRepository repo,
            UserService userService,
            ComplaintStatusService statusService,
            PriorityRuleService ruleService
    ) {
        this.repo = repo;
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
        c.setPriorityScore(ruleService.computePriorityScore(c));

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
}
