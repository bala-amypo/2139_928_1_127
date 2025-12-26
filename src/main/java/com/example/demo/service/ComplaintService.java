package com.example.demo.service;

import java.util.*;
import com.example.demo.dto.ComplaintRequest;
import com.example.demo.entity.*;

public interface ComplaintService {

    Complaint submitComplaint(ComplaintRequest request, User user);

    List<Complaint> getComplaintsForUser(User user);

    List<Complaint> getPrioritizedComplaints();

    Complaint updateStatus(Long id, Complaint.Status status);
}
