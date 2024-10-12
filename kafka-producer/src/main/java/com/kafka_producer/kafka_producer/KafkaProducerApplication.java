package com.kafka_producer.kafka_producer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
@EnableEurekaServer
public class KafkaProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaProducerApplication.class, args);
	}
	@Bean
     CommandLineRunner commandLineRunner(KafkaTemplate<String, String> kafkaTemplate) {
		return args -> {
			kafkaTemplate.send("topic1", "Hello World :)");
		};
}
}
