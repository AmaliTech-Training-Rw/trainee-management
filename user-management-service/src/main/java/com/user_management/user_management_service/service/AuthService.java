package com.user_management.user_management_service.service;

import com.user_management.user_management_service.dto.AuthResponse;
import com.user_management.user_management_service.dto.LoginRequest;
import com.user_management.user_management_service.model.User;
import com.user_management.user_management_service.repository.UserRepository;
import com.user_management.user_management_service.utils.JwtUtil;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final OTPService otpService;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${kafka.topic.password-reset}")
    private String passwordResetTopic;


    @Autowired
    public AuthService(JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager,
                       UserDetailsService userDetailsService,
                       UserRepository userRepository,
                       OTPService otpService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.otpService=otpService;
    }

    public AuthResponse authenticate(LoginRequest input) {
        User user = getUserIfActive(input.getEmail());
        Authentication authentication = authenticateUser(input.getEmail(), input.getPassword());

        String jwtToken = generateToken(authentication);
        String userRole = String.valueOf(user.getRole()); // Assume getRole() returns the user's role

        return new AuthResponse(jwtToken, userRole);
    }

    // Similarly, update oauth2Login method
    public AuthResponse oauth2Login(String email) {
        User user = getUserIfActive(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, user.getAuthorities());

        String jwtToken = generateToken(authentication);
        String userRole = String.valueOf(user.getRole()); // Assume getRole() returns the user's role

        return new AuthResponse(jwtToken, userRole);
    }

    // Retrieve active user by email or throw exception
    private User getUserIfActive(String email) {
        return userRepository.findByEmail(email)
                .filter(User::isEnabled)
                .orElseThrow(() -> new RuntimeException("User account is inactive or does not exist. Please contact the admin."));
    }

    // Authenticate user with credentials and handle exceptions
    private Authentication authenticateUser(String email, String password) {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

    // Generate JWT token using your custom method
    private String generateToken(Authentication authentication) {
        return jwtUtil.generateJwtToken(authentication);
    }

    public void processPasswordResetRequest(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found");
        }

        User savedUser = optionalUser.get();

        // Generate OTP
        String otp = otpService.generateOTP(savedUser.getId());

        // Prepare the message to send to Kafka
        JSONObject messageJson = new JSONObject();
        messageJson.put("userId", savedUser.getId());
        messageJson.put("email", savedUser.getEmail());
        messageJson.put("name", savedUser.getName());
        messageJson.put("otp", otp);

        // Send the message to Kafka
        kafkaTemplate.send(passwordResetTopic, messageJson.toString());

    }

    public String updatePassword(String newPassword) {
        Integer userId = otpService.getVerifiedUserId();
        if (userId == null) {
            throw new RuntimeException("No user is verified for password update.");
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found.");
        }

        User user = optionalUser.get();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);

        // Optionally clear the verified user ID after use
        otpService.clearVerifiedUserId();

        return "Your password has been updated successfully.";
    }



}


