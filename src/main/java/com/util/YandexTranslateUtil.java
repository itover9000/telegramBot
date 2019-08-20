package com.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.model.YandexModel;
import com.settings.YandexSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;

@Service
public class YandexTranslateUtil {

    @Autowired
    private YandexSettings yandexSettings;

    public String translateFromEngToRu(String text) throws IOException {

        // replace all spaces with% 20 characters for the correct link
        String textWithSpacesReplaced = text.replace(" ", "%20");

        StringBuilder fullUrl = new StringBuilder()
                .append(yandexSettings.getUrl())
                .append(yandexSettings.getKey())
                .append("&text=")
                .append(textWithSpacesReplaced)
                .append("&lang=en-ru");


        URL urlObj = new URL(String.valueOf(fullUrl));

        String jsonStringFormat = ReadJSONUtil.getJSONStringFormat(urlObj);

        Gson gson = new Gson();
        Type collectionType = new TypeToken<YandexModel>() {
        }.getType();
        // get the object from json
        YandexModel yandexModel = gson.fromJson(jsonStringFormat, collectionType);


        StringBuilder translate = new StringBuilder();
        if (yandexModel.getCode() == 200) {
            yandexModel.getText().forEach(translate::append);
            return translate.toString();
        } else return "Не удалось получить данные";
    }

}