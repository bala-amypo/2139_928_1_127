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

    private final ComplaintRepository complaintRepository;
    private final PriorityRuleService priorityRuleService;

    public ComplaintServiceImpl(ComplaintRepository complaintRepository,
                                PriorityRuleService priorityRuleService) {
        this.complaintRepository = complaintRepository;
        this.priorityRuleService = priorityRuleService;
    }

    @Override
    public Complaint submitComplaint(ComplaintRequest request, User customer) {

        Complaint complaint = new Complaint();
        complaint.setTitle(request.getTitle());
        complaint.setDescription(request.getDescription());
        complaint.setCategory(request.getCategory());
        complaint.setChannel(request.getChannel());
        complaint.setSeverity(request.getSeverity());
        complaint.setUrgency(request.getUrgency());
        complaint.setCustomer(customer);

        int score = priorityRuleService.computePriorityScore(complaint);
        complaint.setPriorityScore(score);

        return complaintRepository.save(complaint);
    }

    @Override
    public List<Complaint> getComplaintsForUser(User customer) {
        return complaintRepository.findByCustomer(customer);
    }

    @Override
    public List<Complaint> getPrioritizedComplaints() {
        return complaintRepository.findAllOrderByPriorityScoreDescCreatedAtAsc();
    }

    @Override
    public Complaint updateStatus(Long complaintId, Complaint.Status status) {

        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found"));

        complaint.setStatus(status);
        return complaintRepository.save(complaint);
    }
}
