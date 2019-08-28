package com.service;

import com.model.CurrencyModel;
import com.settings.CurrencySetting;
import com.util.TransformObjectFromJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CurrencyInBYN {
    private final CurrencySetting currencySetting;
    private final TransformObjectFromJson<CurrencyModel> transformObjectFromJson;

    @Autowired
    public CurrencyInBYN(CurrencySetting currencySetting, TransformObjectFromJson<CurrencyModel> transformObjectFromJson) {
        this.currencySetting = currencySetting;
        this.transformObjectFromJson = transformObjectFromJson;
    }

    public String getCurrency(String url) throws IOException {
        List<Integer> currencyList = currencySetting.getListCurrencies();

        //get current date in format "dd.MM.yyyy"
        LocalDate localDate = LocalDate.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String currentDate = localDate.format(timeFormatter);

        StringBuilder finalMessage = new StringBuilder("Курс валют на " + currentDate + "\n");

        for (Integer idCurrency : currencyList) {
            URL urlCurrency = new URL(url + idCurrency);

            CurrencyModel currencyModel = transformObjectFromJson.getObjectFromJson(urlCurrency, CurrencyModel.class);

            if (currencyModel.getCurAbbreviation().equals("RUB")) {
                currencyModel.setCurAbbreviation("100RUB");
            }

            finalMessage
                    .append(currencyModel.getCurAbbreviation())
                    .append(" равен ")
                    .append(currencyModel.getCurOfficialRate())
                    .append("\n");
        }
        return finalMessage.toString();
    }
}