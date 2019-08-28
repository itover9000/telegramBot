package com.util;

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
    private static final Logger LOGGER = LogManager.getLogger(GeomagneticStormUtil.class);
    private static final String MESSAGE_LOGGER = "An exception occurred!";
    private static final int INDEX_THRESHOLD = 4;

    private final MailSenderSetting emailSetting;
    private final TransformObjectFromJson<GeomagneticStormModel> transformObjectFromJson;
    private final UrlSetting urlSetting;
    private final MailSender mailSender;
    private final GeomagneticStorm geomagneticStorm;
    private boolean isStorm;

    @Autowired
    public GeomagneticStormUtil(GeomagneticStorm geomagneticStorm, MailSenderSetting emailSetting,
                                TransformObjectFromJson<GeomagneticStormModel> transformObjectFromJson,
                                UrlSetting urlSetting, MailSender mailSender) {
        this.geomagneticStorm = geomagneticStorm;
        this.emailSetting = emailSetting;
        this.transformObjectFromJson = transformObjectFromJson;
        this.urlSetting = urlSetting;
        this.mailSender = mailSender;
    }

    public GeomagneticStormModel getStormModel(String url) throws IOException, NoDataOnSiteException {
        URL urlStorm = new URL(url);
        List<GeomagneticStormModel> listStormModelFromJson = transformObjectFromJson.getListObjectsFromJson(urlStorm, GeomagneticStormModel.class);

        return listStormModelFromJson.stream().reduce((first, second) -> second)
                .orElseThrow(() -> new NoDataOnSiteException(MESSAGE_LOGGER));
    }

    // check storm every 15 minutes, if kpIndex > 4, then will be sent message to email
    @Scheduled(fixedRate = 15 * 1000 * 1000)
    private void check() {
        StringBuilder description = new StringBuilder()
                .append("\nКачественно состояние магнитного поля в зависимости от Кp индекса\n")
                .append("Kp <= 2 — спокойное;\n")
                .append("Kp = 2, 3 — слабовозмущенное; \n")
                .append("Kp = 4 — возмущенное; \n")
                .append("Kp = 5, 6 — магнитная буря; \n")
                .append("Kp >= 7 — сильная магнитная буря.");

        try {
            GeomagneticStormModel stormModelForCheck = getStormModel(urlSetting.getUrlToGeomagneticSite());
            if (stormModelForCheck.getKpIndex() > INDEX_THRESHOLD) {
                isStorm = true;
                String storm = geomagneticStorm.getGeomagneticStorm(urlSetting.getUrlToGeomagneticSite());
                mailSender.send(emailSetting.getEmailRecipient(), "Геомагнитная буря!", storm + description);
            } else if (isStorm && stormModelForCheck.getKpIndex() < INDEX_THRESHOLD) {
                isStorm = false;
                String storm = geomagneticStorm.getGeomagneticStorm(urlSetting.getUrlToGeomagneticSite());
                mailSender.send(emailSetting.getEmailRecipient(), "Геомагнитная буря закончилась", storm);
            }
        } catch (IOException | NoDataOnSiteException e) {
            LOGGER.error(MESSAGE_LOGGER, e);
        }
    }
}
