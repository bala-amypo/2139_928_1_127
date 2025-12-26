package com.example.demo.service;

import com.example.demo.entity.Complaint;

public interface PriorityRuleService {

    int calculatePriority(
            Complaint.Severity severity,
            Complaint.Urgency urgency
    );

    // ✅ REQUIRED BY TEST
    int computePriorityScore(Complaint complaint);
}
