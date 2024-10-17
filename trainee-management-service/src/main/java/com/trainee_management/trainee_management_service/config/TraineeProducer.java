package com.trainee_management.trainee_management_service.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TraineeProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper; // Jackson ObjectMapper

    @Value("${kafka.topic.available.trainees}")
    private String availableTraineesTopic;

    public TraineeProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendAvailableTrainees(List<Long> traineeIds) {
        try {
            // Convert List<Long> to JSON string
            String messageJson = objectMapper.writeValueAsString(traineeIds);

            // Send the JSON string to the Kafka topic
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(availableTraineesTopic, messageJson);

            // Handle success and failure
            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    // Handle the failure
                    System.err.println("Failed to send available trainees: " + ex.getMessage());
                } else {
                    // Handle the success
                    System.out.println("Sent available trainees: " + messageJson + " with offset: " + result.getRecordMetadata().offset());
                }
            });
        } catch (JsonProcessingException e) {
            System.err.println("Error converting trainee IDs to JSON: " + e.getMessage());
        }
    }
}
