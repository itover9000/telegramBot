package com.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ConfigurationProperties("currency")
public class CurrencySetting {
    private String urlApi;
    private List<Integer> listCurrencies;
}
