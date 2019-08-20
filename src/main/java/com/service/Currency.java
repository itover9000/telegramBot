package com.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.model.CurrencyModel;
import com.settings.CurrencySetting;
import com.util.ReadJSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class Currency {

    @Autowired
    private CurrencySetting currencySetting;

    public String getCurrency() throws IOException {
        List<Integer> currencyList = currencySetting.getListCurrencies();

        //get current date in format "dd.MM.yyyy"
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = simpleDateFormat.format(date);

        StringBuilder finalMessage = new StringBuilder("Курс валют на " + currentDate + "\n");

        for (Integer idCurrency : currencyList) {
            URL url = new URL(currencySetting.getUrlApi() + "" + idCurrency);

            String jsonStringFormat = ReadJSONUtil.getJSONStringFormat(url);

            if (!jsonStringFormat.isEmpty()) {
                Gson gson = new Gson();

                Type curr = new TypeToken<CurrencyModel>() {
                }.getType();

                // get the CurrencyModel from json
                CurrencyModel currentCurrencyModel = gson.fromJson(jsonStringFormat, curr);

                if (currentCurrencyModel.getCurAbbreviation().equals("RUB")){
                    currentCurrencyModel.setCurAbbreviation("100RUB");
                }

                finalMessage
                        .append(currentCurrencyModel.getCurAbbreviation())
                        .append(" равен ")
                        .append(currentCurrencyModel.getCurOfficialRate())
                        .append("\n");
            }
        }
        return finalMessage.toString();
    }
}