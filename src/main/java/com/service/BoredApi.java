package com.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.model.BoredModel;
import com.settings.UrlSetting;
import com.util.ReadJSONUtil;
import com.util.YandexTranslateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;

@Service
public class BoredApi {
    private final UrlSetting urlSetting;

    @Autowired
    private YandexTranslateUtil yandexTranslateUtil;

    @Autowired
    public BoredApi(UrlSetting urlSetting) {
        this.urlSetting = urlSetting;
    }

    public String getBoredStringFormat() throws IOException {
        URL url = new URL(urlSetting.getUrlToBoredapi());
        String jsonStringFormat = ReadJSONUtil.getJSONStringFormat(url);

        Gson gson = new Gson();
        Type boredFromJson = new TypeToken<BoredModel>() {
        }.getType();

        // get the CurrencyModel from json
        BoredModel boredModel = gson.fromJson(jsonStringFormat, boredFromJson);

        StringBuilder answer = new StringBuilder()
                .append("Мероприятие: ").append(yandexTranslateUtil.translateFromEngToRu(boredModel.getActivity())).append("\n")
                .append("Activity:  ").append(boredModel.getActivity()).append("\n")
                .append("Количество участников: ").append(boredModel.getParticipants());

        return String.valueOf(answer);
    }
}
