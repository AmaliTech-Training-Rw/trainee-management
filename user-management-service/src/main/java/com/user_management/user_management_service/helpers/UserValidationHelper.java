package com.user_management.user_management_service.helpers;

import com.user_management.user_management_service.dto.UserRequest;
import com.user_management.user_management_service.exception.UserAlreadyExistsException;
import com.user_management.user_management_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserValidationHelper {

    private final UserRepository userRepository;

    @Autowired
    public UserValidationHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateUserInfo(UserRequest userInfo) {
        if (userRepository.existsByEmail(userInfo.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists.");
        }
    }
}
