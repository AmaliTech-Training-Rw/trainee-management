package com.api_gateway.api_gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    @Value("${security.jwt.token.secret-key}")
    private String jwtSecret;

    @Value("${security.jwt.token.expiration}")
    private long jwtExpiration;

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Check for Authorization header
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return this.onError(exchange, "Missing Authorization header", HttpStatus.UNAUTHORIZED);
            }

            // Extract Bearer token
            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String[] parts = authHeader.split(" ");

            if (parts.length != 2 || !"Bearer".equals(parts[0])) {
                return this.onError(exchange, "Invalid Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String token = parts[1];

            try {
                // Validate JWT
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtSecret)
                        .parseClaimsJws(token)
                        .getBody();

                // Extract user ID from claims
                String userId = claims.get("userId", String.class);  // Assuming userId is a claim in your token

                if (userId == null || userId.isEmpty()) {
                    return this.onError(exchange, "User ID not found in token", HttpStatus.UNAUTHORIZED);
                }

                // Set userId in MDC for logging purposes
                MDC.put("userId", userId);

                // Add userId and Authorization header for downstream services
                exchange.getRequest()
                        .mutate()
                        .header("x-auth-user-id", userId)  // Custom header to propagate userId
                        .header(HttpHeaders.AUTHORIZATION, authHeader) // Preserve the original Authorization header
                        .build();

                // Continue with the filter chain
                return chain.filter(exchange)
                        .doFinally(signalType -> MDC.clear());  // Clear MDC after request processing

            } catch (SignatureException e) {
                return this.onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
            } catch (Exception e) {
                return this.onError(exchange, "Token validation error", HttpStatus.UNAUTHORIZED);
            }
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String jsonResponse = String.format("{\"message\": \"%s\", \"status\": %d}", message, status.value());
        byte[] bytes = jsonResponse.getBytes(StandardCharsets.UTF_8);

        response.setRawStatusCode(status.value());
        response.getHeaders().setContentLength(bytes.length);

        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }

    public static class Config {
    }
}
