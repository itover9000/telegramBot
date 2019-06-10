package com.service;

import com.model.BoredModel;
import com.util.ReadJSONUtil;
import com.util.YandexTranslateUtil;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Service
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

        //преобразование в UTF-8 (некорректная кодировка при запуске jar)
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(bored.getActivity());
        String activityInRussian = StandardCharsets.UTF_8.decode(byteBuffer).toString();

        return "Мероприятие: " + activityInRussian + "\n"
                + "Activity:  " + activity + "\n"
                + "Количество участников: " + bored.getParticipants();
    }
}
