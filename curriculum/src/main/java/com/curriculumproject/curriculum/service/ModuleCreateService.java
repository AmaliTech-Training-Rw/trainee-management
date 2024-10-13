package com.curriculumproject.curriculum.service;

import com.curriculumproject.curriculum.module.ModuleCreate;
import com.curriculumproject.curriculum.repository.ModuleCreateRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ModuleCreateService {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ModuleCreateRepository moduleCreateRepository;

    // Create a new module
    public ModuleCreate createModule(String title, String description, List<String> topics, MultipartFile file) throws IOException {
        // Upload the file to Cloudinary and get the URL
        Map<?, ?> uploadResult = cloudinaryService.uploadFile(file);
        String fileUrl = uploadResult.get("url").toString();

        // Convert topics list to JSON string
        String topicsJson = new ObjectMapper().writeValueAsString(topics);

        // Create new module entity and set the properties
        ModuleCreate module = new ModuleCreate();
        module.setTitle(title);
        module.setDescription(description);
        module.setTopics(topicsJson);
        module.setFileUrl(fileUrl);

        // Save the module to the database
        return moduleCreateRepository.save(module);
    }

    // Retrieve all modules
    public List<ModuleCreate> getAllModules() {
        return moduleCreateRepository.findAll();
    }

    // Retrieve a module by ID
    public Optional<ModuleCreate> getModuleById(Long id) {
        return moduleCreateRepository.findById(id);
    }

    // Update a module
    public ModuleCreate updateModule(Long id, String title, String description, List<String> topics, MultipartFile file) throws IOException {
        // Check if the module exists
        Optional<ModuleCreate> existingModuleOpt = moduleCreateRepository.findById(id);
        if (!existingModuleOpt.isPresent()) {
            throw new IllegalArgumentException("Module not found");
        }

        // Get the existing module
        ModuleCreate existingModule = existingModuleOpt.get();

        // Update module details
        existingModule.setTitle(title);
        existingModule.setDescription(description);

        // Convert topics list to JSON string
        String topicsJson = new ObjectMapper().writeValueAsString(topics);
        existingModule.setTopics(topicsJson);

        // If a new file is uploaded, update the file URL
        if (file != null && !file.isEmpty()) {
            Map<?, ?> uploadResult = cloudinaryService.uploadFile(file);
            String fileUrl = uploadResult.get("url").toString();
            existingModule.setFileUrl(fileUrl);
        }

        // Save updated module
        return moduleCreateRepository.save(existingModule);
    }

    // Delete a module by ID
    public boolean deleteModule(Long id) {
        Optional<ModuleCreate> moduleOpt = moduleCreateRepository.findById(id);
        if (moduleOpt.isPresent()) {
            moduleCreateRepository.delete(moduleOpt.get());
            return true;
        }
        return false;
    }
}
