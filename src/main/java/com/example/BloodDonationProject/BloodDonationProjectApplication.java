package com.example.BloodDonationProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.BloodDonationProject.config.EnvConfig;

@SpringBootApplication
public class BloodDonationProjectApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BloodDonationProjectApplication.class);
        // Register EnvConfig to load .env properties into Spring Environment
        app.addInitializers(new EnvConfig());
        app.run(args);
    }
}
