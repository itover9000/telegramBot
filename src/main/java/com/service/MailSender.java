package com.service;

import com.settings.MailSenderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailSenderSetting mailSenderSetting;

    public void send(String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(mailSenderSetting.getUsername());
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        javaMailSender.send(mailMessage);
    }
}
