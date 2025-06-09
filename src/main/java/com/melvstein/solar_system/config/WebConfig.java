package com.melvstein.solar_system.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${allowed.planet.origin}")
    private String allowedPlanetOrigin;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(allowedPlanetOrigin != null && !allowedPlanetOrigin.isBlank() ? allowedPlanetOrigin : "*")
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}