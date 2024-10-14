package com.user_management.user_management_service.helpers;

import com.user_management.user_management_service.dto.UserRequest;
import com.user_management.user_management_service.exception.UserAlreadyExistsException;
import com.user_management.user_management_service.model.User;
import com.user_management.user_management_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserValidationHelper {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserValidationHelper(UserRepository userRepository,
                                AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    public void validateUserInfo(UserRequest userInfo) {
        if (userRepository.existsByEmail(userInfo.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists.");
        }
    }

    private Authentication authenticateUser(String email, String password) {
        try {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid credentials");
        }
    }
    private User findActiveUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .filter(User::isEnabled)  // Check if user is active
                .orElseThrow(() -> new RuntimeException("User account is inactive or not found. Please contact the admin."));
    }


}
