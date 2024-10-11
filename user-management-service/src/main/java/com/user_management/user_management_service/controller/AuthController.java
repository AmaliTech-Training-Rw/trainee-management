package com.user_management.user_management_service.controller;

import com.user_management.user_management_service.dto.LoginRequest;
import com.user_management.user_management_service.dto.ResponseMessage;
import com.user_management.user_management_service.service.AuthService;
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

    // Uncomment and implement these endpoints as needed
    // @PostMapping("/reset-password")
    // public ResponseEntity<ResponseMessage> resetPassword(@RequestBody String email) { ... }

    // @PutMapping("/update-password")
    // public ResponseEntity<ResponseMessage> updatePassword(@RequestParam String email, @RequestParam String newPassword) { ... }
}
