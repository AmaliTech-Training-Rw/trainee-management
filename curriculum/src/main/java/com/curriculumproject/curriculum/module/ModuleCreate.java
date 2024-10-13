package com.curriculumproject.curriculum.module;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "modules")
public class ModuleCreate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String topics;  // Store topics as JSON string

    @Column(name = "file_url")
    private String fileUrl;
}
