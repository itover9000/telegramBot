package com.util;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ReadJSONUtilTest {

    @Test
    public void getJSONStringFormat() throws IOException {
        String jsonStringFormat = ReadJSONUtil.getJSONStringFormat(ClassLoader.getSystemResource("bored.json"));
        assertEquals(jsonStringFormat,
                "{\"activity\":\"Have a photo session with some friends\"," +
                        "\"accessibility\":0.8," +
                        "\"type\":\"social\"," +
                        "\"participants\":4," +
                        "\"price\":0.05," +
                        "\"key\":\"3305912\"}");
    }
}