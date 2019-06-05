package com.service;

import com.model.WeatherModel;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import com.util.ReadJSONUtil;

import java.io.IOException;
import java.net.URL;

@Service
public class Weather {
    // 6621cca782054fa6e402deac83420b10
    public static String getWeather(String message, WeatherModel weatherModel) throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="
                + message + "&units=metric&appid=6621cca782054fa6e402deac83420b10");

        JSONObject object = ReadJSONUtil.readJSONFromUrl(url);
        weatherModel.setName(object.getString("name"));

        JSONObject main = object.getJSONObject("main");
        weatherModel.setTemp(main.getDouble("temp"));
        weatherModel.setHumidity(main.getDouble("humidity"));

        JSONArray getArray = object.getJSONArray("weather");
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject obj = getArray.getJSONObject(i);
            weatherModel.setIcon(obj.getString("icon"));
            weatherModel.setMain(obj.getString("main"));
        }

        return "City: " + weatherModel.getName() + "\n" +
                "Temperature: " + weatherModel.getTemp() + "C" + "\n" +
                "Humidity:" + weatherModel.getHumidity() + "%" + "\n" +
                "Main: " + weatherModel.getMain() + "\n" +
                "http://openweathermap.org/img/w/" + weatherModel.getIcon() + ".png";
    }
}
