package com.springemail.email.service;

import com.springemail.email.repository.UserRepository;
import com.assessment_management.assessment_management_service.model.TraineeProgress;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.json.JSONException;
import org.json.JSONObject; // Ensure you have this dependency for JSON parsing
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String senderEmail;

    // Method to listen for new user registrations

    @KafkaListener(topics = "${kafka.topic.user-created}", groupId = "spring.kafka.consumer.group-id")
    public void listen(String message) throws JSONException {
        // Parse the JSON message
        JSONObject jsonObject = new JSONObject(message);
        String email = jsonObject.getString("email");
        String name = jsonObject.getString("name");
        String password = jsonObject.getString("password");
        String resetToken = jsonObject.getString("token");


        sendRegistrationEmail(email, name, password, resetToken);
    }

    // Method to send an email after registration
    public void sendRegistrationEmail(String to, String name, String password, String token) {
        String subject = "Welcome to TrainingSuite";

        // Construct the email message with HTML content
        String message = String.format("<html><body>" +
                        "<p>Hi %s,</p>" +
                        "<p>Welcome to TrainingSuite! An account has been created for you by our administrator.</p>" +
                        "<p>You can now access the platform to manage your training experience.</p>" +
                        "<p>Here are your account details:</p>" +
                        "<p>Email: %s</p>" +
                        "<p>Password: %s</p>" +
                        "<p>For your security, we recommend updating your password as soon as possible. " +
                        "Please follow the link below to set your new password:</p>" +
                        "<p><a href='http://yourdomain.com/newPassword?token=%s'>Login</a></p>" +
                        "<p>Best Regards,<br>The TraineeSuite Team</p>" +
                        "</body></html>",
                name, to, password, token);
        sendEmail(to, subject, message);
    }
    public void sendPasswordResetEmail(String to, String otp, String name) throws JSONException {
        String subject = "Password Reset Request";
        String message = String.format(
                "Dear %s,\n\n" +
                        "You have requested to reset your password. Please use the following OTP to reset your password:\n" +
                        "OTP: %s\n\n" +
                        "This OTP is valid for a limited time.\n\n" +
                        "Regards,\n" +
                        "TraineeSuit Team",
                name, otp
        );

        sendEmail(to, subject, message);
    }

    @KafkaListener(topics = "${kafka.topic.password-reset}", groupId = "spring.kafka.consumer.group-id")
    public void listenPasswordReset(String message) throws JSONException {
        // Parse the JSON message for password reset
        JSONObject jsonObject = new JSONObject(message);
        String email = jsonObject.getString("email");
        String otp = jsonObject.getString("otp");
        String name = jsonObject.getString("name");
        sendPasswordResetEmail(email, otp, name);
    }



    // Method to send a confirmation email after password change
    public void sendPasswordResetConfirmationEmail(String to) {
        String subject = "Your Password Has Been Changed";
        String message = "Dear User,\n\nYour password has been successfully changed.\n" +
                "If you did not request this change, please contact support immediately.\n" +
                "Regards,\nYour Company";

        sendEmail(to, subject, message);
    }



    public void sendEmail(String to, String subject, String message) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(message, true);
        helper.setFrom("TraininingSuite <" + senderEmail + ">");
        mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    // New method to send progress update email
    public void sendProgressUpdateEmail(String to, TraineeProgress progress) {
        String subject = "Your Progress Has Been Updated";

        // Construct the email message with HTML content
        String message = String.format("<html><body>" +
                        "<p>Hi,</p>" +
                        "<p>Your training progress has been updated.</p>" +
                        "<p>Current Phase: %s</p>" +
                        "<p>Progress Indicator: %s</p>" +
                        "<p>Best Regards,<br>The TraineeSuite Team</p>" +
                        "</body></html>",
                progress.getCurrentPhase(), progress.getProgressIndicator());

        sendEmail(to, subject, message);
    }

}
