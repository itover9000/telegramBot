package com.util;

import com.model.BoredModel;
import com.model.GeomagneticStormModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TransformObjectFromJsonTest {

    @Autowired
    private TransformObjectFromJson<BoredModel> transformObjectFromJson;

    @Autowired
    private TransformObjectFromJson<GeomagneticStormModel> transformListObjectsFromJson;

    private List<GeomagneticStormModel> expectedList;

    @BeforeEach
    void setUp() {
        expectedList = new ArrayList<>();
        expectedList.add(new GeomagneticStormModel("2019-05-15T11:52:00", 0));
        expectedList.add(new GeomagneticStormModel("2019-05-15T11:53:00", 2));
        expectedList.add(new GeomagneticStormModel("2019-05-15T11:54:00", 1));
    }

    @AfterEach
    void tearDown() {
        expectedList = null;
    }

    @Test
    void getObjectFromJson() throws IOException {
        URL urlResource = ClassLoader.getSystemResource("bored.json");
        // transform BoredModel from json
        BoredModel boredModelFromJson = transformObjectFromJson.getObjectFromJson(urlResource, BoredModel.class);
        assertEquals("Have a photo session with some friends", boredModelFromJson.getActivity());
        assertEquals(4, boredModelFromJson.getParticipants());
        assertNotEquals("Other text", boredModelFromJson.getActivity());
        assertNotEquals(5, boredModelFromJson.getParticipants());
    }

    @Test
    void getListObjectsFromJson() throws IOException {
        URL urlResource = ClassLoader.getSystemResource("svpc.json");

        // check not null and size,
        List<GeomagneticStormModel> listObjectsFromJson = transformListObjectsFromJson.
                getListObjectsFromJson(urlResource, GeomagneticStormModel.class);
        assertNotNull(listObjectsFromJson);
        assertEquals(3, listObjectsFromJson.size());
        assertEquals(expectedList, listObjectsFromJson);
    }

    @Test
    void getJSONStringFormat() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        URL urlResource = ClassLoader.getSystemResource("bored.json");

        // invoke private method and compare text from json
        Method method = TransformObjectFromJson.class.getDeclaredMethod("getJSONStringFormat", URL.class);
        method.setAccessible(true);
        String invoke = (String) method.invoke(transformObjectFromJson, urlResource);
        String expected = "{\"activity\":\"Have a photo session with some friends\",\"accessibility\":0.8,\"type\":\"social\",\"participants\":4,\"price\":0.05,\"key\":\"3305912\"}";
        assertEquals(expected, invoke);
    }
}