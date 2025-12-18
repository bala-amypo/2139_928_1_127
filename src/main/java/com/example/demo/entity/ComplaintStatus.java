package com.example.demo.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
public class ComplaintStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Complaint complaint;
    private String status;
    private LocalDateTime updatedOn;
    @PrePersist
    public void onUpdate() { this.updatedOn = LocalDateTime.now(); }
    public Long getId() { return id; }
    public void setComplaint(Complaint complaint) { this.complaint = complaint; }
    public void setStatus(String status) { this.status = status; }
}