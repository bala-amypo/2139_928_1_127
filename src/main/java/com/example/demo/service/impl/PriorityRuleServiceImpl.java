package com.example.demo.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Complaint;
import com.example.demo.entity.PriorityRule;
import com.example.demo.repository.PriorityRuleRepository;
import com.example.demo.service.PriorityRuleService;

@Service
public class PriorityRuleServiceImpl implements PriorityRuleService {

    private final PriorityRuleRepository ruleRepository;

    public PriorityRuleServiceImpl(PriorityRuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @Override
    public int computePriorityScore(Complaint complaint) {

        int score = 0;

        // Severity weight
        switch (complaint.getSeverity()) {
            case LOW -> score += 1;
            case MEDIUM -> score += 3;
            case HIGH -> score += 5;
            case CRITICAL -> score += 8;
        }

        // Urgency weight
        switch (complaint.getUrgency()) {
            case LOW -> score += 1;
            case MEDIUM -> score += 2;
            case HIGH -> score += 4;
            case IMMEDIATE -> score += 6;
        }

        // Active priority rules
        List<PriorityRule> rules = ruleRepository.findByActiveTrue();
        for (PriorityRule rule : rules) {
            score += rule.getWeight();
        }

        return Math.max(score, 0);
    }

    @Override
    public List<PriorityRule> getActiveRules() {
        return ruleRepository.findByActiveTrue();
    }
}
