package com;

import com.model.GeomagneticStormModel;
import com.util.GeomagneticStormUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(Application.class, args);

        //проверка бури с индексом > 4 каждые 3 часа и отправка ссобщения на эл. почту
        GeomagneticStormUtil.checkStormEvery3Hour(new GeomagneticStormModel());
    }
}
