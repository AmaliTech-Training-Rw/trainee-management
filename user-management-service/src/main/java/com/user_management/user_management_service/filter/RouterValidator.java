package com.user_management.user_management_service.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;


@Component
public class RouterValidator {

    // Example of secured routes
    public final Predicate<ServerHttpRequest> isSecured = request ->
            request.getURI().getPath().startsWith("/api/v1/trainees") ||
                    request.getURI().getPath().startsWith("/api/v1/users");
}
