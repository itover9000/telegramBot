package com.service;

import com.model.BoredModel;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;

public class BoredApiTest {


    @Test
    public void getBoredStringFormat() throws IOException {
        BoredModel model = new BoredModel();

        URL urlResource = ClassLoader.getSystemResource("bored.json");
        BoredApi boredApi = new BoredApi(urlResource);

        //прочитали json из ресурсов, достали название мероприятия, количество участников и перевели
        String boredStringFormat = boredApi.getBoredStringFormat(model);

        assertEquals(boredStringFormat, "Мероприятие: Фотосессия с друзьями\n" +
                "Activity:  Have a photo session with some friends\n" +
                "Количество участников: 4");

    }
}