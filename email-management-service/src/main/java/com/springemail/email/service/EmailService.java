package com.springemail.email.service;

import com.springemail.email.repository.UserRepository;
import org.json.JSONException;
import org.json.JSONObject; // Ensure you have this dependency for JSON parsing
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
        String resetToken = jsonObject.getString("token");


        sendRegistrationEmail(email, name, resetToken);
    }

    // Method to send an email after registration
    public void sendRegistrationEmail(String to, String name, String token) {
        String subject = "Welcome to Our Service!";
        String message = String.format("Dear %s,\n\n" +
                        "Thank you for registering with us. We're excited to have you on board!\n" +
                        "To create your new password, please use the following link:\n" +
                        "http://yourdomain.com/newPassword?token=%s\n\n" +
                        "Regards,\n" +
                        "Trainee Management",
                name, token);

        sendEmail(to, subject, message);
    }
    public void sendPasswordResetEmail(String to, String otp, String name) throws JSONException {
        String subject = "Password Reset Request";
        String message = String.format(
                "Dear %s,\n\n" +
                        "You have requested to reset your password. Please use the following OTP to reset your password:\n" +
                        "OTP: %s\n\n" +
                        "This OTP is valid for a limited time. If you did not request this, please ignore this email.\n\n" +
                        "Regards,\n" +
                        "Your Company",
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

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        email.setFrom("Trainee Management <" + senderEmail + ">");
        mailSender.send(email);
    }

}
