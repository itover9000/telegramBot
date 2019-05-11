package pojo;

import model.ModelBored;
import org.json.JSONObject;
import util.ReadJSONUtil;
import util.YandexTranslateUtil;

import java.io.IOException;
import java.net.URL;

public class BoredApi {
    private static final String urlPath = "https://www.boredapi.com/api/activity";

    public static String getBored(ModelBored bored) throws IOException {
        URL url = new URL(urlPath);
        JSONObject object = ReadJSONUtil.readJSONFromUrl(url);


        String activity = object.getString("activity");
        int participants = object.getInt("participants");
        bored.setActivity(YandexTranslateUtil.translateFromEnToRu(activity));
//        bored.setActivity(activity);
        bored.setParticipants(participants);

        return "Мероприятие: " + bored.getActivity() + "\n"
                + "Activity:  " + activity + "\n"
                + "Количество участников: " + bored.getParticipants();
    }
}
