package com.service;

import com.exception.InvalidDataFormatException;
import com.exception.NoDataOnSiteException;
import com.model.GeomagneticStormModel;
import com.settings.UrlSetting;
import com.util.GeomagneticStormUtil;
import com.util.TransformObjectFromJson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

@Service
public class GeomagneticStorm {
    private static final Logger logger = LogManager.getLogger(GeomagneticStorm.class);
    private static final String ERROR_MESSAGE = "An exception occurred!";

    @Autowired
    private TransformObjectFromJson<GeomagneticStormModel> transformObjectFromJson;

    @Autowired
    private UrlSetting urlSetting;

    @Autowired
    private GeomagneticStormUtil geomagneticStormUtil;

    private String parseGeomagneticStorm(String dateForParse) throws InvalidDataFormatException {
        //set pattern
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date result;
        try {
            result = df.parse(dateForParse);
        } catch (ParseException e) {
            logger.error(ERROR_MESSAGE, e);
            throw new InvalidDataFormatException("Invalid data format");
        }
        // set new time pattern
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        //set TimeZone Minsk
        sdf.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Etc/GMT-6")));
        SimpleDateFormat format = new SimpleDateFormat(sdf.format(result));

        return format.toLocalizedPattern();
    }

    public String getGeomagneticStorm() throws IOException, InvalidDataFormatException, NoDataOnSiteException {
        GeomagneticStormModel stormModelLastElement = geomagneticStormUtil.getStormModel();
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
}
