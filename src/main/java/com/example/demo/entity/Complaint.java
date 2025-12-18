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
