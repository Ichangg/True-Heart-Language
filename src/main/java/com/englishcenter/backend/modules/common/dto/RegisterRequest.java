package com.englishcenter.backend.modules.common.dto;

public class RegisterRequest {
    private String fullname;
    private String email;
    private String password;
    private String role;

    // Getter và Setter cho fullname
    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    // Getter và Setter cho email
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // Getter và Setter cho password
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // Getter và Setter cho role
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}