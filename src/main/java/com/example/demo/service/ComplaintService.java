package com.example.demo.service;

import com.example.demo.dto.ComplaintRequest;
import com.example.demo.entity.Complaint;
import java.util.List;

public interface ComplaintService {
    Complaint submitComplaint(ComplaintRequest request);
    List<Complaint> getUserComplaints(Long userId);
    List<Complaint> getPrioritizedComplaints();
    void updateComplaintStatus(Long id, String status);
}
