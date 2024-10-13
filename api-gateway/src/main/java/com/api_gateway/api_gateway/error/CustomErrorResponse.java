package com.api_gateway.api_gateway.error;

public class CustomErrorResponse {
    private String message;
    private int status;

    public CustomErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
