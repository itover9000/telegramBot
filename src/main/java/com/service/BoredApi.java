package com.service;

import com.model.BoredModel;
import com.util.TransformObjectFromJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
public class BoredApi {
    private final YandexTranslate yandexTranslate;
    private final TransformObjectFromJson<BoredModel> transformObjectFromJson;

    @Autowired
    public BoredApi(YandexTranslate yandexTranslate, TransformObjectFromJson<BoredModel> transformObjectFromJson) {
        this.yandexTranslate = yandexTranslate;
        this.transformObjectFromJson = transformObjectFromJson;
    }

    public String getBoredStringFormat(String url) throws IOException {
        URL urlBored = new URL(url);
        BoredModel boredModel = transformObjectFromJson.getObjectFromJson(urlBored, BoredModel.class);

        StringBuilder answer = new StringBuilder()
                .append("Мероприятие: ").append(yandexTranslate.translateFromEngToRu(boredModel.getActivity())).append("\n")
                .append("Activity:  ").append(boredModel.getActivity()).append("\n")
                .append("Количество участников: ").append(boredModel.getParticipants());

        return answer.toString();
    }
}
