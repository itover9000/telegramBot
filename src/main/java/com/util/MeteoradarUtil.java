package com.util;

import org.apache.http.client.utils.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


@Service
public class MeteoradarUtil {

    private final Time24HoursValidator validator;

    private final String link = "http://www.meteoinfo.by/radar/?q=UMMN&t=0";
    private String title;

    @Autowired
    public MeteoradarUtil(Time24HoursValidator validator) {
        this.validator = validator;
    }

    public String getImageFromUrl() throws IOException {
        Document doc = Jsoup.connect(link).get();
        Element table = doc.select("table").get(2); //select the third table.
        Elements rows = table.select("tr");

        title = rows.get(0).getElementsByTag("img").get(0).attr("title");
        //select absolute path from table
        String url = rows.get(0).getElementsByTag("img").get(0).absUrl("src");
        return url;
    }


    public String getTimeFromSite() throws ParseException {
        try {
            getImageFromUrl();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getTimeFromSiteWithNewTime(title);
    }

    public String getTimeFromSiteWithNewTime(String title) throws ParseException {
        if (title != null && !title.isEmpty()) {
            //олучаем время в формате "HH:mm"  16:35
            String validTime = parseTitleForGettingTime(title);

            //получаем дату в формате dd.MM 13.06
            String validDate = parseTitleForGettingDate(title);

            if (validTime != null && !validTime.isEmpty() && validDate != null && !validDate.isEmpty()) {
                /*//устанавливаем часовой пояс
                SimpleDateFormat customFormat = new SimpleDateFormat("HH:mm");
                customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                //получили дату с новым часовым поясом
                Date date = customFormat.parse(validTime);

                //вывод времени в формате HH:mm в корректном виде (раньше  16:00 выводило как 16:0, теперь корректно)
                return new SimpleDateFormat("HH:mm").format(date);*/
//
//                //получаем время в формате "HH:mm"
//                String timeHHmm = parseTitleForGettingTime(title);
//
//                //получаем дату в формате dd.MM
//                String dateddMM = parseTitleForGettingDate(title);

                //получаем текущий год
                Calendar calendar = Calendar.getInstance();   // Gets the current date and time
                int year = calendar.get(Calendar.YEAR);       // The current year

                //устанавливаем формат времени
                SimpleDateFormat format = new SimpleDateFormat("HH:mm dd.MM.yyyy");
                //устанавливаем часовой пояс
                format.setTimeZone(TimeZone.getTimeZone("UTC"));

                //парсим входящее время в формате "HH:mm dd.MM.yyyy"
                Date past = format.parse(validTime + " " + validDate + "." + year);

                Date now = new Date();

                PrettyTime prettyTime = new PrettyTime(now, new Locale("ru"));
                String prettyFormat = prettyTime.format(past);

                //считаем разницу между текущей датой и полученной из title
                long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
                long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());

                return correctEndingInTime(hours, minutes, prettyFormat);
            }
        }
        return "неверный формат";
    }

    //делаю чтобы было корректное окончание минут\минуты\минута час\часа\часов
    public String correctEndingInTime(long hours, long minutes, String prettyFormat) {

        if (hours == 0 && minutes >= 0) {
            //сейчас или 15 минут назад
            return prettyFormat;
        }

        if (hours == 1) {
            if (minutes % 60 == 0) {
                return prettyFormat;
            } else if (minutes % 60 == 1) {
                return hours + " час " + minutes % 60 + " минуту назад";
            } else if (minutes % 60 >= 2 && minutes % 60 <= 4) {
                return hours + " час " + minutes % 60 + " минуты назад";
            } else {
                return hours + " час " + minutes % 60 + " минут назад";
            }
        }

        if (hours > 1 && hours <= 4) {
            if (minutes % 60 == 0) {
                return prettyFormat;
            } else if (minutes % 60 == 1) {
                return hours + " часа " + minutes % 60 + " минуту назад";
            } else if (minutes % 60 >= 2 && minutes % 60 <= 4) {
                return hours + " часа " + minutes % 60 + " минуты назад";
            } else {
                return hours + " часа " + minutes % 60 + " минут назад";
            }
        }

        if (hours > 4) {
            if (minutes % 60 == 0) {
                return prettyFormat;
            } else if (minutes % 60 == 1) {
                return hours + " часов " + minutes % 60 + " минуту назад";
            } else if (minutes % 60 >= 2 && minutes % 60 <= 4) {
                return hours + " часов " + minutes % 60 + " минуты назад";
            } else {
                return hours + " часов " + minutes % 60 + " минут назад";
            }
        }

        return "время не попало под диапазон";
    }

    public String parseTitleForGettingTime(String text) {
        //возвращаем из текста время в формате HH:mm
//        Time24HoursValidator validator = new Time24HoursValidator();
        String resultTime = "";
        String[] splitText = text.split(" ");
        for (String time : splitText) {
            if (validator.isValidateTime24(time)) {
                resultTime = time;
                break;
            }
        }
        return resultTime;
    }

    public String parseTitleForGettingDate(String text) {
        //возвращаем из текста дату в формате dd.mm
        String resultDate = "";
        String[] splitText = text.split(" ");
        for (String time : splitText) {
            if (validator.isValidateDate(time)) {
                resultDate = time;
                break;
            }
        }
        return resultDate;
    }

    public String getPathToGifFile(String urlForDownloadGif) throws IOException {
        File file = new File("radar-map.gif");
        URL website = new URL(urlForDownloadGif);

        if (!file.exists()) {
            //если файла не существует, то качаем и копируем в корень проекта
            copyGifToRootProject(file, website);
        } else {
            long contentLengthGif = website.openConnection().getContentLengthLong();
            //если файл отличается по размеру, то заменяем на новый файл
            if (file.length() != contentLengthGif) {
                copyGifToRootProject(file, website);
            }
        }
        return file.toString();
    }

    private void copyGifToRootProject(File file, URL website) throws IOException {
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(file.toString());
        //максимальный размер 5Mb
        fos.getChannel().transferFrom(rbc, 0, 5 * 1024 * 1024);
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }
}