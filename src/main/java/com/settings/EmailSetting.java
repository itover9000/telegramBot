package com.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("email")
public class EmailSetting {
    private String emailSender;
    private String emailRecipient;
    private String fromEmail;
    private String passwordSender;
}