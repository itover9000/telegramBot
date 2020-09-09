package com.service;

import com.exception.NoDataOnSiteException;
import com.model.GeomagneticStormModel;
import com.util.GeomagneticStormUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class GeomagneticStorm {
    private final GeomagneticStormUtil geomagneticStormUtil;

    @Autowired
    public GeomagneticStorm(@Lazy GeomagneticStormUtil geomagneticStormUtil) {
        this.geomagneticStormUtil = geomagneticStormUtil;
    }

    public String getGeomagneticStorm(String url) throws IOException, NoDataOnSiteException {
        GeomagneticStormModel stormModelLastElement = geomagneticStormUtil.getStormModel(url);
        if (stormModelLastElement != null) {
            //check last kpIndex in List
            StringBuilder returnText = new StringBuilder()
                .append("Kp индекс шторма = ").append(stormModelLastElement.getKpIndex()).append("\n")
                .append("время ").append(parseGeomagneticStorm(stormModelLastElement.getTimeTag()));
            if (stormModelLastElement.getKpIndex() > 4) {
                returnText.append("\nВнимание, сильная буря!");
            }
            return String.valueOf(returnText);
        }
        return "не удалось получить данные";
    }

    private String parseGeomagneticStorm(String dateForParse) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        LocalDateTime localDateTime = LocalDateTime.parse(dateForParse, inputFormatter);
        //transform localDateTime to zonedDateTime adjusted UTC
        ZonedDateTime zonedDateTimeUtc = ZonedDateTime.of(localDateTime, ZoneOffset.UTC);
        // get zonedDateTime relative to the local time zone
        ZonedDateTime zonedDateTimeLocalMachine = zonedDateTimeUtc.withZoneSameInstant(ZoneId.systemDefault());
        return zonedDateTimeLocalMachine.format(outputFormatter);
    }

}
