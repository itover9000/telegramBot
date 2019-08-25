package com.service;

import com.model.CurrencyModel;
import com.settings.CurrencySetting;
import com.util.TransformObjectFromJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class Currency {

    @Autowired
    private CurrencySetting currencySetting;

    @Autowired
    private TransformObjectFromJson<CurrencyModel> transformObjectFromJson;

    public String getCurrency(String stringUrl) throws IOException {
        List<Integer> currencyList = currencySetting.getListCurrencies();

        //get current date in format "dd.MM.yyyy"
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = simpleDateFormat.format(date);

        StringBuilder finalMessage = new StringBuilder("Курс валют на " + currentDate + "\n");

        for (Integer idCurrency : currencyList) {
            URL url = new URL(stringUrl + idCurrency);

            CurrencyModel currencyModel = transformObjectFromJson.getObjectFromJson(url, CurrencyModel.class);

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