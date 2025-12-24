package com.example.demo.service;

import java.util.List;
import com.example.demo.entity.Complaint;
import com.example.demo.entity.PriorityRule;

public interface PriorityRuleService {

    int computePriorityScore(Complaint complaint);

    List<PriorityRule> getActiveRules();
}
