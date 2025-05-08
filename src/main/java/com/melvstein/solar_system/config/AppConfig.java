package com.melvstein.solar_system.config;

import com.melvstein.solar_system.service.ConfigService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ConfigService configService() {
        return new ConfigService();
    }
}
