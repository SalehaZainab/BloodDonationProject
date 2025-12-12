package com.example.BloodDonationProject.config;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;
import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        Map<String, Object> envMap = new HashMap<>();

        // Load all entries from .env file
        dotenv.entries().forEach(entry -> {
            envMap.put(entry.getKey(), entry.getValue());
        });

        environment.getPropertySources()
                .addFirst(new MapPropertySource("dotenvProperties", envMap));
    }
}
