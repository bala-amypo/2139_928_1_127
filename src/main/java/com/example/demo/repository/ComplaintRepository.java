package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Complaint;
import com.example.demo.entity.User;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    // ✅ MUST MATCH ENTITY FIELD NAME "user"
    List<Complaint> findByUser(User user);

    // ✅ CORRECT ORDER BY SYNTAX
    List<Complaint> findAllByOrderByPriorityScoreDescCreatedAtAsc();
}
