package com.assessment_management.assessment_management_service.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class TraineeConsumer {

    // Use a thread-safe list to store available trainee IDs
    private final List<Long> availableTraineeIds = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper

    @KafkaListener(topics = "${kafka.topic.available.trainees}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(String message) {
        try {
            // Parse the incoming JSON message
            JsonNode jsonNode = objectMapper.readTree(message);
            List<Long> traineeIds = objectMapper.convertValue(jsonNode.get("traineeIds"), new TypeReference<List<Long>>() {});

            // Update the list of available trainee IDs
            updateAvailableTrainees(traineeIds);
            System.out.println("Received available trainees: " + availableTraineeIds);
        } catch (JsonProcessingException e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }

    private void updateAvailableTrainees(List<Long> traineeIds) {
        availableTraineeIds.clear(); // Clear previous IDs
        availableTraineeIds.addAll(traineeIds); // Update with new available IDs
    }

    public List<Long> getAvailableTraineeIds() {
        return new ArrayList<>(availableTraineeIds);
    }
}
