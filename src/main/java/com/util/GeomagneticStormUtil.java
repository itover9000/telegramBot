package com.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.model.GeomagneticStormModel;
import com.service.GeomagneticStorm;
import com.service.MailSender;
import com.settings.MailSenderSetting;
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

    @Autowired
    private MailSenderSetting emailSetting;

    @Autowired
    private UrlSetting urlSetting;

    @Autowired
    private MailSender mailSender;

    private final GeomagneticStorm geomagneticStorm;

    private final GeomagneticStormModel stormModel;

    private boolean startStorm;

    @Autowired
    public GeomagneticStormUtil(GeomagneticStorm geomagneticStorm, GeomagneticStormModel stormModel) {
        this.geomagneticStorm = geomagneticStorm;
        this.stormModel = stormModel;
    }

    // check storm every 15 minutes, if kpIndex > 4, then will be sent message to email
    @Scheduled(fixedRate = 15 * 1000 * 1000)
    public void checkStormEvery3Hour() {
        check(stormModel);
    }

    private void check(GeomagneticStormModel stormModel) {

        StringBuilder description = new StringBuilder()
                .append("\nКачественно состояние магнитного поля в зависимости от Кp индекса\n")
                .append("Kp <= 2 — спокойное;\n")
                .append("Kp = 2, 3 — слабовозмущенное; \n")
                .append("Kp = 4 — возмущенное; \n")
                .append("Kp = 5, 6 — магнитная буря; \n")
                .append("Kp >= 7 — сильная магнитная буря.");

        try {
            GeomagneticStormModel stormModelForCheck = getStormModel(stormModel);
            if (stormModelForCheck.getKpIndex() > 4) {
                startStorm = true;
                String storm = geomagneticStorm.getGeomagneticStorm();
                mailSender.send(emailSetting.getEmailRecipient(), "Геомагнитная буря!", storm + description);
            } else if (startStorm && stormModelForCheck.getKpIndex() < 4) {
                startStorm = false;
                String storm = geomagneticStorm.getGeomagneticStorm();
                mailSender.send(emailSetting.getEmailRecipient(), "Геомагнитная буря закончилась", storm);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private GeomagneticStormModel getStormModel(GeomagneticStormModel stormModel) throws IOException {
        URL url = new URL(urlSetting.getUrlToGeomagneticSite());
        String jsonStringFormat = ReadJSONUtil.getJSONStringFormat(url);

        if (!jsonStringFormat.isEmpty()) {
            Gson gson =new Gson();

            Type collectionType = new TypeToken<Collection<GeomagneticStormModel>>() {
            }.getType();
            // get the List<GeomagneticStormModel> from json
            List<GeomagneticStormModel> listStormModel = gson.fromJson(jsonStringFormat, collectionType);

            //return last kpIndex
            if (!listStormModel.isEmpty()) {
                stormModel = listStormModel.get(listStormModel.size() - 1);
                return stormModel;
            }
        }
        return stormModel;
    }
}
