package com.assessment_management.assessment_management_service.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dg8qr14n3",
                "api_key", "797434996567489",
                "api_secret", "qxqz9WBWkwjc4vwXZmsVOyhnqu4"
        ));
    }
}