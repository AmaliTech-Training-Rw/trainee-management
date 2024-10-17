package com.assessment_management.assessment_management_service.config;



public class UserContext {
    private static final ThreadLocal<String> userIdHolder = new ThreadLocal<>();

    public static String getUserId() {
        return userIdHolder.get();
    }

    public static void setUserId(String userId) {
        userIdHolder.set(userId);
    }

    public static void clear() {
        userIdHolder.remove();
    }
}
