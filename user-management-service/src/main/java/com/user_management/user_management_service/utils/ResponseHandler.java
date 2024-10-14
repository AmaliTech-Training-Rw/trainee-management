package com.user_management.user_management_service.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> responseBuilder(
            String message,
            HttpStatus httpStatus,
            Object data
    ){
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("HttpStatus", httpStatus);
        response.put("data", data);

        return new ResponseEntity<>(response, httpStatus);
    }
}
