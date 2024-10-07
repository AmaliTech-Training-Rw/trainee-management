package com.kafka_consumer.kafka_consumer.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {

    @KafkaListener(topics = "your-topic-name", groupId = "consumer-group")
    public void listen(String message) {
        System.out.println("Received Message: " + message);
    }
}
