package com.trainee_management.trainee_management_service.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageResponseDTO {
    private String correlationId;
    private String imageUrl;
    private String errorMessage;

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert ImageResponseDTO to JSON string", e);
        }
    }

    public static ImageResponseDTO fromString(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, ImageResponseDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON string to ImageResponseDTO", e);
        }
    }

}
