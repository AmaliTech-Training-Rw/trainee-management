package com.user_management.user_management_service.service;

import com.user_management.user_management_service.model.User; // Assuming you have a User model
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final RestTemplate restTemplate;

    // The URL for the API Gateway
    @Value("${api.gateway.url}")
    private String apiGatewayUrl;

    public UserDetailsServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String url = apiGatewayUrl + "/users/users?email=" + email;
        User user;

        try {
            user = restTemplate.getForObject(url, User.class);
        } catch (RestClientException e) {
            throw new UsernameNotFoundException("Error fetching user from the API Gateway: " + e.getMessage());
        }

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getAuthorities()
        );
    }
}
