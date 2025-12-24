package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import com.example.demo.entity.*;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findByCustomer(User customer);

    @Query("select c from Complaint c order by c.priorityScore desc, c.createdAt asc")
    List<Complaint> findAllOrderByPriorityScoreDescCreatedAtAsc();
}
