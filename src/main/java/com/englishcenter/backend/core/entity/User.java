package com.englishcenter.backend.core.entity;

import jakarta.persistence.*; 
import java.time.LocalDateTime;

@Entity
@Table(name = "users", schema = "dbo")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @Column(name = "full_name", nullable = false, length = 255)
   private String fullname;

   @Column(name = "email", nullable = false, unique = true, length = 255)
   private String email; 
   
   @Column(name = "phone", length = 20)
   private String phone;
   
   @Column(name = "address_user", length = 255)
   private String address;
   
   @Column(name = "password_hash", nullable = false, length = 255)
   private String passwordHash;
   
   @Column(name = "role", nullable = false, length = 20)
    private String role;
   
    @Column(name = "zalo_id", length = 50)
    private String zaloId;
   
    @Column(name = "facebook_id", length = 50)
    private String facebookId;
   
    @Column(name = "is_active")
    private boolean isActive;
   
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "batch", length = 50)
    private String batch;

    @Column(name = "academic_status", length = 50)
    private String academicStatus;




    // Constructors, getters, and setters

    public User() {
    }

    public User(String fullname, String email, String phone, String address, String passwordHash, String role, String zaloId, String facebookId, boolean isActive) {
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.passwordHash = passwordHash;
        this.role = role;
        this.zaloId = zaloId;
        this.facebookId = facebookId;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }
    
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getZaloId() {
        return zaloId;
    }

    public void setZaloId(String zaloId) {
        this.zaloId = zaloId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getAcademicStatus() {
        return academicStatus;
    }

    public void setAcademicStatus(String academicStatus) {
        this.academicStatus = academicStatus;
    }
}