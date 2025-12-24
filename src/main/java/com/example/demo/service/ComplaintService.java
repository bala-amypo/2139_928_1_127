package com.example.demo.service;

import java.util.List;
import com.example.demo.dto.ComplaintRequest;
import com.example.demo.entity.Complaint;
import com.example.demo.entity.User;

public interface ComplaintService {

    Complaint submitComplaint(ComplaintRequest request, User customer);

    List<Complaint> getComplaintsForUser(User customer);

    List<Complaint> getPrioritizedComplaints();

    Complaint updateStatus(Long complaintId, Complaint.Status status);
}
