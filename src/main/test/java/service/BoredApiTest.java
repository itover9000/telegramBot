package service;

import model.BoredModel;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.ReadJSONUtil;

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