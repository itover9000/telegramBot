package com.service;

import com.model.BoredModel;
import com.util.TransformObjectFromJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
public class BoredApi {

    @Autowired
    private YandexTranslate yandexTranslate;

    @Autowired
    private TransformObjectFromJson<BoredModel> transformObjectFromJson;

    public String getBoredStringFormat(String stringUrl) throws IOException {
        URL url = new URL(stringUrl);
        BoredModel boredModel = transformObjectFromJson.getObjectFromJson(url, BoredModel.class);

        StringBuilder answer = new StringBuilder()
                .append("Мероприятие: ").append(yandexTranslate.translateFromEngToRu(boredModel.getActivity())).append("\n")
                .append("Activity:  ").append(boredModel.getActivity()).append("\n")
                .append("Количество участников: ").append(boredModel.getParticipants());

        return answer.toString();
    }
}
