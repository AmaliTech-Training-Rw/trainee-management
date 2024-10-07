package com.kafka_producer.kafka_producer.controller;

import com.kafka_producer.kafka_producer.service.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageProducer messageProducer;

    @Autowired
    public MessageController(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    @PostMapping
    public String sendMessage(@RequestParam String topic, @RequestParam String message) {
        messageProducer.sendMessage(topic, message);
        return "Message sent to Kafka topic: " + topic;
    }
}
