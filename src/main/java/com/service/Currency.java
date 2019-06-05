package com.service;

import com.model.CurrencyModel;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import com.util.ReadJSONUtil;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class Currency {

    public String getCurrency(CurrencyModel currencyModel) throws IOException {
        List<Integer> currencyList = new ArrayList<Integer>() {{
            add(145);
            add(292);
            add(298);
        }};

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentDate = simpleDateFormat.format(date);

        StringBuilder finalAnswer = new StringBuilder("Курс валют на " + currentDate + "\n");

        for (Integer idCurrency : currencyList) {

            URL url = new URL("http://www.nbrb.by/API/ExRates/Rates/" + idCurrency);
            JSONObject object = ReadJSONUtil.readJSONFromUrl(url);

            String abbreviation = object.getString("Cur_Abbreviation");
            if (abbreviation.equals("RUB")){
                abbreviation = "100RUB";
            }
            double officialRate = object.getDouble("Cur_OfficialRate");
            currencyModel.setAbbreviation(abbreviation);
            currencyModel.setOfficialRate(officialRate);

            finalAnswer.append(currencyModel.getAbbreviation())
                    .append(" равен ")
                    .append(currencyModel.getOfficialRate())
                    .append("\n");
        }

        return finalAnswer.toString();

    }
}
