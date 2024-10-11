package com.user_management.user_management_service.service;

import com.user_management.user_management_service.dto.UserRequest;
import com.user_management.user_management_service.exception.UserAlreadyExistsException;
import com.user_management.user_management_service.exception.UserNotFoundException;
import com.user_management.user_management_service.model.User;
import com.user_management.user_management_service.repository.UserRepository;
import com.user_management.user_management_service.helpers.EmailHelper; // Import EmailHelper
import com.user_management.user_management_service.helpers.UserValidationHelper;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate; // Import KafkaTemplate
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserValidationHelper userValidationHelper;

    @Autowired
    private EmailHelper emailHelper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic.user-created}")
    private String userCreatedTopic;

    public User addUser(UserRequest userInfo) {
        try {
            userValidationHelper.validateUserInfo(userInfo);

            // Check if user already exists
            if (userRepository.existsByEmail(userInfo.getEmail())) {
                throw new UserAlreadyExistsException("User already exists with email: " + userInfo.getEmail());
            }

            User user = new User();
            user.setName(userInfo.getName());
            user.setEmail(userInfo.getEmail());
            user.setPassword(passwordEncoder.encode(userInfo.getPassword())); // Correctly encode and set the password
            user.setRole(userInfo.getRole());

            // Generate a reset token and set the expiration
            String resetToken = UUID.randomUUID().toString();
            user.setResetToken(resetToken);
            user.setTokenExpiration(LocalDateTime.now().plusHours(1)); // Token valid for 1 hour

            // Save user to database
            User savedUser = userRepository.save(user);

            JSONObject messageJson = new JSONObject();
            messageJson.put("email", savedUser.getEmail());
            messageJson.put("name", savedUser.getName());
            messageJson.put("token", resetToken);

            kafkaTemplate.send(userCreatedTopic, messageJson.toString());

            logger.info("User created: {}", savedUser.getEmail());
            logger.info("User created: {}", savedUser.getEmail());
            return savedUser;
        } catch (UserAlreadyExistsException e) {
            logger.error("Error adding user: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error adding user: {}", e.getMessage());
            throw new RuntimeException("An unexpected error occurred while adding the user.", e);
        }
    }

    public Optional<User> updateUser(int id, UserRequest userInfo) {
        try {
            User user = getUserByIdOrThrow(id);
            updateUserDetails(user, userInfo);
            logger.info("Updating user: {}", user.getEmail());
            return Optional.of(userRepository.save(user));
        } catch (UserNotFoundException e) {
            logger.error("Error updating user: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error updating user: {}", e.getMessage());
            throw new RuntimeException("An unexpected error occurred while updating the user.", e);
        }
    }

    public void deleteUser(int id) {
        try {
            User user = getUserByIdOrThrow(id);
            logger.info("Deleting user: {}", user.getEmail());
            userRepository.delete(user);
        } catch (UserNotFoundException e) {
            logger.error("Error deleting user: {}", e.getMessage());
            throw e; // Rethrow the exception for the controller to handle
        } catch (Exception e) {
            logger.error("Unexpected error deleting user: {}", e.getMessage());
            throw new RuntimeException("An unexpected error occurred while deleting the user.", e);
        }
    }

    public User getUserById(int id) {
        return getUserByIdOrThrow(id);
    }

    public List<User> getAllUsers() {
        logger.info("Retrieving all users");
        return userRepository.findAll();
    }

    // Private helper methods

    private void updateUserDetails(User user, UserRequest userInfo) {
        user.setName(userInfo.getName());
        user.setEmail(userInfo.getEmail());
        if (userInfo.getPassword() != null && !userInfo.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        }
        user.setRole(userInfo.getRole());
    }

    private User getUserByIdOrThrow(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    // New method to set the user's password using the reset token
    public boolean setPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token);
        if (user != null && user.getTokenExpiration().isAfter(LocalDateTime.now())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setIsActive(true);
            user.setResetToken(null);
            user.setTokenExpiration(null);
            userRepository.save(user);
            logger.info("Password set successfully for user: {}", user.getEmail());
            return true;
        }
        logger.warn("Failed to set password. Invalid or expired token: {}", token);
        return false;
    }
}
