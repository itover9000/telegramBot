package util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.GeomagneticStormModel;
import service.GeomagneticStorm;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collection;
import java.util.List;

public class GeomagneticStormUtil {
    private static GeomagneticStorm geomagneticStorm = new GeomagneticStorm();

    public static void checkStormEvery3Hour(GeomagneticStormModel stormModel) {
        Thread task = new Thread(() -> {
            while (true) {
                try {
//                    Thread.sleep(3 * 60 * 60 * 1000);
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                check(stormModel);
            }
        });
        task.start();

    }

    public static String check(GeomagneticStormModel stormModel) {
//        String geomagneticStorm = "132";
        String stormGeomagneticStorm = "нет данных";
        try {
            GeomagneticStormModel stormModelForCheck = getStormModel(stormModel);
            if (stormModelForCheck.getKp_index() > 2) {
                String storm = GeomagneticStormUtil.geomagneticStorm.getGeomagneticStorm(stormModel);
                System.out.println(storm);
                return storm;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stormGeomagneticStorm;
    }

    private static GeomagneticStormModel getStormModel(GeomagneticStormModel stormModel) throws IOException {
        URL url = new URL("https://services.swpc.noaa.gov/json/planetary_k_index_1m.json");
        String jsonStringFormat = ReadJSONUtil.getJSONStringFormat(url);

        if (!jsonStringFormat.isEmpty()) {
            Gson gson = new Gson();

            Type collectionType = new TypeToken<Collection<GeomagneticStormModel>>() {
            }.getType();
            List<GeomagneticStormModel> listStormModel = gson.fromJson(jsonStringFormat, collectionType);

            if (!listStormModel.isEmpty()) {
                stormModel = listStormModel.get(listStormModel.size() - 1);
                return stormModel;
            }

        }
        return stormModel;
    }
}
