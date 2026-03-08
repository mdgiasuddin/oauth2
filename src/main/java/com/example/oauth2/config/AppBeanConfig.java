package com.example.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;
import java.util.Random;

@Configuration
public class AppBeanConfig {

    @Bean
    public Random random() {
        return new SecureRandom();
    }
}
