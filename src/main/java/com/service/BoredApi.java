package com.service;

import com.model.BoredModel;
import com.settings.UrlSetting;
import com.util.ReadJSONUtil;
import com.util.YandexTranslateUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Service
public class BoredApi {
    private final UrlSetting urlSetting;

    @Autowired
    private YandexTranslateUtil yandexTranslateUtil;

    @Autowired
    public BoredApi(UrlSetting urlSetting) {
        this.urlSetting = urlSetting;
    }

    public String getBoredStringFormat(BoredModel bored) throws IOException {
        JSONObject object = ReadJSONUtil.readJSONFromUrl(new URL(urlSetting.getUrlToBoredapi()));


        String activity = object.getString("activity");
        int participants = object.getInt("participants");
        bored.setActivity(yandexTranslateUtil.translateFromEngToRu(activity));
        bored.setParticipants(participants);

        //conversion to UTF-8 (incorrect encoding when starting jar)
        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(bored.getActivity());
        String activityInRussian = StandardCharsets.UTF_8.decode(byteBuffer).toString();

        StringBuilder answer = new StringBuilder()
                .append("Мероприятие: ").append(activityInRussian).append("\n")
                .append("Activity:  ").append(activity).append("\n")
                .append("Количество участников: ").append(bored.getParticipants());

        return String.valueOf(answer);
    }
}
