package com.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class BoredApiTest {

    @Autowired
    private BoredApi boredApi;

    @Test
    void getBoredStringFormat() throws IOException {
        URL urlResource = ClassLoader.getSystemResource("bored.json");
        // read json from the resources, got the name of the event, the number of participants and translated
        String boredStringFormat = boredApi.getBoredStringFormat(urlResource.toString());

//        assertEquals(new String(String.valueOf(boredStringFormat).getBytes(), StandardCharsets.UTF_8), "Мероприятие: Фотосессия с друзьями\n" +
//                "Activity:  Have a photo session with some friends\n" +
//                "Количество участников: 4");
        assertEquals("Мероприятие: Фотосессия с друзьями\n" +
                "Activity:  Have a photo session with some friends\n" +
                "Количество участников: 4", boredStringFormat);
    }
}
