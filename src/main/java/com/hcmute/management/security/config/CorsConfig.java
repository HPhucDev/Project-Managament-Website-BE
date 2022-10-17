package com.hcmute.management.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET","POST","DELETE","PUT","OPTIONS","PATCH")
                        .allowedHeaders("*")
                        .allowedOrigins("https://subject-management.herokuapp.com/","http://127.0.0.1:4040/","http://localhost:3000/","http://localhost:8080/","http://localhost:5000/"
                        )
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}
