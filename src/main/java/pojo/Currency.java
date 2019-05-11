package pojo;

import model.ModelCurrency;
import org.json.JSONObject;
import util.ReadJSONUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Currency {

    public static String getCurrency(ModelCurrency modelCurrency) throws IOException {
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
            modelCurrency.setAbbreviation(abbreviation);
            modelCurrency.setOfficialRate(officialRate);

            finalAnswer.append(modelCurrency.getAbbreviation())
                    .append(" равен ")
                    .append(modelCurrency.getOfficialRate())
                    .append("\n");
        }

        return finalAnswer.toString();

    }
}