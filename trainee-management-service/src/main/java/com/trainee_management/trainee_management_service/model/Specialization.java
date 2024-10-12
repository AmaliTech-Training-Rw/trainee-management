package com.trainee_management.trainee_management_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Specialization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private String description;

    @OneToMany(mappedBy = "specialization", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Trainee> trainees;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Trainee> getTrainees() { return trainees; }
    public void setTrainees(List<Trainee> trainees) { this.trainees = trainees; }
}
