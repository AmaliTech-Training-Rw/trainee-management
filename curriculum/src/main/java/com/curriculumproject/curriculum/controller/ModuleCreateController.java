package com.curriculumproject.curriculum.controller;

import com.curriculumproject.curriculum.module.ModuleCreate;
import com.curriculumproject.curriculum.service.ModuleCreateService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/modules")
public class ModuleCreateController {

    @Autowired
    private ModuleCreateService moduleCreateService;

    // Create a new module
    @PostMapping("/create")
    public ResponseEntity<?> createModule(@RequestParam("title") String title,
                                          @RequestParam("description") String description,
                                          @RequestParam("topics") String topics,  // Expecting JSON string
                                          @RequestParam("file") MultipartFile file) {
        try {
            // Convert the topics JSON string to a List
            List<String> topicList = new ObjectMapper().readValue(topics, List.class);

            // Call the service to create the module
            ModuleCreate module = moduleCreateService.createModule(title, description, topicList, file);
            return ResponseEntity.ok(module);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error creating module");
        }
    }

    // Retrieve all modules
    @GetMapping
    public ResponseEntity<List<ModuleCreate>> getAllModules() {
        List<ModuleCreate> modules = moduleCreateService.getAllModules();
        return ResponseEntity.ok(modules);
    }

    // Retrieve a module by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getModuleById(@PathVariable Long id) {
        Optional<ModuleCreate> module = moduleCreateService.getModuleById(id);
        if (module.isPresent()) {
            return ResponseEntity.ok(module.get());
        } else {
            return ResponseEntity.status(404).body("Module not found");
        }
    }

    // Update a module by ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateModule(@PathVariable Long id,
                                          @RequestParam("title") String title,
                                          @RequestParam("description") String description,
                                          @RequestParam("topics") String topics,  // Expecting JSON string
                                          @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            // Convert the topics JSON string to a List
            List<String> topicList = new ObjectMapper().readValue(topics, List.class);

            // Call the service to update the module
            ModuleCreate updatedModule = moduleCreateService.updateModule(id, title, description, topicList, file);
            return ResponseEntity.ok(updatedModule);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error updating module");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // Delete a module by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteModule(@PathVariable Long id) {
        boolean isDeleted = moduleCreateService.deleteModule(id);
        if (isDeleted) {
            return ResponseEntity.ok("Module deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Module not found");
        }
    }
}
