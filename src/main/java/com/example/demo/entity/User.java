package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password required")
    @Size(min = 6, message = "Min 6 characters")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Complaint> complaints;

    // Getters
public Long getId() {
    return id;
}

public String getName() {
    return name;
}

public String getEmail() {
    return email;
}

public String getPassword() {
    return password;
}

public List<Complaint> getComplaints() {
    return complaints;
}

// Setters
public void setId(Long id) {
    this.id = id;
}

public void setName(String name) {
    this.name = name;
}

public void setEmail(String email) {
    this.email = email;
}

public void setPassword(String password) {
    this.password = password;
}

public void setComplaints(List<Complaint> complaints) {
    this.complaints = complaints;
}

}
