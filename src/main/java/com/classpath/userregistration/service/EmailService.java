package com.classpath.userregistration.service;

import com.classpath.userregistration.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
public class EmailService {

    private JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    @Async
    public void sendConfirmationTokenEmail(String emailAddress, String content){
        SimpleMailMessage registrationEmail = new SimpleMailMessage();
        registrationEmail.setTo(emailAddress);
        registrationEmail.setSubject("Registration Confirmation");
        registrationEmail.setText(content);
        registrationEmail.setFrom("noreply@domain.com");
        this.mailSender.send(registrationEmail);
    }
}