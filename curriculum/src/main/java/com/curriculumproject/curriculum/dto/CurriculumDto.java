package com.curriculumproject.curriculum.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CurriculumDto {

    private String title;
    private String description;
    private Long specializationId;
    private Long cohortId;
    private List<String> learningObjectives;
    private MultipartFile thumbnailImage; // File upload
}
