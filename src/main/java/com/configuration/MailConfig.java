package com.configuration;

import com.settings.MailSenderSetting;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender getMailSender(MailSenderSetting emailSetting) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(emailSetting.getHost());
        mailSender.setPort(emailSetting.getPort());
        mailSender.setUsername(emailSetting.getUsername());
        mailSender.setPassword(emailSetting.getPassword());

        Properties properties = mailSender.getJavaMailProperties();
        properties.setProperty("mail.transport.protocol", emailSetting.getProtocol());

        return mailSender;
    }
}