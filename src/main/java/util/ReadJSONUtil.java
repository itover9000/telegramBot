package util;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class ReadJSONUtil {
    public static JSONObject readJSONFromUrl(URL url) throws IOException {
        String result = getJSONStringFormat(url);

        return new JSONObject(result);
    }

    //возвращаем json  в формате String
    public static String getJSONStringFormat(URL url) throws IOException {
        Scanner in = new Scanner((InputStream) url.getContent());
        StringBuilder result = new StringBuilder();
        while (in.hasNext()) {
            result.append(in.nextLine());
        }
        return String.valueOf(result);

    }
}
