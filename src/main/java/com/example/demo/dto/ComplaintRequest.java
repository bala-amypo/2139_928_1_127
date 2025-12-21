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