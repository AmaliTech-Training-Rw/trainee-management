package com.api_gateway.api_gateway.handler;

import com.api_gateway.api_gateway.error.CustomErrorResponse;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class ErrorHandlerFilter extends AbstractGatewayFilterFactory<ErrorHandlerFilter.Config> {

    public ErrorHandlerFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            return chain.filter(exchange)
                    .onErrorResume(throwable -> {
                        // Customize your error handling logic here
                        CustomErrorResponse errorResponse = new CustomErrorResponse("Custom error message", 404);
                        return handleError(exchange, errorResponse);
                    });
        };
    }

    private Mono<Void> handleError(ServerWebExchange exchange, CustomErrorResponse errorResponse) {
        exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String responseBody = String.format("{\"message\": \"%s\", \"status\": %d}", errorResponse.getMessage(), errorResponse.getStatus());

        DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
        DataBuffer buffer = dataBufferFactory.wrap(responseBody.getBytes(StandardCharsets.UTF_8));

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    public static class Config {

    }
}
