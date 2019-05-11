package util;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;

public class YandexTranslateUtil {
     private static final String key = "key=trnsl.1.1.20190511T083532Z.659a23627272fcf1.ab2b6c844422017a936d29c902783ebb3659700a";
     private static final String url ="https://translate.yandex.net/api/v1.5/tr.json/translate?";


    public static String translateFromEnToRu(String text) throws IOException {
        String textForUrlAndLanguage = ("&text=" + text + "&lang=en-ru").replaceAll(" ", "%20");
        URL urlObj = new URL(url + key  + textForUrlAndLanguage);

        JSONObject object = ReadJSONUtil.readJSONFromUrl(urlObj);
        JSONArray jsonArray = object.getJSONArray("text");
        StringBuilder translate= new StringBuilder();
        for (int i = 0; i < jsonArray.length(); i++) {
            translate.append(jsonArray.getString(i));
        }
        return String.valueOf(translate);
    }

}