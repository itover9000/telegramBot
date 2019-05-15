package model;

import lombok.Data;

import java.util.Date;

@Data
public class CurrencyModel {
    private String date;
    private String abbreviation;
    private Double officialRate;
}
