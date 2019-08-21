package com.util;

import com.exception.InvalidDataFormatException;
import com.exception.NoDataOnSiteException;
import com.model.GeomagneticStormModel;
import com.service.GeomagneticStorm;
import com.service.MailSender;
import com.settings.MailSenderSetting;
import com.settings.UrlSetting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@Service
public class GeomagneticStormUtil {
    private static final Logger logger = LogManager.getLogger(GeomagneticStormUtil.class);
    private static final String MESSAGE_LOGGER = "An exception occurred!";

    @Autowired
    private MailSenderSetting emailSetting;

    @Autowired
    private TransformObjectFromJson<GeomagneticStormModel> transformObjectFromJson;

    @Autowired
    private UrlSetting urlSetting;

    @Autowired
    private MailSender mailSender;

    private final GeomagneticStorm geomagneticStorm;

    private boolean isStorm;

    @Autowired
    public GeomagneticStormUtil(GeomagneticStorm geomagneticStorm) {
        this.geomagneticStorm = geomagneticStorm;
    }

    // check storm every 15 minutes, if kpIndex > 4, then will be sent message to email
    @Scheduled(fixedRate = 15 * 1000 * 1000)
    public void checkStormEvery15Minutes() {
        check();
    }

    private void check() {

        StringBuilder description = new StringBuilder()
                .append("\nКачественно состояние магнитного поля в зависимости от Кp индекса\n")
                .append("Kp <= 2 — спокойное;\n")
                .append("Kp = 2, 3 — слабовозмущенное; \n")
                .append("Kp = 4 — возмущенное; \n")
                .append("Kp = 5, 6 — магнитная буря; \n")
                .append("Kp >= 7 — сильная магнитная буря.");

        try {
            GeomagneticStormModel stormModelForCheck = getStormModel();
            if (stormModelForCheck.getKpIndex() > 4) {
                isStorm = true;
                String storm = geomagneticStorm.getGeomagneticStorm();
                mailSender.send(emailSetting.getEmailRecipient(), "Геомагнитная буря!", storm + description);
            } else if (isStorm && stormModelForCheck.getKpIndex() < 4) {
                isStorm = false;
                String storm = geomagneticStorm.getGeomagneticStorm();
                mailSender.send(emailSetting.getEmailRecipient(), "Геомагнитная буря закончилась", storm);
            }
        } catch (IOException | NoDataOnSiteException | InvalidDataFormatException e) {
            logger.error(MESSAGE_LOGGER, e);
        }
    }

    public GeomagneticStormModel getStormModel() throws IOException, NoDataOnSiteException {
        URL url = new URL(urlSetting.getUrlToGeomagneticSite());

        List<GeomagneticStormModel> listStormModelFromJson = transformObjectFromJson.getListObjectsFromJson(url, GeomagneticStormModel.class);

        GeomagneticStormModel stormModelFromJson = listStormModelFromJson.stream().reduce((first, second) -> second).orElse(null);
        if (stormModelFromJson != null) {
            //return last kpIndex
            return stormModelFromJson;
        } else throw new NoDataOnSiteException(MESSAGE_LOGGER);
    }


}
