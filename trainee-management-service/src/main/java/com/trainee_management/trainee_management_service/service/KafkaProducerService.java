package com.trainee_management.trainee_management_service.service;

import com.trainee_management.trainee_management_service.dto.ImageMessageDTO;
import com.trainee_management.trainee_management_service.dto.ImageResponseDTO;
import com.trainee_management.trainee_management_service.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "file-upload";
    private static final String RESPONSE_TOPIC = "file-upload-response";

    private final ConcurrentHashMap<String, CompletableFuture<String>> pendingRequests = new ConcurrentHashMap<>();

    public CompletableFuture<String> sendImageMessage(MultipartFile file) throws IOException {
        String correlationId = UUID.randomUUID().toString();

        ImageMessageDTO message = new ImageMessageDTO();
        message.setImageContent(FileUtils.encodeFileToBase64(file));
        message.setFileName(file.getOriginalFilename());
        message.setContentType(file.getContentType());
        message.setCorrelationId(correlationId);

        CompletableFuture<String> future = new CompletableFuture<>();
        pendingRequests.put(correlationId, future);

        kafkaTemplate.send(TOPIC, message.toString());

        return future;
    }

    @KafkaListener(topics = RESPONSE_TOPIC, groupId = "file-upload-group")
    public void listenResponse(String responseString) {
        ImageResponseDTO response = ImageResponseDTO.fromString(responseString);
        CompletableFuture<String> future = pendingRequests.remove(response.getCorrelationId());
        if (future != null) {
            if (response.getErrorMessage() == null) {
                future.complete(response.getImageUrl());
            } else {
                future.completeExceptionally(new RuntimeException(response.getErrorMessage()));
            }
        }
    }
}