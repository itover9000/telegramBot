package com.util;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class YandexTranslateUtilTest {

    @Test
    public void translateFromEnToRu() throws IOException {
        String hello_world = YandexTranslateUtil.translateFromEnToRu("Hello world");
//        ByteBuffer encode = StandardCharsets.UTF_8.encode(hello_world);
//        String s = new String(hello_world.getBytes(), StandardCharsets.UTF_8);
        assertEquals(hello_world, "Привет мир");
    }
}