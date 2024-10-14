package com.springemail.email.controller;

import com.springemail.email.repository.UserRepository;
import com.springemail.email.model.User;
import com.springemail.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;
// Other imports...

@RestController
@RequestMapping("/email") // Base URL for user-related endpoints
public class UserController {


    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // Kafka template for sending messages

    private static final String TOPIC = "${kafka.topic.user-created}";

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository; // Assuming you have a repository to handle User persistence

    @PostMapping("/sendActivationEmail")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            // Prepare the message to be sent to Kafka
            String message = String.format("{\"email\":\"%s\", \"name\":\"%s\", \"token\":\"%s\"}",
                    user.getEmail(), user.getName(), user.getResetToken());

            // Send the message to Kafka
            kafkaTemplate.send("${kafka.topic.user-created}", message);

            return ResponseEntity.ok("Email sent successfully!");
        } catch (Exception e) {
            logger.error("Error registering user: {}", e.getMessage());
            return ResponseEntity.status(500).body("User registration failed: " + e.getMessage());
        }
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email) {
        // Create a JSON message for Kafka with the provided email
        String message = String.format("{\"email\":\"%s\"}", email);

        try {
            kafkaTemplate.send("${kafka.topic.password-reset}", message);
            return ResponseEntity.ok("Password reset request has been processed. Check your email.");
        } catch (Exception e) {
            System.err.println("Error sending message to Kafka: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the password reset request. Please try again later.");
        }
    }



    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam String token, @RequestParam String newPassword) {
        // Find user by reset token
        Optional<User> userOptional = userRepository.findByResetToken(token);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Update the password (make sure to hash the password before saving)
            user.setPassword(newPassword);
            user.setResetToken(null); // Clear the reset token after successful reset
            userRepository.save(user);

            // Send confirmation email
            emailService.sendPasswordResetConfirmationEmail(user.getEmail());

            return ResponseEntity.ok("Password has been reset successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid reset token.");
        }
    }



}




