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
                        .allowedOrigins(" https://d0e8-2001-ee0-4f0f-c0f0-6c4b-3143-1481-9a3f.ngrok.io/","http://127.0.0.1:4040/","http://localhost:3000/","http://localhost:8080/","http://localhost:5000/","https://tiki-web.vercel.app/"
                        ,"https://gorgeous-pastelito-2fb64c.netlify.app/","https://tiki-ui.vercel.app/")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}
