package com.user_management.user_management_service.service;

import com.user_management.user_management_service.dto.ActivationEmailRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final RestTemplate restTemplate;

    @Value("${email.service.url}")
    private String emailServiceUrl;

    public EmailService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendActivationEmail(String email, String resetToken) {
        if (email == null || email.isEmpty()) {
            logger.error("Failed to send activation email: Email is empty or null");
            throw new IllegalArgumentException("Email cannot be empty or null");
        }
        if (resetToken == null || resetToken.isEmpty()) {
            logger.error("Failed to send activation email: Reset token is empty or null");
            throw new IllegalArgumentException("Reset token cannot be empty or null");
        }

        ActivationEmailRequest activationEmailRequest = new ActivationEmailRequest(email, resetToken);
        logger.info("Sending activation email with request: {}", activationEmailRequest);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

            HttpEntity<ActivationEmailRequest> requestEntity = new HttpEntity<>(activationEmailRequest, headers);
            ResponseEntity<Void> response = restTemplate.exchange(emailServiceUrl, HttpMethod.POST, requestEntity, Void.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Activation email successfully sent to: {}", email);
            } else {
                logger.warn("Email service responded with a non-success status code: {}", response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            logger.error("Client error while sending activation email: {}", e.getStatusCode(), e);
        } catch (Exception e) {
            logger.error("Failed to send activation email due to an unexpected error: {}", e.getMessage(), e);
        }
    }}
