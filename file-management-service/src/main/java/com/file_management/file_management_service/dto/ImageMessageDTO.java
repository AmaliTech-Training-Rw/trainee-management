package com.file_management.file_management_service.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageMessageDTO {
    private String imageContent;
    private String fileName;
    private String contentType;
    private String correlationId;

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert ImageMessageDTO to JSON string", e);
        }
    }

    public static ImageMessageDTO fromString(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, ImageMessageDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON string to ImageMessageDTO", e);
        }
    }
}