package com.util;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;


@Service
public class ReadJSONUtil {

    private ReadJSONUtil() {
    }

    public static JSONObject readJSONFromUrl(URL url) throws IOException {
        String result = getJSONStringFormat(url);

        return new JSONObject(result);
    }

    //return json on String format
    public static String getJSONStringFormat(URL url) throws IOException {
        Scanner in = new Scanner((InputStream) url.getContent());
        StringBuilder result = new StringBuilder();
        while (in.hasNext()) {
            result.append(in.nextLine());
        }
        return result.toString();

    }
}
