package com.example.demo.service.impl;
import com.example.demo.repository.PriorityRuleRepository;
import com.example.demo.service.PriorityRuleService;
import com.example.demo.entity.PriorityRule;
import java.util.List;
public class PriorityRuleServiceImpl implements PriorityRuleService {
    private final PriorityRuleRepository repo;
    public PriorityRuleServiceImpl(PriorityRuleRepository repo) {
        this.repo = repo;
    }
    public int calculatePriority(String category) {
        return repo.findByCategory(category).map(PriorityRule::getBaseScore).orElse(0);
    }
    public List<PriorityRule> getAllRules() {
        return repo.findAll();
    }
}