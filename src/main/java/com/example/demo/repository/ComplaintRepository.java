package com.example.demo.repository;

import com.example.demo.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findByUser_Id(Long userId);

    List<Complaint> findAllByOrderByPriorityScoreDescSubmittedOnAsc();
}
