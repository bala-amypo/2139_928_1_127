package com.example.demo.service.impl;
import com.example.demo.repository.*;
import com.example.demo.service.ComplaintStatusService;
import com.example.demo.entity.*;
import java.util.List;
public class ComplaintStatusServiceImpl implements ComplaintStatusService {
    private final ComplaintStatusRepository statusRepo;
    private final ComplaintRepository complaintRepo;
    public ComplaintStatusServiceImpl(ComplaintStatusRepository statusRepo, ComplaintRepository complaintRepo) {
        this.statusRepo = statusRepo;
        this.complaintRepo = complaintRepo;
    }
    public void updateStatus(Long complaintId, String status) {
        Complaint c = complaintRepo.findById(complaintId).orElseThrow();
        ComplaintStatus cs = new ComplaintStatus();
        cs.setComplaint(c);
        cs.setStatus(status);
        statusRepo.save(cs);
    }
    public List<ComplaintStatus> getStatusHistory(Long complaintId) {
        return statusRepo.findByComplaintId(complaintId);
    }
}