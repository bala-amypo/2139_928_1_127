package com.example.demo.service.impl;

import com.example.demo.repository.ComplaintStatusRepository;
import com.example.demo.repository.ComplaintRepository;
import com.example.demo.service.ComplaintStatusService;
import com.example.demo.entity.Complaint;
import com.example.demo.entity.ComplaintStatus;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ComplaintStatusServiceImpl implements ComplaintStatusService {

    private final ComplaintStatusRepository statusRepo;
    private final ComplaintRepository complaintRepo;

    public ComplaintStatusServiceImpl(
            ComplaintStatusRepository statusRepo,
            ComplaintRepository complaintRepo) {
        this.statusRepo = statusRepo;
        this.complaintRepo = complaintRepo;
    }

    @Override
    public void updateStatus(Long complaintId, String status) {
        Complaint c = complaintRepo.findById(complaintId).orElseThrow();
        ComplaintStatus cs = new ComplaintStatus();
        cs.setComplaint(c);
        cs.setStatus(status);
        statusRepo.save(cs);
    }

    @Override
    public List<ComplaintStatus> getStatusHistory(Long complaintId) {
        return statusRepo.findByComplaintId(complaintId);
    }
}
