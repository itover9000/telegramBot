package com.util;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;

import static org.junit.Assert.*;

public class MeteoradarUtilTest {


    @Test
    public void getImageFromUrl() throws IOException {
        //http://www.meteoinfo.by/radar/UMMN/UMMN_1559557200.png
        String link = MeteoradarUtil.getLink();

        //проверка на доступность ссылки(код 200) "http://www.meteoinfo.by/radar/?q=UMMN&t=0";
        URL url = new URL(link);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        int responseCode = urlConnection.getResponseCode();
        assertEquals(responseCode, 200);

        //проверка на доступность ссылки(код 200) http://www.meteoinfo.by/radar/UMMN/UMMN_1559557200.png
        String urlToImage = MeteoradarUtil.getImageFromUrl();
        URL linkToImage = new URL(link);
        HttpURLConnection urlToImagePng = (HttpURLConnection) linkToImage.openConnection();
        int responseCodeImage = urlToImagePng.getResponseCode();
        assertEquals(responseCodeImage, 200);

        //проверка на корректность возвращаемой ссылки
        assertTrue(urlToImage.endsWith(".png"));
        assertTrue(urlToImage.startsWith("http://www.meteoinfo.by/radar"));

    }

    @Test
    public void getTimeFromSite() throws ParseException {
        String timeFromSiteWithNewTime = MeteoradarUtil.getTimeFromSiteWithNewTime("13:10");
        assertEquals(timeFromSiteWithNewTime, "16:10");
    }

    @Test
    public void parseTitleFromTime() {
        String title = "Метеорадар UMMN.Радиолокационная карта метеоявлений. Дата и время формирования карты: 03.06 в 10:00 UTC.";
        String timeFormatHHmm = MeteoradarUtil.parseTitleFromTime(title);
        assertEquals(timeFormatHHmm, "10:00");
    }

    @Test
    public void getPathToGifFile() throws IOException {

        //проверка на доступность ссылки(код 200) "http://www.meteoinfo.by/radar/UMMN/radar-map.gif"
        URL linkToImage = new URL("http://www.meteoinfo.by/radar/UMMN/radar-map.gif");
        HttpURLConnection urlToImagePng = (HttpURLConnection) linkToImage.openConnection();
        int responseCodeImage = urlToImagePng.getResponseCode();
        assertEquals(responseCodeImage, 200);

        String pathToGifFile = MeteoradarUtil.getPathToGifFile("http://www.meteoinfo.by/radar/UMMN/radar-map.gif");
        File file = new File(pathToGifFile);
        assertTrue(file.exists());
        assertEquals(pathToGifFile, "radar-map.gif");
        assertTrue(file.length() > 3000);
    }
}