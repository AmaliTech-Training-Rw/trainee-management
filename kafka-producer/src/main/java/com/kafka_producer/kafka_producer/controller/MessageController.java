package com.kafka_producer.kafka_producer.controller;

import com.kafka_producer.kafka_producer.dto.MessageRequest;
import com.kafka_producer.kafka_producer.service.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageProducer messageProducer;

    @Autowired
    public MessageController(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam String topic, @RequestBody String message) {
        messageProducer.sendMessage(topic, message);
        return "Message sent to topic " + topic;
    }
}



