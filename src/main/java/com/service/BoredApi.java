package com.service;

import com.model.BoredModel;
import com.settings.UrlSetting;
import com.util.TransformObjectFromJson;
import com.util.YandexTranslateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
public class BoredApi {
    private final UrlSetting urlSetting;

    @Autowired
    private YandexTranslateUtil yandexTranslateUtil;

    @Autowired
    private TransformObjectFromJson<BoredModel> transformObjectFromJson;

    @Autowired
    public BoredApi(UrlSetting urlSetting) {
        this.urlSetting = urlSetting;
    }

    public String getBoredStringFormat() throws IOException {
        URL url = new URL(urlSetting.getUrlToBoredapi());
        BoredModel boredModel = transformObjectFromJson.getObjectFromJson(url, BoredModel.class);

        StringBuilder answer = new StringBuilder()
                .append("Мероприятие: ").append(yandexTranslateUtil.translateFromEngToRu(boredModel.getActivity())).append("\n")
                .append("Activity:  ").append(boredModel.getActivity()).append("\n")
                .append("Количество участников: ").append(boredModel.getParticipants());

        return answer.toString();
    }
}
