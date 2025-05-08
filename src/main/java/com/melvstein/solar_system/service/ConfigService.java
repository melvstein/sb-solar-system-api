package com.melvstein.solar_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {
    public Environment environment;

    @Autowired
    public void getEnvironment(Environment environment) {
        this.environment = environment;
    }

    public String getOsName() {
        String osName = environment.getProperty("os.name");
        return "OS name: " + osName;
    }

    public String getJavaVersion() {
        String javaVersion = environment.getProperty("java.version");
        return "Java version: " + javaVersion;
    }

    public String getAppName() {
        String appName = environment.getProperty("spring.application.name");
        return "App name: " + appName;
    }
}
