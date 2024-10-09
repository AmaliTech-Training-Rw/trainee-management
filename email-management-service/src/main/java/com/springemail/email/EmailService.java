package com.springemail.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Method to send an email after registration
    public void sendRegistrationEmail(String to, String userName) {
        String subject = "Welcome to Our Service!";
        String message = "Dear " + userName + ",\n\n" +
                "Thank you for registering with us. We're excited to have you on board!\n" +
                "Regards,\nYour Company";

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
