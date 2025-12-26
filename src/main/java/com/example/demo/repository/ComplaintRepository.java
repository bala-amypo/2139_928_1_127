package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Complaint;
import com.example.demo.entity.User;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    // ✅ TEST EXPECTED
    List<Complaint> findByCustomer(User customer);

    // ✅ TEST EXPECTED
    List<Complaint> findAllOrderByPriorityScoreDescCreatedAtAsc();
}
