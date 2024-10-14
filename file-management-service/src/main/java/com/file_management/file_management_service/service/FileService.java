package com.file_management.file_management_service.service;

import com.file_management.file_management_service.dto.ImageMessageDTO;
import com.file_management.file_management_service.dto.ImageResponseDTO;
import com.file_management.file_management_service.utils.FileUtils;
import org.apache.http.client.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    private final CloudinaryService cloudinaryService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);


    @Autowired
    public FileService(CloudinaryService cloudinaryService, KafkaTemplate<String, String> kafkaTemplate) {
        this.cloudinaryService = cloudinaryService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "file-upload", groupId = "file-upload-group")
    public void listen(String stringMessage) {
        try {

            ImageMessageDTO message = ImageMessageDTO.fromString(stringMessage);

            // Convert Base64 to MultipartFile
            MultipartFile file = FileUtils.decodeBase64ToMultipartFile(
                    message.getImageContent(),
                    message.getFileName(),
                    message.getContentType()
            );

            // Upload to Cloudinary
            String imageUrl = cloudinaryService.uploadFile(file);

            System.out.println("image uploaded "+ imageUrl);

            // Create response message
            ImageResponseDTO response = new ImageResponseDTO();
            response.setCorrelationId(message.getCorrelationId());
            response.setImageUrl(imageUrl);

            // Send response message to Kafka
            kafkaTemplate.send("file-upload-response", response.toString());
        } catch (Exception e) {
            ImageMessageDTO message = ImageMessageDTO.fromString(stringMessage);
            // Handle exception and send error response if needed
            ImageResponseDTO response = new ImageResponseDTO();
            response.setCorrelationId(message.getCorrelationId());
            response.setErrorMessage("Error: " + e.getMessage());
            kafkaTemplate.send("file-upload-response", response.toString());
        }
    }

    public String uploadFile(MultipartFile file) throws Exception {
        try {
            return cloudinaryService.uploadFile(file);
        }catch (Exception e){
            logger.error("error uploading image: {}", e.getMessage());
            throw e;
        }
    }
}