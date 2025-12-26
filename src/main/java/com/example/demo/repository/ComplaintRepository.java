package com.example.demo.repository;

import com.example.demo.entity.Complaint;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    // Required by tests
    List<Complaint> findByCustomer(User customer);

    // ✅ FIXED: Explicit JPQL avoids method-name parsing error
    @Query("""
        SELECT c
        FROM Complaint c
        ORDER BY
            c.priorityScore DESC NULLS LAST,
            c.createdAt ASC
    """)
    List<Complaint> findAllOrderByPriorityScoreDescCreatedAtAsc();
}
