package com.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@Data
@ConfigurationProperties("bot")
public class BotSetting {
    private String token;
    private String username;
    private Map<String, List<String>> buttons;
}
