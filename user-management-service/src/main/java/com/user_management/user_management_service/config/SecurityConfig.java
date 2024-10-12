package com.user_management.user_management_service.config;

import com.user_management.user_management_service.filter.JwtAuthenticationFilter;
import com.user_management.user_management_service.util.errorHandler.ForbiddenErrorHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ForbiddenErrorHandler forbiddenErrorHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider,
            ForbiddenErrorHandler forbiddenErrorHandler
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.forbiddenErrorHandler = forbiddenErrorHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/users/login/**","/users/login/oauth2/**","/users/request-password-reset","/users/reset-password").permitAll() // Allow login
                        .requestMatchers("/users/v3/api-docs", "/users/swagger-ui.html", "/webjars/**", "/users/OAuth2", "/users/{id}/password").permitAll()
                        .requestMatchers("/trainees/**", "/users/**").hasAnyRole("ADMIN", "TRAINER")
                        .anyRequest().authenticated()
                )
                .oauth2Login() // Configure OAuth2 login
                .defaultSuccessUrl("/users/login/oauth2/callback")
                .failureUrl("/login?error=true")
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(forbiddenErrorHandler)
                )
                .authenticationProvider(authenticationProvider);

        return http.build();
    }
}
