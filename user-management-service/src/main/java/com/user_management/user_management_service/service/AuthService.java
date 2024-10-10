package com.user_management.user_management_service.service;

import com.user_management.user_management_service.dto.LoginRequest;
import com.user_management.user_management_service.model.User;
import com.user_management.user_management_service.repository.UserRepository;
import com.user_management.user_management_service.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private UserRepository userRepository;

    @Autowired
    public AuthService(JwtUtil jwtUtil,
                       AuthenticationManager authenticationManager,
                       UserDetailsService userDetailsService,
                       UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    // User Login with Email and Password

    public String authenticate(LoginRequest input) throws Exception {
        Authentication authentication;
        try {
            // Log the attempt to authenticate the user
            logger.info("Attempting to authenticate user with email: {}", input.getEmail());

            // Authenticate the user
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
            );

            // Log successful authentication
            logger.info("User authenticated successfully: {}", input.getEmail());
        } catch (BadCredentialsException e) {
            logger.error("Authentication failed: Invalid credentials for email: {}", input.getEmail());
            throw new RuntimeException("Authentication failed: Invalid credentials");
        } catch (Exception exception) {
            logger.error("Authentication failed: {}", exception.getMessage());
            throw new RuntimeException("Authentication failed: " + exception.getMessage());
        }

        // Generate and return JWT token using the authenticated user
        String jwtToken = jwtUtil.generateJwtToken(authentication);

        // Log the generated JWT token
        if (jwtToken != null) {
            logger.info("Generated JWT token for email: {}", input.getEmail());
        } else {
            logger.error("Failed to generate JWT token for email: {}", input.getEmail());
        }

        return jwtToken;
    }




    // Uncomment and implement Google authentication logic if needed
    // 2. Google Authentication Logic
    // Implementing Google authentication is typically done through the OAuth2 process,
    // but for simplicity, you might just fetch user details from the OAuth2 provider and return a JWT.

    // 3. Password Reset - Send OTP
    // public void sendOtpForPasswordReset(String email) {
    //     Optional<User> userOptional = userRepository.findByEmail(email);
    //     if (userOptional.isPresent()) {
    //         String otp = otpService.generateOtp(email); // Generate and send OTP
    //         // Send OTP to user via email (implement email sending logic)
    //     } else {
    //         throw new RuntimeException("User not found.");
    //     }
    // }

    // 4. Validate OTP
    // public boolean validateOtp(String email, String otp) {
    //     return otpService.validateOtp(email, otp);
    // }

}
