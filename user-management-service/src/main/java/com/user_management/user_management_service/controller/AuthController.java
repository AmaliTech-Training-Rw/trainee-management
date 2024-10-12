package com.user_management.user_management_service.controller;

import com.user_management.user_management_service.dto.LoginRequest;
import com.user_management.user_management_service.dto.ResponseMessage;
import com.user_management.user_management_service.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Authentication

    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login(@RequestBody LoginRequest loginRequest) {
        try {
            String jwtToken = String.valueOf(authService.authenticate(loginRequest));
            return ResponseEntity.ok(new ResponseMessage("Login successful", jwtToken));
        } catch (RuntimeException e) {
            return handleError(e);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseMessage("Internal Server Error", null));
        }
    }
    @GetMapping("/login/oauth2")
    public String loginWithGoogle() {
        // This method triggers the Google OAuth2 authorization flow
        return "redirect:/oauth2/authorization/google"; // Redirect to Google OAuth2 authorization endpoint
    }
    // Combined OAuth2 login initiation and callback
    @GetMapping("/login/oauth2/callback")
    public ResponseEntity<ResponseMessage> oauth2Callback(@AuthenticationPrincipal OAuth2User principal) {
        // Get user information from the principal
        String email = principal.getAttribute("email");

        // Process the login and generate a JWT token
        try {
            String jwtToken = String.valueOf(authService.oauth2Login(email));
            return ResponseEntity.ok(new ResponseMessage("OAuth2 login successful", jwtToken));
        } catch (RuntimeException e) {
            return handleError(e);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseMessage("Internal Server Error", null));
        }
    }

    private ResponseEntity<ResponseMessage> handleError(RuntimeException e) {
        return ResponseEntity.badRequest().body(new ResponseMessage(e.getMessage(), null));
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        try {
            authService.processPasswordResetRequest(email);
            return ResponseEntity.ok("Password reset request has been sent to your email.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String otp,
                                                @RequestParam String newPassword) {
        try {
            String confirmationMessage = authService.updatePassword(otp, newPassword);
            return ResponseEntity.ok(confirmationMessage);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
