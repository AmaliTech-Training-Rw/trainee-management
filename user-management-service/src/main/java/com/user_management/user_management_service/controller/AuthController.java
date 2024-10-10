package com.user_management.user_management_service.controller;

import com.user_management.user_management_service.dto.LoginRequest;
import com.user_management.user_management_service.dto.ResponseMessage;
import com.user_management.user_management_service.service.AuthService;
import com.user_management.user_management_service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Endpoint for user login
    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Received login request for email: {}", loginRequest.getEmail());

        try {
            String jwtToken = authService.authenticate(loginRequest);
            return ResponseEntity.ok(new ResponseMessage("Login successful", jwtToken));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage(), null));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


//    // Endpoint for resetting password
//    @PostMapping("/reset-password")
//    public ResponseEntity<ResponseMessage> resetPassword(@RequestBody String email) {
//        try {
//            authService.sendOtpForPasswordReset(email); // Implement this method in AuthService
//            return ResponseEntity.ok(new ResponseMessage("OTP sent to email", null));
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage(), null));
//        }
//    }
//
//    // Endpoint to update password
//    @PutMapping("/update-password")
//    public ResponseEntity<ResponseMessage> updatePassword(@RequestParam String email, @RequestParam String newPassword) {
//        try {
//            authService.updatePassword(email, newPassword);
//            return ResponseEntity.ok(new ResponseMessage("Password updated successfully", null));
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage(), null));
//        }
//    }
//
//    // You can add more endpoints for Google authentication and OTP validation as needed
}
