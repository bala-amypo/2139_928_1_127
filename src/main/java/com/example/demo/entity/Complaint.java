



### Complaint.java

```java
package com.example.demo.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String category;
    private Integer priorityScore;
    private LocalDateTime submittedOn;
    @ManyToOne
    private User user;
    @PrePersist
    public void onCreate() { this.submittedOn = LocalDateTime.now(); }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getPriorityScore() { return priorityScore; }
    public void setPriorityScore(Integer priorityScore) { this.priorityScore = priorityScore; }
    public LocalDateTime getSubmittedOn() { return submittedOn; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
```

### PriorityRule.java

```java


### ComplaintStatus.java

```java
package com.example.demo.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
public class ComplaintStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Complaint complaint;
    private String status;
    private LocalDateTime updatedOn;
    @PrePersist
    public void onUpdate() { this.updatedOn = LocalDateTime.now(); }
    public Long getId() { return id; }
    public void setComplaint(Complaint complaint) { this.complaint = complaint; }
    public void setStatus(String status) { this.status = status; }
}
```

---

## DTO

### ComplaintRequest.java

```java
package com.example.demo.dto;
public class ComplaintRequest {
    private String title;
    private String description;
    private String category;
    private Long userId;
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public Long getUserId() { return userId; }
}
```

---

## Repository Interfaces

### UserRepository.java

```java
package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.User;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
```

### ComplaintRepository.java

```java
package com.example.demo.repository;
import org.springframework.data.jpa.repository.*;
import com.example.demo.entity.Complaint;
import java.util.List;
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByUserId(Long userId);
    @Query("SELECT c FROM Complaint c ORDER BY c.priorityScore DESC, c.submittedOn ASC")
    List<Complaint> findAllOrderByPriorityScoreDescCreatedAtAsc();
}
```

### PriorityRuleRepository.java

```java
package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.PriorityRule;
import java.util.Optional;
public interface PriorityRuleRepository extends JpaRepository<PriorityRule, Long> {
    Optional<PriorityRule> findByCategory(String category);
}
```

### ComplaintStatusRepository.java

```java
package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.ComplaintStatus;
import java.util.List;
public interface ComplaintStatusRepository extends JpaRepository<ComplaintStatus, Long> {
    List<ComplaintStatus> findByComplaintId(Long complaintId);
}
```

---

## Services & Implementations

### UserService.java

```java
package com.example.demo.service;
import com.example.demo.entity.User;
public interface UserService {
    User saveUser(User user);
    User findByEmail(String email);
    User findById(Long id);
}
```

### UserServiceImpl.java

```java
package com.example.demo.service.impl;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.entity.User;
import com.example.demo.util.JwtUtil;
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    public UserServiceImpl(UserRepository repo, PasswordEncoder encoder, JwtUtil jwtUtil) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }
    public User saveUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }
    public User findByEmail(String email) {
        return repo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }
    public User findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
```

### PriorityRuleService.java

```java
package com.example.demo.service;
import java.util.List;
import com.example.demo.entity.PriorityRule;
public interface PriorityRuleService {
    int calculatePriority(String category);
    List<PriorityRule> getAllRules();
}
```

### PriorityRuleServiceImpl.java

```java
package com.example.demo.service.impl;
import com.example.demo.repository.PriorityRuleRepository;
import com.example.demo.service.PriorityRuleService;
import com.example.demo.entity.PriorityRule;
import java.util.List;
public class PriorityRuleServiceImpl implements PriorityRuleService {
    private final PriorityRuleRepository repo;
    public PriorityRuleServiceImpl(PriorityRuleRepository repo) {
        this.repo = repo;
    }
    public int calculatePriority(String category) {
        return repo.findByCategory(category).map(PriorityRule::getBaseScore).orElse(0);
    }
    public List<PriorityRule> getAllRules() {
        return repo.findAll();
    }
}
```

### ComplaintService.java

```java
package com.example.demo.service;
import com.example.demo.dto.ComplaintRequest;
import com.example.demo.entity.Complaint;
import java.util.List;
public interface ComplaintService {
    Complaint submitComplaint(ComplaintRequest request);
    List<Complaint> getUserComplaints(Long userId);
    List<Complaint> getPrioritizedComplaints();
    void updateComplaintStatus(Long id, String status);
}
```

### ComplaintServiceImpl.java

```java
package com.example.demo.service.impl;
import com.example.demo.repository.ComplaintRepository;
import com.example.demo.service.*;
import com.example.demo.dto.ComplaintRequest;
import com.example.demo.entity.*;
import java.util.List;
public class ComplaintServiceImpl implements ComplaintService {
    private final ComplaintRepository repo;
    private final PriorityRuleService ruleService;
    private final UserService userService;
    public ComplaintServiceImpl(ComplaintRepository repo, PriorityRuleService ruleService, UserService userService) {
        this.repo = repo;
        this.ruleService = ruleService;
        this.userService = userService;
    }
    public Complaint submitComplaint(ComplaintRequest request) {
        User user = userService.findById(request.getUserId());
        Complaint c = new Complaint();
        c.setTitle(request.getTitle());
        c.setDescription(request.getDescription());
        c.setCategory(request.getCategory());
        c.setPriorityScore(ruleService.calculatePriority(request.getCategory()));
        c.setUser(user);
        return repo.save(c);
    }
    public List<Complaint> getUserComplaints(Long userId) {
        return repo.findByUserId(userId);
    }
    public List<Complaint> getPrioritizedComplaints() {
        return repo.findAllOrderByPriorityScoreDescCreatedAtAsc();
    }
    public void updateComplaintStatus(Long id, String status) {}
}
```

### ComplaintStatusService.java

```java
package com.example.demo.service;
import java.util.List;
import com.example.demo.entity.ComplaintStatus;
public interface ComplaintStatusService {
    void updateStatus(Long complaintId, String status);
    List<ComplaintStatus> getStatusHistory(Long complaintId);
}
```

### ComplaintStatusServiceImpl.java

```java
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
```

---

## Controllers

### ComplaintController.java

```java
package com.example.demo.controller;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.ComplaintService;
import com.example.demo.dto.ComplaintRequest;
@RestController
@RequestMapping("/complaints")
public class ComplaintController {
    private final ComplaintService service;
    public ComplaintController(ComplaintService service) { this.service = service; }
    @PostMapping("/submit")
    public Object submit(@RequestBody ComplaintRequest r) { return service.submitComplaint(r); }
    @GetMapping("/user/{id}")
    public Object user(@PathVariable Long id) { return service.getUserComplaints(id); }
    @GetMapping("/prioritized")
    public Object prioritized() { return service.getPrioritizedComplaints(); }
    @PutMapping("/status/{id}")
    public void status(@PathVariable Long id, @RequestParam String status) { service.updateComplaintStatus(id, status); }
}
```

### PriorityRuleController.java

```java
package com.example.demo.controller;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.PriorityRuleService;
@RestController
@RequestMapping("/rules")
public class PriorityRuleController {
    private final PriorityRuleService service;
    public PriorityRuleController(PriorityRuleService service) { this.service = service; }
    @GetMapping("/all")
    public Object all() { return service.getAllRules(); }
}
```

### StatusController.java

```java
package com.example.demo.controller;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.ComplaintStatusService;
@RestController
@RequestMapping("/status")
public class StatusController {
    private final ComplaintStatusService service;
    public StatusController(ComplaintStatusService service) { this.service = service; }
    @GetMapping("/history/{id}")
    public Object history(@PathVariable Long id) { return service.getStatusHistory(id); }
}
```

### AuthController.java

```java
package com.example.demo.controller;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService service;
    public AuthController(UserService service) { this.service = service; }
    @PostMapping("/register")
    public Object register(@RequestBody User u) { return service.saveUser(u); }
    @PostMapping("/login")
    public Object login(@RequestBody User u) { return service.findByEmail(u.getEmail()); }
}
```

---

## Servlet

### SimpleEchoServlet.java

```java
package com.example.demo.servlet;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
@WebServlet(urlPatterns = {"/ping", "/health"})
public class SimpleEchoServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/plain");
        if (req.getRequestURI().contains("ping")) res.getWriter().write("PONG");
        else res.getWriter().write("0K");
    }
}
```
