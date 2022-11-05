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
                        .allowedMethods("GET","POST","DELETE","PUT","OPTIONS")
                        .allowedHeaders("*")
                        .allowedOrigins("http://localhost:3000/","http://localhost:8080/","https://tiki-web.vercel.app/"
                        ,"https://gorgeous-pastelito-2fb64c.netlify.app/","https://tiki-ui.vercel.app/","https://ad86-2001-ee0-4f0c-c4c0-5d0a-cd6f-680e-1c23.ngrok.io/")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}
