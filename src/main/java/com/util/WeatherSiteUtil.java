package com.util;

import com.enums.TimeEnum;
import com.exception.InvalidUrlException;
import com.exception.NoDataOnSiteException;
import com.settings.UrlSetting;
import org.apache.commons.validator.UrlValidator;
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
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


@Service
public class WeatherSiteUtil {

    @Autowired
    private UrlSetting urlSetting;

    private final Time24HoursValidator validator;
    private String title;
    private static final String EXCEPTION_MESSAGE = "Invalid URL";

    private final DrawVillageOnMap drawVillageOnMap;

    @Autowired
    public WeatherSiteUtil(Time24HoursValidator validator, DrawVillageOnMap drawVillageOnMap) {
        this.validator = validator;
        this.drawVillageOnMap = drawVillageOnMap;
    }

    public String getMapWithVillage(String fileName) throws IOException, InvalidUrlException, NoDataOnSiteException {
        String mapInRootProject = getPathToFileInRootProject(getImageFromUrl(), fileName);
        drawVillageOnMap.mapWithVillage(mapInRootProject);
        return fileName;
    }

    public String getImageFromUrl() throws IOException, InvalidUrlException, NoDataOnSiteException {
        Document doc = Jsoup.connect(urlSetting.getUrlMainPageMeteoinfo()).get();

        //get element by id
        Elements rdr = doc.select("rdr");
        if (rdr.isEmpty()) throw new NoDataOnSiteException("Missing data on the site");

        Element elementImg = rdr.get(0).getElementsByTag("img").get(0);
        title = elementImg.attr("title");
        //select absolute path from TABLE
        String url = elementImg.absUrl("src");

        UrlValidator urlValidator = new UrlValidator();
        // check url by validator
        if (!url.isBlank() || !urlValidator.isValid(url)) {
            throw new InvalidUrlException(EXCEPTION_MESSAGE);
        } else return url;
    }


    public String getTimeFromSiteWithNewTime(String title) throws ParseException {
        if (title.isBlank()) {
            return "неверный формат";
        }

        //get time in the format "HH:mm"  16:35
        String validTime = parseTitleForGettingTime(title);

        //get date in the format dd.MM 13.06
        String validDate = parseTitleForGettingDate(title);

        if (!validTime.isBlank() && !validDate.isBlank()) {
            // Get the current date and time
            LocalDate localDate = LocalDate.now();
            // The current year, because in title only day and month
            int year = localDate.getYear();
            //set the format time
            SimpleDateFormat format = new SimpleDateFormat("HH:mm dd.MM.yyyy");
            //set the time zone
            format.setTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));
            //parse incoming time in format "HH:mm dd.MM.yyyy"
            Date past = format.parse(validTime + " " + validDate + "." + year);

            Date now = new Date();

            PrettyTime prettyTime = new PrettyTime(now, new Locale("ru"));
            String prettyFormat = prettyTime.format(past);

            //consider the difference between the current date and the one obtained from title
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());

            return correctEndingInTime(hours, minutes, prettyFormat);
        } else return "неверный формат";
    }

    //make sure that there is a correct ending минут\минуты\минута час\часа\часов
    private String correctEndingInTime(long hours, long minutes, String prettyFormat) {

        if (hours == 0 && minutes >= 0) {
            //now or 15 minutes ago
            return prettyFormat;
        }

        if (hours == 1) {
            if (minutes % 60 == 0) {
                return prettyFormat;
            } else if (minutes % 60 == 1) {
                return hours + TimeEnum.HOUR.getTranslation() + minutes % 60 + TimeEnum.MINUTE.getTranslation();
            } else if (minutes % 60 >= 2 && minutes % 60 <= 4) {
                return hours + TimeEnum.HOUR.getTranslation() + minutes % 60 + TimeEnum.MINUTES_OTHER.getTranslation();
            } else {
                return hours + TimeEnum.HOUR.getTranslation() + minutes % 60 + TimeEnum.MINUTES.getTranslation();
            }
        }

        if (hours > 1 && hours <= 4) {
            if (minutes % 60 == 0) {
                return prettyFormat;
            } else if (minutes % 60 == 1) {
                return hours + TimeEnum.HOURS_OTHER.getTranslation() + minutes % 60 + TimeEnum.MINUTE.getTranslation();
            } else if (minutes % 60 >= 2 && minutes % 60 <= 4) {
                return hours + TimeEnum.HOURS_OTHER.getTranslation() + minutes % 60 + TimeEnum.MINUTES_OTHER.getTranslation();
            } else {
                return hours + TimeEnum.HOURS_OTHER.getTranslation() + minutes % 60 + TimeEnum.MINUTES.getTranslation();
            }
        }

        if (hours > 4) {
            if (minutes % 60 == 0) {
                return prettyFormat;
            } else if (minutes % 60 == 1) {
                return hours + TimeEnum.HOURS.getTranslation() + minutes % 60 + TimeEnum.MINUTE.getTranslation();
            } else if (minutes % 60 >= 2 && minutes % 60 <= 4) {
                return hours + TimeEnum.HOURS.getTranslation() + minutes % 60 + TimeEnum.MINUTES_OTHER.getTranslation();
            } else {
                return hours + TimeEnum.HOURS.getTranslation() + minutes % 60 + TimeEnum.MINUTES.getTranslation();
            }
        }

        return "время не попало под диапазон";
    }

    public String parseTitleForGettingTime(String text) {
        // return time from the text in the format HH: mm
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
        //return the date from the text in the format dd.mm
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

    public String getPathToFileInRootProject(String urlForDownloadGif, String fileName) throws IOException, InvalidUrlException {
        File file = new File(fileName);
        URL website = new URL(urlForDownloadGif);

        if (!file.exists()) {
            //if the file does not exist, then download and copy to the root of the project
            copyGifToRootProject(file, website);
        } else {
            long contentLengthGif = website.openConnection().getContentLengthLong();
            //if the file is different in size, then replace with a new file
            if (contentLengthGif == -1) {
                throw new InvalidUrlException(EXCEPTION_MESSAGE);
            } else if (file.length() != contentLengthGif) {
                copyGifToRootProject(file, website);
            }
        }
        return file.toString();
    }

    private void copyGifToRootProject(File file, URL website) throws IOException {
        try (ReadableByteChannel rbc = Channels.newChannel(website.openStream());
             FileOutputStream fos = new FileOutputStream(file.toString())) {
            //max size 5Mb
            fos.getChannel().transferFrom(rbc, 0, 5 * 1024 * 1024);
        }
    }

    public String getTitle() {
        return title;
    }
}