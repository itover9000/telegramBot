package pojo;

import model.ModelWeather;
import org.json.JSONArray;
import org.json.JSONObject;
import util.ReadJSONUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Weather {
    // 6621cca782054fa6e402deac83420b10
    public static String getWeather(String message, ModelWeather modelWeather) throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="
                + message + "&units=metric&appid=6621cca782054fa6e402deac83420b10");

        JSONObject object = ReadJSONUtil.readJSONFromUrl(url);
        modelWeather.setName(object.getString("name"));

        JSONObject main = object.getJSONObject("main");
        modelWeather.setTemp(main.getDouble("temp"));
        modelWeather.setHumidity(main.getDouble("humidity"));

        JSONArray getArray = object.getJSONArray("weather");
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject obj = getArray.getJSONObject(i);
            modelWeather.setIcon(obj.getString("icon"));
            modelWeather.setMain(obj.getString("main"));
        }

        return "City: " + modelWeather.getName() + "\n" +
                "Temperature: " + modelWeather.getTemp() + "C" + "\n" +
                "Humidity:" + modelWeather.getHumidity() + "%" + "\n" +
                "Main: " + modelWeather.getMain() + "\n" +
                "http://openweathermap.org/img/w/" + modelWeather.getIcon() + ".png";
    }
}
