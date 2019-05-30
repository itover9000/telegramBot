package util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MeteoradarUtil {
    private static final String link = "http://www.meteoinfo.by/radar/?q=UMMN&t=0";

    public static String getImageFromUrl() throws IOException {
        Document doc = Jsoup.connect(link).get();
        Element table = doc.select("table").get(2); //select the third table.
        Elements rows = table.select("tr");

        //select absolute path from table
        return rows.get(0).getElementsByTag("img").get(0).absUrl("src");
    }
}
