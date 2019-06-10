package com.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YandexTranslateUtilTest {

    @Test
    public void translateFromEnToRu() throws IOException {
        String hello_world = YandexTranslateUtil.translateFromEnToRu("Hello world");
//        ByteBuffer encode = StandardCharsets.UTF_8.encode(hello_world);
//        String s = new String(hello_world.getBytes(), StandardCharsets.UTF_8);
        assertEquals(hello_world, "Привет мир");
    }
}