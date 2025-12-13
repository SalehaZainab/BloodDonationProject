package com.example.BloodDonationProject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${email.from}")
    private String fromEmail;

    @Value("${email.enabled:true}")
    private boolean emailEnabled;

    @Value("${app.name}")
    private String appName;

    /**
     * Send simple text email
     */
    public void sendEmail(String to, String subject, String body) {
        if (!emailEnabled) {
            System.out.println("Email is disabled. Skipping email send.");
            System.out.println("To: " + to);
            System.out.println("Subject: " + subject);
            System.out.println("Body: " + body);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            System.out.println("Email sent successfully to: " + to);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    /**
     * Send HTML email
     */
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        System.out.println("\n======= EMAIL SERVICE DEBUG =======");
        System.out.println("Email Enabled: " + emailEnabled);
        System.out.println("From Email: " + fromEmail);
        System.out.println("To Email: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("==================================\n");

        if (!emailEnabled) {
            System.out.println("Email is disabled. Skipping HTML email send.");
            System.out.println("To: " + to);
            System.out.println("Subject: " + subject);
            return;
        }

        try {
            System.out.println("Creating MimeMessage...");
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            System.out.println("Setting email properties...");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            System.out.println("Sending email via SMTP...");
            mailSender.send(message);
            System.out.println("✅ HTML email sent successfully to: " + to);
            System.out.println("Check your inbox (and spam folder) for: " + subject);
        } catch (MessagingException e) {
            System.err.println("❌ MessagingException: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send HTML email: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("❌ General Exception while sending email: " + e.getMessage());
            System.err.println("Exception type: " + e.getClass().getName());
            e.printStackTrace();
            throw new RuntimeException("Failed to send HTML email: " + e.getMessage(), e);
        }
    }

    /**
     * Send OTP email
     */
    public void sendOtpEmail(String to, String otp, String userName) {
        System.out.println("\n📧 Preparing to send OTP email...");
        System.out.println("Recipient: " + to);
        System.out.println("User Name: " + userName);
        System.out.println("OTP: " + otp);

        String subject = appName + " - OTP Verification";

        String htmlBody = buildOtpEmailTemplate(userName, otp);

        sendHtmlEmail(to, subject, htmlBody);
    }

    /**
     * Send password reset email
     */
    public void sendPasswordResetEmail(String to, String resetToken, String userName) {
        String subject = appName + " - Password Reset Request";

        String htmlBody = buildPasswordResetTemplate(userName, resetToken);

        sendHtmlEmail(to, subject, htmlBody);
    }

    /**
     * Send welcome email
     */
    public void sendWelcomeEmail(String to, String userName) {
        String subject = "Welcome to " + appName;

        String htmlBody = buildWelcomeEmailTemplate(userName);

        sendHtmlEmail(to, subject, htmlBody);
    }

    /**
     * Send donation confirmation email
     */
    public void sendDonationConfirmationEmail(String to, String donorName, String bloodGroup, int units,
            String hospital) {
        String subject = appName + " - Donation Confirmation";

        String htmlBody = buildDonationConfirmationTemplate(donorName, bloodGroup, units, hospital);

        sendHtmlEmail(to, subject, htmlBody);
    }

    /**
     * Build OTP email template
     */
    private String buildOtpEmailTemplate(String userName, String otp) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #dc3545; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }"
                +
                ".content { background-color: #f9f9f9; padding: 30px; border-radius: 0 0 5px 5px; }" +
                ".otp-box { background-color: #fff; padding: 20px; text-align: center; margin: 20px 0; border: 2px dashed #dc3545; border-radius: 5px; }"
                +
                ".otp-code { font-size: 32px; font-weight: bold; color: #dc3545; letter-spacing: 5px; }" +
                ".footer { text-align: center; margin-top: 20px; font-size: 12px; color: #666; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>🩸 " + appName + "</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<h2>Hello " + userName + ",</h2>" +
                "<p>Your OTP for account verification is:</p>" +
                "<div class='otp-box'>" +
                "<div class='otp-code'>" + otp + "</div>" +
                "</div>" +
                "<p><strong>This OTP will expire in 10 minutes.</strong></p>" +
                "<p>If you didn't request this verification, please ignore this email.</p>" +
                "<p>Thank you for joining our blood donation community!</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>© 2025 " + appName + ". Every drop counts!</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    /**
     * Build password reset email template
     */
    private String buildPasswordResetTemplate(String userName, String resetToken) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #dc3545; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }"
                +
                ".content { background-color: #f9f9f9; padding: 30px; border-radius: 0 0 5px 5px; }" +
                ".token-box { background-color: #fff; padding: 20px; text-align: center; margin: 20px 0; border: 2px solid #dc3545; border-radius: 5px; }"
                +
                ".reset-token { font-size: 18px; font-weight: bold; color: #dc3545; word-break: break-all; }" +
                ".warning { background-color: #fff3cd; padding: 15px; border-left: 4px solid #ffc107; margin: 20px 0; }"
                +
                ".footer { text-align: center; margin-top: 20px; font-size: 12px; color: #666; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>🩸 " + appName + "</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<h2>Hello " + userName + ",</h2>" +
                "<p>We received a request to reset your password. Use the token below:</p>" +
                "<div class='token-box'>" +
                "<div class='reset-token'>" + resetToken + "</div>" +
                "</div>" +
                "<div class='warning'>" +
                "<strong>⚠️ Security Notice:</strong>" +
                "<p>This token will expire in 1 hour. If you didn't request this password reset, please secure your account immediately.</p>"
                +
                "</div>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>© 2025 " + appName + ". Every drop counts!</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    /**
     * Build welcome email template
     */
    private String buildWelcomeEmailTemplate(String userName) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #dc3545; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }"
                +
                ".content { background-color: #f9f9f9; padding: 30px; border-radius: 0 0 5px 5px; }" +
                ".welcome-box { background-color: #fff; padding: 20px; margin: 20px 0; border-radius: 5px; text-align: center; }"
                +
                ".footer { text-align: center; margin-top: 20px; font-size: 12px; color: #666; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>🩸 " + appName + "</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<div class='welcome-box'>" +
                "<h2>Welcome, " + userName + "! 🎉</h2>" +
                "<p>Thank you for joining our blood donation community.</p>" +
                "<p>Your registration is complete and you can now:</p>" +
                "<ul style='text-align: left;'>" +
                "<li>Find blood donation requests in your area</li>" +
                "<li>Register as a blood donor</li>" +
                "<li>Track your donation history</li>" +
                "<li>Help save lives</li>" +
                "</ul>" +
                "</div>" +
                "<p><strong>Every drop counts!</strong> Together, we can save lives.</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>© 2025 " + appName + ". Every drop counts!</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    /**
     * Build donation confirmation email template
     */
    private String buildDonationConfirmationTemplate(String donorName, String bloodGroup, int units, String hospital) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #28a745; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }"
                +
                ".content { background-color: #f9f9f9; padding: 30px; border-radius: 0 0 5px 5px; }" +
                ".donation-details { background-color: #fff; padding: 20px; margin: 20px 0; border-left: 4px solid #28a745; }"
                +
                ".thank-you { background-color: #d4edda; padding: 20px; border-radius: 5px; text-align: center; margin: 20px 0; }"
                +
                ".footer { text-align: center; margin-top: 20px; font-size: 12px; color: #666; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h1>🩸 " + appName + "</h1>" +
                "</div>" +
                "<div class='content'>" +
                "<h2>Hello " + donorName + ",</h2>" +
                "<div class='thank-you'>" +
                "<h3>🎉 Thank You for Your Donation! 🎉</h3>" +
                "<p>You've made a difference today!</p>" +
                "</div>" +
                "<div class='donation-details'>" +
                "<h3>Donation Details:</h3>" +
                "<p><strong>Blood Group:</strong> " + bloodGroup + "</p>" +
                "<p><strong>Units Donated:</strong> " + units + "</p>" +
                "<p><strong>Hospital:</strong> " + hospital + "</p>" +
                "</div>" +
                "<p>Your generous donation can save up to 3 lives. You are a hero!</p>" +
                "<p><strong>Remember:</strong> Wait at least 56 days before your next donation.</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>© 2025 " + appName + ". Every drop counts!</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
