package com.example.demo.service.impl;

import org.springframework.stereotype.Service;
import com.example.demo.entity.Complaint;
import com.example.demo.service.PriorityRuleService;

@Service
public class PriorityRuleServiceImpl implements PriorityRuleService {

    @Override
    public int calculatePriority(
            Complaint.Severity severity,
            Complaint.Urgency urgency) {

        int score = 0;

        switch (severity) {
            case LOW -> score += 1;
            case MEDIUM -> score += 2;
            case HIGH -> score += 3;
            case CRITICAL -> score += 4;
        }

        switch (urgency) {
            case LOW -> score += 1;
            case MEDIUM -> score += 2;
            case HIGH -> score += 3;
            case IMMEDIATE -> score += 4;
        }

        return score;
    }

    @Override
    public int computePriorityScore(Complaint complaint) {
        return calculatePriority(
                complaint.getSeverity(),
                complaint.getUrgency()
        );
    }
}
