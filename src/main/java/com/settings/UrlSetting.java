package com.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("url")
public class UrlSetting {
    private String urlToGifFile;
    private String gifFileNameFromMeteoinfo;
    private String urlToBoredapi;
    private String urlToGeomagneticSite;
}
