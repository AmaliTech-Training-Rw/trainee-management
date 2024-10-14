package com.trainee_management.trainee_management_service.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class FileUtils {
    public static String encodeFileToBase64(MultipartFile file) throws IOException {
        return Base64.encodeBase64String(file.getBytes());
    }
}