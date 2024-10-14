package com.file_management.file_management_service.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class FileUtils {
    public static MultipartFile decodeBase64ToMultipartFile(String base64String, String fileName, String contentType) {
        byte[] decodedBytes = Base64.decodeBase64(base64String);
        return new Base64DecodedMultipartFile(decodedBytes, fileName, contentType);
    }
}