package com.example.demo.service;
import java.util.List;
import com.example.demo.entity.PriorityRule;
public interface PriorityRuleService {
    int calculatePriority(String category);
    List<PriorityRule> getAllRules();
}