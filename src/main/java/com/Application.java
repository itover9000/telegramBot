package com;

import com.model.GeomagneticStormModel;
import com.util.GeomagneticStormUtil;
import com.util.MeteoradarUtil;
import com.util.Time24HoursValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(Application.class, args);

//        new MeteoradarUtil(new Time24HoursValidator()).wtf();

        //проверка бури с индексом > 4 каждые 3 часа и отправка ссобщения на эл. почту
        GeomagneticStormUtil.checkStormEvery3Hour(new GeomagneticStormModel());
    }
}
