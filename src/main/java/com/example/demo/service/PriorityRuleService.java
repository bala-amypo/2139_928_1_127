package com.example.demo.service;

import java.util.*;
import com.example.demo.entity.*;

public interface PriorityRuleService {

    int calculatePriority(Complaint.Severity severity, Complaint.Urgency urgency);

    int computePriorityScore(Complaint complaint);

    List<PriorityRule> getActiveRules();
}
