package com.springemail.email;

import org.json.JSONException;
import org.json.JSONObject; // Ensure you have this dependency for JSON parsing
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

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
                        "Your Company",
                name, token);

        sendEmail(to, subject, message);
    }


    // Method to send an email for password reset
    public void sendPasswordResetEmail(String to, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n" +
                "http://localhost:8081/api/users/change-password?token=" + token);

        mailSender.send(message);
    }

    // Method to send a confirmation email after password change
    public void sendPasswordResetConfirmationEmail(String to) {
        String subject = "Your Password Has Been Changed";
        String message = "Dear User,\n\nYour password has been successfully changed.\n" +
                "If you did not request this change, please contact support immediately.\n" +
                "Regards,\nYour Company";

        sendEmail(to, subject, message);
    }

    // Common method to send an email
    private void sendEmail(String to, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }
}
