package com.example.BloodDonationProject.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.type:service_account}")
    private String type;

    @Value("${firebase.project-id:}")
    private String projectId;

    @Value("${firebase.private-key-id:}")
    private String privateKeyId;

    @Value("${firebase.private-key:}")
    private String privateKey;

    @Value("${firebase.client-email:}")
    private String clientEmail;

    @Value("${firebase.client-id:}")
    private String clientId;

    @Value("${firebase.auth-uri:https://accounts.google.com/o/oauth2/auth}")
    private String authUri;

    @Value("${firebase.token-uri:https://oauth2.googleapis.com/token}")
    private String tokenUri;

    @Value("${firebase.auth-provider-x509-cert-url:https://www.googleapis.com/oauth2/v1/certs}")
    private String authProviderX509CertUrl;

    @Value("${firebase.client-x509-cert-url:}")
    private String clientX509CertUrl;

    @Value("${firebase.universe-domain:googleapis.com}")
    private String universeDomain;

    @Value("${firebase.enabled:false}")
    private boolean firebaseEnabled;

    @PostConstruct
    public void initialize() {
        if (!firebaseEnabled) {
            System.out.println("Firebase is disabled. Skipping initialization.");
            return;
        }

        try {
            // Check if Firebase is already initialized
            if (FirebaseApp.getApps().isEmpty()) {
                // Build the JSON credentials string
                String credentialsJson = String.format(
                        "{" +
                                "\"type\": \"%s\"," +
                                "\"project_id\": \"%s\"," +
                                "\"private_key_id\": \"%s\"," +
                                "\"private_key\": \"%s\"," +
                                "\"client_email\": \"%s\"," +
                                "\"client_id\": \"%s\"," +
                                "\"auth_uri\": \"%s\"," +
                                "\"token_uri\": \"%s\"," +
                                "\"auth_provider_x509_cert_url\": \"%s\"," +
                                "\"client_x509_cert_url\": \"%s\"," +
                                "\"universe_domain\": \"%s\"" +
                                "}",
                        type, projectId, privateKeyId, privateKey.replace("\\n", "\n"),
                        clientEmail, clientId, authUri, tokenUri,
                        authProviderX509CertUrl, clientX509CertUrl, universeDomain);

                ByteArrayInputStream serviceAccount = new ByteArrayInputStream(
                        credentialsJson.getBytes(StandardCharsets.UTF_8));

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println("Firebase Admin SDK initialized successfully");
            } else {
                System.out.println("Firebase Admin SDK already initialized");
            }
        } catch (IOException e) {
            System.err.println("Error initializing Firebase Admin SDK: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
