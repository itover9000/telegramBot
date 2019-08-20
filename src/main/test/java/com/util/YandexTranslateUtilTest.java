package com.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YandexTranslateUtilTest {

    @Autowired
    private YandexTranslateUtil yandexTranslateUtil;

    @Test
    public void translateFromEnToRu() throws IOException {
        String helloWorld = yandexTranslateUtil.translateFromEngToRu("Hello world");
        assertEquals("Привет мир", new String(String.valueOf(helloWorld).getBytes(), StandardCharsets.UTF_8));
    }
}