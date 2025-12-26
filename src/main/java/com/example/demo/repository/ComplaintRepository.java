package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Complaint;
import com.example.demo.entity.User;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    // ✅ MUST MATCH ENTITY FIELD NAME
    List<Complaint> findByCustomer(User customer);

    // ✅ CORRECT SPRING DATA SYNTAX
    List<Complaint> findAllByOrderByPriorityScoreDescCreatedAtAsc();
}
