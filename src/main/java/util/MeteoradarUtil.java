package util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MeteoradarUtil {
    private static final String link = "http://www.meteoinfo.by/radar/?q=UMMN&t=0";
    private static String title;

    public static String getImageFromUrl() throws IOException {
        Document doc = Jsoup.connect(link).get();
        Element table = doc.select("table").get(2); //select the third table.
        Elements rows = table.select("tr");

        title = rows.get(0).getElementsByTag("img").get(0).attr("title");
        //select absolute path from table
        String url = rows.get(0).getElementsByTag("img").get(0).absUrl("src");
        return url;
    }


    public static String getTimeFromSite() throws ParseException {
        try {
            getImageFromUrl();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (title != null && !title.isEmpty()) {
            String validTime = parseTitleFromTime(title);

            if (validTime != null && !validTime.isEmpty()) {
                //устанавливаем часовой пояс
                SimpleDateFormat isoFormat = new SimpleDateFormat("HH:mm");
                isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date date = isoFormat.parse(validTime);
                return date.getHours() + ":" + date.getMinutes();
            }
        }
        return "неверный формат";
    }

    private static String parseTitleFromTime(String text) {
        //возвращаем из текста время в формате HH:mm
        Time24HoursValidator validator = new Time24HoursValidator();
        String resultTime = "";
        String[] splitText = text.split(" ");
        for (String time : splitText) {
            if (validator.validate(time)) {
                resultTime = time;
                break;
            }
        }
        return resultTime;
    }

    public static String getPathToGifFile(String urlForDownloadGif) throws IOException {
        File file = new File("radar-map.gif");
        URL website = new URL(urlForDownloadGif);

        if (!file.exists()) {
            //если файла не существует, то качаем и копируем в корень проекта
            copyGifToRootProject(file, website);
        } else {
            long contentLengthGif = website.openConnection().getContentLengthLong();
            //если файл отличается по размеру, то заменяем на новый файл
            if (file.length() != contentLengthGif ) {
                copyGifToRootProject(file, website);
            }
        }
        return file.toString();
    }

    private static void copyGifToRootProject(File file, URL website) throws IOException {
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(file.toString());
        fos.getChannel().transferFrom(rbc, 0, 5 * 1024 * 1024);
    }

    public static void main(String[] args) throws IOException {
        getPathToGifFile("http://www.meteoinfo.by/radar/UMMN/radar-map.gif");
    }
}

