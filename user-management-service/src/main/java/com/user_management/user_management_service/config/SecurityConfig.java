package com.user_management.user_management_service.config;

import com.user_management.user_management_service.filter.JwtAuthenticationFilter;
import com.user_management.user_management_service.utils.errorHandler.ForbiddenErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final AuthenticationProvider authenticationProvider;
        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final ForbiddenErrorHandler forbiddenErrorHandler;

        public SecurityConfig(
                        JwtAuthenticationFilter jwtAuthenticationFilter,
                        AuthenticationProvider authenticationProvider,
                        ForbiddenErrorHandler forbiddenErrorHandler) {
                this.authenticationProvider = authenticationProvider;
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
                this.forbiddenErrorHandler = forbiddenErrorHandler;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .cors().and() // Enable CORS
                                .csrf().disable()
                                .authorizeHttpRequests(authorize -> authorize
                                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/users/login/**", "/users/request-password-reset",
                                                "/users/change-password").permitAll()
                                        .requestMatchers("/users/v3/api-docs", "/users/swagger-ui.html",
                                                                "/webjars/**", "/users/OAuth2", "/users/{id}/password",
                                                                "/users/verify-otp")
                                                .permitAll()
                                                .requestMatchers("/trainees/**", "/users/**", "/assessments/**")
                                                .hasAnyRole("ADMIN")
                                                .requestMatchers("/trainees/**", "/assessments/**").hasAnyRole("ADMIN", "TRAINER")
                                                .anyRequest().authenticated())
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .exceptionHandling(exceptionHandling -> exceptionHandling
                                                .accessDeniedHandler(forbiddenErrorHandler))
                                .authenticationProvider(authenticationProvider);

                return http.build();
        }
}
