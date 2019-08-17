package com.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.model.GeomagneticStormModel;
import com.settings.UrlSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.util.ReadJSONUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
public class GeomagneticStorm {

    @Autowired
    private UrlSetting urlSetting;

    private String parseGeomagneticStorm(String dateForParse) {
        //set pattern
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date result;
        SimpleDateFormat format = new SimpleDateFormat();
        try {
            result = df.parse(dateForParse);
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

            //set TimeZone Minsk
            sdf.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Etc/GMT-6")));
            format = new SimpleDateFormat(sdf.format(result));

            return format.toLocalizedPattern();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format.toLocalizedPattern();
    }

    public String getGeomagneticStorm() throws IOException {

        URL url = new URL(urlSetting.getUrlToGeomagneticSite());
        String jsonStringFormat = ReadJSONUtil.getJSONStringFormat(url);

        if (!jsonStringFormat.isEmpty()) {
            Gson gson = new Gson();

            // get List<GeomagneticStormModel> from json
            Type collectionType = new TypeToken<Collection<GeomagneticStormModel>>() {
            }.getType();
            List<GeomagneticStormModel> listStormModel = gson.fromJson(jsonStringFormat, collectionType);

            if (!listStormModel.isEmpty()) {

                //check last kpIndex in List
                GeomagneticStormModel stormModel = listStormModel.get(listStormModel.size() - 1);

                StringBuilder returnText = new StringBuilder()
                        .append("Kp индекс шторма = ").append(stormModel.getKpIndex()).append("\n")
                        .append("время ").append(parseGeomagneticStorm(stormModel.getTimeTag()));

                if (stormModel.getKpIndex() > 4) {
                    returnText.append("\nВнимание, сильная буря!");
                }
                return String.valueOf(returnText);
            }
        }
        return "не удалось получить данные";
    }
}
