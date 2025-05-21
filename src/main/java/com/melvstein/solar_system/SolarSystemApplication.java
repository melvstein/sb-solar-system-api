package com.melvstein.solar_system;

import com.melvstein.solar_system.service.ConfigService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@EnableCaching
@SpringBootApplication
public class SolarSystemApplication {
	public Environment environment;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SolarSystemApplication.class, args);
		ConfigService configService = context.getBean(ConfigService.class);

		System.out.println(configService.getOsName());
		System.out.println(configService.getJavaVersion());
		System.out.println(configService.getAppName());
        System.out.println(configService.getConfigEnv());
	}

}
