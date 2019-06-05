package com.model;

import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class CurrencyModel {
    private String date;
    private String abbreviation;
    private Double officialRate;
}
