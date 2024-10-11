package com.springemail.email.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")  // Specify the table name
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use IDENTITY for auto-increment
    private Long id;  // Changed to Long

    private String name;
    private String email;
    private String password;
    private Boolean isActive = true;
    private String resetToken;  // Retained the resetToken field

    // Default constructor for JPA
    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", isActive=" + isActive +
                ", resetToken='" + resetToken + '\'' +  // Include resetToken in toString
                '}';
    }
}
