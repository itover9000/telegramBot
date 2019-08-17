package com.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.model.GeomagneticStormModel;
import com.service.GeomagneticStorm;
import com.service.Sender;
import com.settings.EmailSetting;
import com.settings.UrlSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collection;
import java.util.List;

@Service
public class GeomagneticStormUtil {

    private final GeomagneticStorm geomagneticStorm;

    private final EmailSetting emailSetting;

    private final GeomagneticStormModel stormModel;

    @Autowired
    private UrlSetting urlSetting;

    private boolean startStorm;

    @Autowired
    public GeomagneticStormUtil(GeomagneticStorm geomagneticStorm, EmailSetting emailSetting, GeomagneticStormModel stormModel) {
        this.geomagneticStorm = geomagneticStorm;
        this.emailSetting = emailSetting;
        this.stormModel = stormModel;
    }

    //проверка бури с индексом > 4 каждые 3 часа и отправка ссобщения на эл. почту
    @Scheduled(fixedRate = 30 * 1000 * 1000)
    public void checkStormEvery3Hour() {
        check(stormModel);
    }

    private void check(GeomagneticStormModel stormModel) {
        Sender sender = Sender.builder()
                .username(emailSetting.getEmailSender())
                .password(emailSetting.getPasswordSender())
                .build();

        StringBuilder description = new StringBuilder()
                .append("\nКачественно состояние магнитного поля в зависимости от Кp индекса\n")
                .append("Kp <= 2 — спокойное;\n")
                .append("Kp = 2, 3 — слабовозмущенное; \n")
                .append("Kp = 4 — возмущенное; \n")
                .append("Kp = 5, 6 — магнитная буря; \n")
                .append("Kp >= 7 — сильная магнитная буря.");

        try {
            GeomagneticStormModel stormModelForCheck = getStormModel(stormModel);
            if (stormModelForCheck.getKp_index() > 4) {
                startStorm = true;
                String storm = geomagneticStorm.getGeomagneticStorm();
                sender.send("Геомагнитная буря!", storm + description, emailSetting.getEmailSender());
            } else if (startStorm && stormModelForCheck.getKp_index() < 4) {
                startStorm = false;
                String storm = geomagneticStorm.getGeomagneticStorm();
                sender.send("Геомагнитная буря закончилась", storm, emailSetting.getEmailSender());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private GeomagneticStormModel getStormModel(GeomagneticStormModel stormModel) throws IOException {
        URL url = new URL(urlSetting.getUrlToGeomagneticSite());
        String jsonStringFormat = ReadJSONUtil.getJSONStringFormat(url);

        if (!jsonStringFormat.isEmpty()) {
            Gson gson = new Gson();

            Type collectionType = new TypeToken<Collection<GeomagneticStormModel>>() {
            }.getType();
            List<GeomagneticStormModel> listStormModel = gson.fromJson(jsonStringFormat, collectionType);

            //смотрим индекс на ближайшее время
            if (!listStormModel.isEmpty()) {
                stormModel = listStormModel.get(listStormModel.size() - 1);
                return stormModel;
            }

        }
        return stormModel;
    }
}
