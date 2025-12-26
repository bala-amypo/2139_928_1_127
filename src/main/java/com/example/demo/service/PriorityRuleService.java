package com.example.demo.service;

import java.util.List;
import com.example.demo.entity.Complaint;
import com.example.demo.entity.PriorityRule;

public interface PriorityRuleService {

    int calculatePriority(
            Complaint.Severity severity,
            Complaint.Urgency urgency
    );

    // ✅ REQUIRED BY TESTS
    int computePriorityScore(Complaint complaint);

    // ✅ REQUIRED BY CONTROLLER
    List<PriorityRule> getActiveRules();
}
