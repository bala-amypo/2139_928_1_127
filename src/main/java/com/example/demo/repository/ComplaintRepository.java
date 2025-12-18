package com.example.demo.repository;
import org.springframework.data.jpa.repository.*;
import com.example.demo.entity.Complaint;
import java.util.List;
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByUserId(Long userId);
    @Query("SELECT c FROM Complaint c ORDER BY c.priorityScore DESC, c.submittedOn ASC")
    List<Complaint> findAllOrderByPriorityScoreDescCreatedAtAsc();
}