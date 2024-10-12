package com.kafka_consumer.kafka_consumer.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {

    @KafkaListener(topics = "topic1", groupId = "group_id")
    public void consume(String message) {
        System.out.println("Received message: " + message);
    }
}
