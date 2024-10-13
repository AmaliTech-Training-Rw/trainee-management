package com.curriculumproject.curriculum.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.curriculumproject.curriculum.ResourceNotFoundException;
import com.curriculumproject.curriculum.dto.CurriculumDto;
import com.curriculumproject.curriculum.module.Cohort;
import com.curriculumproject.curriculum.module.Curriculum;
import com.curriculumproject.curriculum.module.Specialization;
import com.curriculumproject.curriculum.repository.CohortRepository;
import com.curriculumproject.curriculum.repository.CurriculumRepository;
import com.curriculumproject.curriculum.repository.SpecializationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class CurriculumService {

    @Autowired
    private CurriculumRepository curriculumRepository;

    @Autowired
    private SpecializationRepository specializationRepository;

    @Autowired
    private CohortRepository cohortRepository;

    @Autowired
    private Cloudinary cloudinary;

    // Create Curriculum
    public Curriculum createCurriculum(CurriculumDto dto) {
        Specialization specialization = specializationRepository.findById(dto.getSpecializationId())
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found with ID: " + dto.getSpecializationId()));

        Cohort cohort = cohortRepository.findById(dto.getCohortId())
                .orElseThrow(() -> new ResourceNotFoundException("Cohort not found with ID: " + dto.getCohortId()));

        // Upload file to Cloudinary and get the URL
        String thumbnailUrl = uploadToCloudinary(dto.getThumbnailImage());

        Curriculum curriculum = new Curriculum();
        curriculum.setTitle(dto.getTitle());
        curriculum.setDescription(dto.getDescription());
        curriculum.setSpecialization(specialization);
        curriculum.setCohort(cohort);
        curriculum.setLearningObjectives(dto.getLearningObjectives());
        curriculum.setThumbnailImage(thumbnailUrl); // Set the Cloudinary URL

        return curriculumRepository.save(curriculum);
    }

    // Get all Curriculums
    public List<Curriculum> getAllCurriculums() {
        return curriculumRepository.findAll();
    }

    // Get a specific Curriculum by ID
    public Curriculum getCurriculumById(Long id) {
        return curriculumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curriculum not found with ID: " + id));
    }

    // Update Curriculum
    public Curriculum updateCurriculum(Long id, CurriculumDto dto) {
        Curriculum curriculum = curriculumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curriculum not found with ID: " + id));

        Specialization specialization = specializationRepository.findById(dto.getSpecializationId())
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found with ID: " + dto.getSpecializationId()));

        Cohort cohort = cohortRepository.findById(dto.getCohortId())
                .orElseThrow(() -> new ResourceNotFoundException("Cohort not found with ID: " + dto.getCohortId()));

        // Check if a new file was uploaded; if yes, upload to Cloudinary
        if (dto.getThumbnailImage() != null && !dto.getThumbnailImage().isEmpty()) {
            String thumbnailUrl = uploadToCloudinary(dto.getThumbnailImage());
            curriculum.setThumbnailImage(thumbnailUrl); // Update Cloudinary URL if a new file is uploaded
        }

        curriculum.setTitle(dto.getTitle());
        curriculum.setDescription(dto.getDescription());
        curriculum.setSpecialization(specialization);
        curriculum.setCohort(cohort);
        curriculum.setLearningObjectives(dto.getLearningObjectives());

        return curriculumRepository.save(curriculum);
    }

    // Delete Curriculum
    public void deleteCurriculum(Long id) {
        Curriculum curriculum = curriculumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curriculum not found with ID: " + id));
        curriculumRepository.delete(curriculum);
    }

    // Upload file to Cloudinary
    private String uploadToCloudinary(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("url").toString(); // Get and return the URL of the uploaded file
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to Cloudinary", e);
        }
    }
}
