package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Complaint {

    public enum Status { NEW, OPEN, IN_PROGRESS, RESOLVED }
    public enum Severity { LOW, MEDIUM, HIGH, CRITICAL }
    public enum Urgency { LOW, MEDIUM, HIGH, IMMEDIATE }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String category;
    private String channel;
    private Integer priorityScore;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    @Enumerated(EnumType.STRING)
    private Urgency urgency;

    private LocalDateTime createdAt;

    // 🔴 TEST EXPECTS THESE NAMES
    @ManyToOne
    private User customer;

    @ManyToOne
    private User assignedAgent;

    @ManyToMany
    private List<PriorityRule> priorityRules = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = Status.NEW;
    }

    // ===== REQUIRED GETTERS & SETTERS =====

    public User getCustomer() { return customer; }
    public void setCustomer(User customer) { this.customer = customer; }

    public User getAssignedAgent() { return assignedAgent; }
    public void setAssignedAgent(User assignedAgent) { this.assignedAgent = assignedAgent; }

    public List<PriorityRule> getPriorityRules() { return priorityRules; }

    public Severity getSeverity() { return severity; }
    public Urgency getUrgency() { return urgency; }

    public void setSeverity(Severity severity) { this.severity = severity; }
    public void setUrgency(Urgency urgency) { this.urgency = urgency; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setChannel(String channel) { this.channel = channel; }
    public void setPriorityScore(Integer priorityScore) { this.priorityScore = priorityScore; }
    public void setStatus(Status status) { this.status = status; }
}
