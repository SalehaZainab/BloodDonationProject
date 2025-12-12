package com.example.BloodDonationProject;

import com.example.BloodDonationProject.config.EnvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.example.BloodDonationProject.config.EnvConfig;

@SpringBootApplication
public class BloodDonationProjectApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(BloodDonationProjectApplication.class);
		app.addInitializers(new EnvConfig());
		app.run(args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		System.out.println("\n" +
				"╔══════════════════════════════════════════════════════════════╗\n" +
				"║                                                              ║\n" +
				"║        🩸  BLOOD DONATION PROJECT STARTED! 🩸                ║\n" +
				"║                                                              ║\n" +
				"║     💉 Application is ready to save lives! 💉               ║\n" +
				"║                                                              ║\n" +
				"║     🏥 Server running on: http://localhost:8080              ║\n" +
				"║     📊 Health Check: http://localhost:8080/actuator/health   ║\n" +
				"║                                                              ║\n" +
				"║     ❤️  Every drop counts! ❤️                               ║\n" +
				"║                                                              ║\n" +
				"╚══════════════════════════════════════════════════════════════╝\n");
	}

}
