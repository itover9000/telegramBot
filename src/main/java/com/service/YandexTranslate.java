package com.service;

import com.model.YandexModel;
import com.settings.YandexSettings;
import com.util.TransformObjectFromJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Service
public class YandexTranslate {

    @Autowired
    private YandexSettings yandexSettings;

    @Autowired
    private TransformObjectFromJson<YandexModel> transformObjectFromJson;

    public String translateFromEngToRu(String text) throws IOException {
        // encode text for the correct link
        String encodeText = URLEncoder.encode(text, StandardCharsets.UTF_8);

        StringBuilder fullUrl = new StringBuilder()
                .append(yandexSettings.getUrl())
                .append(yandexSettings.getKey())
                .append("&text=")
                .append(encodeText)
                .append("&lang=en-ru");

        URL url = new URL(fullUrl.toString());

        //transform json to yandexModel
        YandexModel yandexModel = transformObjectFromJson.getObjectFromJson(url, YandexModel.class);

        if (yandexModel.getCode() == Response.Status.OK.getStatusCode()) {
            return yandexModel.getText().stream().map(StringBuilder::new).collect(Collectors.joining());
        } else return "Не удалось получить данные";
    }
}