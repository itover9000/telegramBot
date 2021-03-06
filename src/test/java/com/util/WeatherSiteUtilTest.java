package com.util;

import com.exception.InvalidUrlException;
import com.exception.NoDataOnSiteException;
import com.settings.UrlSetting;
import org.junit.gen5.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.gen5.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class WeatherSiteUtilTest {

    @Autowired
    private WeatherSiteUtil weatherSiteUtil;

    @Autowired
    private UrlSetting urlSetting;

    private String title;

    @Test
    void getImageFromUrl() throws IOException, InvalidUrlException, NoDataOnSiteException {
        //http://www.meteoinfo.by/radar/UMMN/UMMN_1559557200.png
        String link = urlSetting.getUrlMainPageMeteoinfo();

        //link availability check (code 200) "http://www.meteoinfo.by/radar/?q=UMMN&t=0";
        URL url = new URL(link);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        int responseCode = urlConnection.getResponseCode();
        assertEquals(200, responseCode);

        //link availability check (code 200) http://www.meteoinfo.by/radar/UMMN/UMMN_1559557200.png
        String urlToImage = weatherSiteUtil.getImageFromUrl();
        URL linkToImage = new URL(link);
        HttpURLConnection urlToImagePng = (HttpURLConnection) linkToImage.openConnection();
        int responseCodeImage = urlToImagePng.getResponseCode();
        assertEquals(200, responseCodeImage);

        //checking the correctness of the returned link
        assertTrue(urlToImage.endsWith(".png"));
        assertTrue(urlToImage.startsWith("http://www.meteoinfo.by/radar"));
    }

    @BeforeEach
    void setUp() {
        title = "Метеорадар UMMN.Радиолокационная карта метеоявлений. Дата и время формирования карты: 03.06 в 10:00 UTC.";
    }

    @AfterEach
    void tearDown() {
        title = null;
    }

    @Test
    void getPathToFileInRootProject() throws IOException, InvalidUrlException {
        //link availability check (code 200) "http://www.meteoinfo.by/radar/UMMN/radar-map.gif"
        URL linkToImage = new URL(urlSetting.getUrlToGifFile());
        HttpURLConnection urlToImagePng = (HttpURLConnection) linkToImage.openConnection();
        int responseCodeImage = urlToImagePng.getResponseCode();
        Assertions.assertEquals(200, responseCodeImage);

        String pathToGifFile = weatherSiteUtil.getPathToFileInRootProject(
                urlSetting.getUrlToGifFile(),
                urlSetting.getGifFileNameFromMeteoinfo());
        File file = new File(pathToGifFile);
        assertTrue(file.exists());
        assertEquals(pathToGifFile, urlSetting.getGifFileNameFromMeteoinfo());
        assertTrue(file.length() > 3000);
    }

    @Test
    void parseTitleForGettingTime() {
        String timeFormatHHmm = weatherSiteUtil.parseTitleForGettingTime(title);
        assertEquals("10:00", timeFormatHHmm);
    }

    @Test
    void parseTitleForGettingDate() {
        String dateTime = weatherSiteUtil.parseTitleForGettingDate(title);
        assertEquals("03.06", dateTime);
    }
}