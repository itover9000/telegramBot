package com.util;

import com.model.YandexModel;
import com.settings.YandexSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
public class YandexTranslateUtil {

    @Autowired
    private YandexSettings yandexSettings;

    @Autowired
    private TransformObjectFromJson<YandexModel> transformObjectFromJson;

    public String translateFromEngToRu(String text) throws IOException {

        // replace all spaces with% 20 characters for the correct link
        String textWithSpacesReplaced = text.replace(" ", "%20");

        StringBuilder fullUrl = new StringBuilder()
                .append(yandexSettings.getUrl())
                .append(yandexSettings.getKey())
                .append("&text=")
                .append(textWithSpacesReplaced)
                .append("&lang=en-ru");

        URL url = new URL(fullUrl.toString());

        //transform json to yandexModel
        YandexModel yandexModelFromJson = transformObjectFromJson.getObjectFromJson(url, YandexModel.class);

        StringBuilder translate = new StringBuilder();
        if (yandexModelFromJson != null && yandexModelFromJson.getCode() == 200) {
            yandexModelFromJson.getText().forEach(translate::append);
            return translate.toString();
        } else return "Не удалось получить данные";
    }
}