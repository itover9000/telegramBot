/*
package com.service;

import com.model.BoredModel;
import com.settings.UrlSetting;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BoredApiTest {

    @Autowired
    private UrlSetting urlSetting;

    @Autowired
    private BoredModel model;

    @Autowired
    private BoredApi boredApi;

    @Test
    public void getBoredStringFormat() throws IOException, NoSuchMethodException {

        BoredApi boredApiRefl = new BoredApi(urlSetting);
        Method getBoredStringFormat = boredApiRefl.getClass().getMethod("getBoredStringFormat");
//        getBoredStringFormat.


        URL urlResource = ClassLoader.getSystemResource("bored.json");
//        boredApi = new BoredApi(urlResource);
//        boredApi = new BoredApi(urlSetting);

        // read json from the resources, got the name of the event, the number of participants and translated
        String boredStringFormat = boredApi.getBoredStringFormat();

        assertEquals(new String(String.valueOf(boredStringFormat).getBytes(), StandardCharsets.UTF_8), "Мероприятие: Фотосессия с друзьями\n" +
                "Activity:  Have a photo session with some friends\n" +
                "Количество участников: 4");

    }
}
*/
