package com.service;

import com.exception.NoDataOnSiteException;
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

    public String getBoredStringFormat() throws IOException, NoDataOnSiteException {
        URL url = new URL(urlSetting.getUrlToBoredapi());

        String jsonStringFormat = ReadJSONUtil.getJSONStringFormat(url);

        if (!jsonStringFormat.isEmpty()) {
            Gson gson = new Gson();

            Type boresType = new TypeToken<BoredModel>() {
            }.getType();

            // get the BoredModel from json
            BoredModel currentBoredModel = gson.fromJson(jsonStringFormat, boresType);

            StringBuilder answer = new StringBuilder()
                    .append("Мероприятие: ").append(yandexTranslateUtil.translateFromEngToRu(currentBoredModel.getActivity())).append("\n")
                    .append("Activity:  ").append(currentBoredModel.getActivity()).append("\n")
                    .append("Количество участников: ").append(currentBoredModel.getParticipants());

            return answer.toString();

        } else throw new NoDataOnSiteException("not received data from site");
    }
}
