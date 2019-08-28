package com.service;

import com.settings.MailSenderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSender {
    private final JavaMailSender javaMailSender;
    private final MailSenderSetting mailSenderSetting;

    @Autowired
    public MailSender(@Qualifier("getMailSender") JavaMailSender javaMailSender, MailSenderSetting mailSenderSetting) {
        this.javaMailSender = javaMailSender;
        this.mailSenderSetting = mailSenderSetting;
    }

    public void send(String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(mailSenderSetting.getEmailSender());
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        javaMailSender.send(mailMessage);
    }
}
