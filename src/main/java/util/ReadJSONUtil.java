package util;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class ReadJSONUtil {
    public static JSONObject readJSONFromUrl(URL url) throws IOException {
        String result = readJsonStringFormat(url);
//        if (result.substring(0,1).equals("[") && result.substring(result.length()-1, result.length()).equals("]")){
//            //удаляем первый и последник символ, если json начинается и заканчивается на [ ]
//            result.deleteCharAt(0)
//                    .deleteCharAt(result.length()-1);
//        }
        return new JSONObject(result);
    }

    public static String getJSONStringFormat(URL url) throws IOException {
        return readJsonStringFormat(url);
    }

    private static String readJsonStringFormat(URL url) throws IOException {
        Scanner in = new Scanner((InputStream) url.getContent());
        StringBuilder result = new StringBuilder();
        while (in.hasNext()) {
            result.append(in.nextLine());
        }
        return String.valueOf(result);
    }
}
