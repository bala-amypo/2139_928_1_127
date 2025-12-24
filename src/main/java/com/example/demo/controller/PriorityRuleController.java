package com.example.demo.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.PriorityRule;
import com.example.demo.service.PriorityRuleService;

@RestController
@RequestMapping("/rules")
public class PriorityRuleController {

    private final PriorityRuleService ruleService;

    public PriorityRuleController(PriorityRuleService ruleService) {
        this.ruleService = ruleService;
    }

    @GetMapping("/all")
    public List<PriorityRule> getRules() {
        return ruleService.getActiveRules();
    }
}
