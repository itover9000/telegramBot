package com.util;

import com.exception.NoDataOnSiteException;
import com.model.GeomagneticStormModel;
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
class GeomagneticStormUtilTest {

    @Autowired
    private GeomagneticStormUtil geomagneticStormUtil;

    @Test
    void getStormModel() throws IOException, NoDataOnSiteException {
        URL urlResource = ClassLoader.getSystemResource("svpc.json");

        //get last StormModel from list
        GeomagneticStormModel stormModel = geomagneticStormUtil.getStormModel(urlResource.toString());

        assertEquals("2019-05-15T11:54:00", stormModel.getTimeTag());
        assertEquals(1, stormModel.getKpIndex());
    }
}