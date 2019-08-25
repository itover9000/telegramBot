package com.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("map")
public class DrawVillageSetting {
    private int x;
    private int y;
    private int width;
    private int height;
    private String message;
}
