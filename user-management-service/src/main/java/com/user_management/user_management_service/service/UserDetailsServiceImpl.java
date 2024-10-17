package com.user_management.user_management_service.service;

import com.user_management.user_management_service.model.User;
import com.user_management.user_management_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${api.gateway.url}")
    private String apiGatewayUrl;

    public UserDetailsServiceImpl(RestTemplate restTemplate, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
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

        return new CustomUserDetails(user);
    }

    public static class CustomUserDetails implements UserDetails {
        private final User user;

        public CustomUserDetails(User user) {
            this.user = user;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return user.getAuthorities();
        }

        @Override
        public String getPassword() {
            return user.getPassword();
        }

        @Override
        public String getUsername() {
            return user.getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return user.getIsActive();
        }

        public Integer getId() {
            return user.getId();
        }
    }

    public Integer getUserIdByEmail(String email) {
        // Use Optional's isPresent() to check if the user exists
        Optional<User> userOpt = userRepository.findByEmail(email); // Adjust based on your repository method
        if (userOpt.isPresent()) {
            return userOpt.get().getId(); // Assuming getId() returns Long
        }
        throw new RuntimeException("User not found with email: " + email);
    }
}
