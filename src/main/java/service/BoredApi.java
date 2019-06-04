package service;

import model.BoredModel;
import org.json.JSONObject;
import util.ReadJSONUtil;
import util.YandexTranslateUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class BoredApi {
    private final String urlPath = "https://www.boredapi.com/api/activity";
    private URL url = new URL(urlPath);

    public BoredApi( URL url) throws MalformedURLException {
        this.url = url;
    }

    public BoredApi() throws MalformedURLException {
    }

    public  String getBoredStringFormat(BoredModel bored) throws IOException {
//        url = new URL(urlPath);
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
