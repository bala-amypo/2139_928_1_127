package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.ComplaintRequest;
import com.example.demo.entity.Complaint;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ComplaintRepository;
import com.example.demo.service.ComplaintService;
import com.example.demo.service.PriorityRuleService;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository repo;
    private final PriorityRuleService ruleService;

    // ✅ EXACT constructor expected by tests
    public ComplaintServiceImpl(ComplaintRepository repo,
                                PriorityRuleService ruleService) {
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
        c.setUser(user);
        c.setStatus(Complaint.Status.NEW);

        int priority = ruleService.calculatePriority(
                request.getSeverity(), request.getUrgency());
        c.setPriorityScore(priority);

        return repo.save(c);
    }

    @Override
    public List<Complaint> getComplaintsForUser(User user) {
        return repo.findByUser(user);
    }

    @Override
    public List<Complaint> getPrioritizedComplaints() {
        return repo.findAllOrderByPriorityScoreDescCreatedAtAsc();
    }

    @Override
    public Complaint updateStatus(Long id, Complaint.Status status) {

        Complaint c = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found"));

        c.setStatus(status);
        return repo.save(c);
    }
}
