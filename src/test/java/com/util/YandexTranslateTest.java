package com.util;

import com.service.YandexTranslate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class YandexTranslateTest {

    @Autowired
    private YandexTranslate yandexTranslate;

    @Test
    void translateFromEnToRu() throws IOException {
        //compare translate
        String helloWorld = yandexTranslate.translateFromEngToRu("Hello world");
        assertEquals("Привет мир", helloWorld);
    }
}