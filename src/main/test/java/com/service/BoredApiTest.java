package com.service;

import com.model.BoredModel;
import com.settings.UrlSetting;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoredApiTest {

    @Autowired
    private UrlSetting urlSetting;

    @Autowired
    private BoredModel model;

    @Autowired
    private BoredApi boredApi;

    @Test
    public void getBoredStringFormat() throws IOException {

        URL urlResource = ClassLoader.getSystemResource("bored.json");
//        boredApi = new BoredApi(urlResource);
//        boredApi = new BoredApi(urlSetting);

        //прочитали json из ресурсов, достали название мероприятия, количество участников и перевели
        String boredStringFormat = boredApi.getBoredStringFormat();

        assertEquals(boredStringFormat, "Мероприятие: Фотосессия с друзьями\n" +
                "Activity:  Have a photo session with some friends\n" +
                "Количество участников: 4");

    }
}