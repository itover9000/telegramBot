package com.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

@Service
public class TransformObjectFromJson<T> {
    private final Gson gson;

    @Autowired
    public TransformObjectFromJson(Gson gson) {
        this.gson = gson;
    }

    public T getObjectFromJson(URL url, Class<T> modelClass) throws IOException {
        String jsonStringFormat = getJSONStringFormat(url);
        Type type = TypeToken.getParameterized(modelClass).getType();
        return gson.fromJson(jsonStringFormat, type);
    }

    public List<T> getListObjectsFromJson(URL url, Class<T> modelClass) throws IOException {
        String jsonStringFormat = getJSONStringFormat(url);
        // set list token
        Type type = TypeToken.getParameterized(List.class, modelClass).getType();
        return gson.fromJson(jsonStringFormat, type);
    }

    private String getJSONStringFormat(URL url) throws IOException {
        Scanner in = new Scanner((InputStream) url.getContent(), StandardCharsets.UTF_8);
        StringBuilder result = new StringBuilder();
        while (in.hasNext()) {
            result.append(in.nextLine());
        }
        return result.toString();
    }
}
