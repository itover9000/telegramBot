package com.service;

import com.exception.NoDataOnSiteException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class GeomagneticStormTest {

    @Autowired
    private GeomagneticStorm geomagneticStorm;

    @Test
    void getGeomagneticStorm() throws NoDataOnSiteException, IOException {
        URL urlResource = ClassLoader.getSystemResource("svpc.json");
        String answerFromJson = this.geomagneticStorm.getGeomagneticStorm(urlResource.toString());
        String expected = "Kp индекс шторма = 1\n" +
                "время 15.05.2019 14:54:00";

        assertEquals(expected, answerFromJson);
    }

    @Test
    void parseGeomagneticStorm() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String formatDate = "2019-05-15T11:54:00";

        Method method = GeomagneticStorm.class.getDeclaredMethod("parseGeomagneticStorm", String.class);
        method.setAccessible(true);
        String returnNewFormat = (String) method.invoke(geomagneticStorm, formatDate);
        String expected = "15.05.2019 14:54:00";

        // compare expected and parse time
        assertEquals(expected, returnNewFormat);
    }
}