package util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.GeomagneticStormModel;
import service.GeomagneticStorm;
import service.Sender;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GeomagneticStormUtil {
    private static GeomagneticStorm geomagneticStorm = new GeomagneticStorm();
    private static Sender sender = new Sender("h.dabravolskay@yandex.ru", "tratata88");

    //проверка бури с индексом > 4 каждые 3 часа и отправка ссобщения на эл. почту
    public static void checkStormEvery3Hour(GeomagneticStormModel stormModel) {

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> check(stormModel), 0, 3, TimeUnit.HOURS);
    }

    private static String check(GeomagneticStormModel stormModel) {
        String stormGeomagneticStorm = "нет данных";
        try {
            GeomagneticStormModel stormModelForCheck = getStormModel(stormModel);
            if (stormModelForCheck.getKp_index() > 4) {
                String storm = GeomagneticStormUtil.geomagneticStorm.getGeomagneticStorm(stormModel);
                sender.send("Геомагнитная буря!", storm, "h.dabravolskay@yandex.ru", "over9000@tut.by");
//                System.out.println(storm);
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
