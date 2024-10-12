package com.user_management.user_management_service.service;

import com.user_management.user_management_service.dto.AuthResponse;
import com.user_management.user_management_service.dto.LoginRequest;
import com.user_management.user_management_service.model.User;
import com.user_management.user_management_service.repository.UserRepository;
import com.user_management.user_management_service.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

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
        return jwtUtil.generateJwtToken(authentication);  // Replace with your actual token generation method
    }
}
