package com.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("spring.mail")
public class MailSenderExperimental {

    private String host;
    private String username;
    private String password;
    private int port;
    private String protocol;
}
