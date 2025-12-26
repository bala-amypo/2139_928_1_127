package com.example.demo.service.impl;

import java.util.*;
import org.springframework.stereotype.Service;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.*;

@Service
public class PriorityRuleServiceImpl implements PriorityRuleService {

    private final PriorityRuleRepository repo;

    public PriorityRuleServiceImpl(PriorityRuleRepository repo) {
        this.repo = repo;
    }

    @Override
    public int calculatePriority(Complaint.Severity s, Complaint.Urgency u) {
        return s.ordinal() + u.ordinal() + 2;
    }

    @Override
    public int computePriorityScore(Complaint c) {
        int base = calculatePriority(c.getSeverity(), c.getUrgency());
        return base + repo.findByActiveTrue()
                .stream()
                .mapToInt(PriorityRule::getWeight)
                .sum();
    }

    @Override
    public List<PriorityRule> getActiveRules() {
        return repo.findByActiveTrue();
    }
}
