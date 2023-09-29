package com.mparaske.studentmanagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("#{'${allowed.origins}'.split(',')}")
    private final List<String> rawOrigins;

    public CorsConfig(List<String> rawOrigins) {
        this.rawOrigins = rawOrigins;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**")
                .allowedOrigins(getOrigin())
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true);
    }

    public String[] getOrigin() {
        int size = rawOrigins.size();
        String[] originArray = new String[size];
        return rawOrigins.toArray(originArray);
    }
}

