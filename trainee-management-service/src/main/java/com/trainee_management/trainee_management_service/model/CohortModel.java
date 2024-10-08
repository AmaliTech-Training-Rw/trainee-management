package com.trainee_management.trainee_management_service.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class CohortModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date startDate;
    private Date endDate;
    private String location;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> specializations;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> enrolledTrainees;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getSpecializations() {
        return specializations;
    }

    public void setSpecializations(List<String> specializations) {
        this.specializations = specializations;
    }

    public List<String> getEnrolledTrainees() {
        return enrolledTrainees;
    }

    public void setEnrolledTrainees(List<String> enrolledTrainees) {
        this.enrolledTrainees = enrolledTrainees;
    }
}
