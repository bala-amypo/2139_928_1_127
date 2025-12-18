package com.example.demo.service;
import java.util.List;
import com.example.demo.entity.ComplaintStatus;
public interface ComplaintStatusService {
    void updateStatus(Long complaintId, String status);
    List<ComplaintStatus> getStatusHistory(Long complaintId);
}