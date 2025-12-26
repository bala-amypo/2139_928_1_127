package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.ComplaintRequest;
import com.example.demo.entity.Complaint;
import com.example.demo.entity.User;

public interface ComplaintService {

    Complaint submitComplaint(ComplaintRequest request, User user);

    List<Complaint> getComplaintsForUser(User user);

    List<Complaint> getPrioritizedComplaints();

    // ✅ MUST exist (tests expect this)
    Complaint updateStatus(Long id, Complaint.Status status);
}
