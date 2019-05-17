package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.GeomagneticStormModel;
import util.ReadJSONUtil;

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

public class GeomagneticStorm {

    private static String parseGeomagneticStorm(String dateForParse) {
        //set pattern
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date result;
        SimpleDateFormat format = new SimpleDateFormat();
        try {
            result = df.parse(dateForParse);
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            //set TimeZone Minsk
            sdf.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Europe/Minsk")));
            format = new SimpleDateFormat(sdf.format(result));

            return format.toLocalizedPattern();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format.toLocalizedPattern();
    }

    public String getGeomagneticStorm(GeomagneticStormModel stormModel) throws IOException {

        URL url = new URL("https://services.swpc.noaa.gov/json/planetary_k_index_1m.json");
        String jsonStringFormat = ReadJSONUtil.getJSONStringFormat(url);

        if (!jsonStringFormat.isEmpty()) {
            Gson gson = new Gson();

            Type collectionType = new TypeToken<Collection<GeomagneticStormModel>>() {
            }.getType();
            List<GeomagneticStormModel> listStormModel = gson.fromJson(jsonStringFormat, collectionType);

            if (!listStormModel.isEmpty()) {
                stormModel = listStormModel.get(listStormModel.size() - 1);
                String returnText = "Kp индекс шторма = " + stormModel.getKp_index() + "\n" +
                        "время " + parseGeomagneticStorm(stormModel.getTime_tag());
                if (stormModel.getKp_index() > 4){
                    returnText += "\nВнимание, сильная буря!";
                }
                return returnText;
            }
        }
        return "не удалось получить данные";
    }


}
