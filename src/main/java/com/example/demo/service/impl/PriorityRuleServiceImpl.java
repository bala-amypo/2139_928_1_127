package com.example.demo.service.impl;

import com.example.demo.repository.PriorityRuleRepository;
import com.example.demo.service.PriorityRuleService;
import com.example.demo.entity.PriorityRule;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PriorityRuleServiceImpl implements PriorityRuleService {

    private final PriorityRuleRepository repo;

    public PriorityRuleServiceImpl(PriorityRuleRepository repo) {
        this.repo = repo;
    }

    @Override
    public int calculatePriority(String category) {
        return repo.findByCategory(category)
                   .map(PriorityRule::getBaseScore)
                   .orElse(0);
    }

    @Override
    public List<PriorityRule> getAllRules() {
        return repo.findAll();
    }
}
