package anaxin.newshellas;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by x on 31/03/2016.
 */
public class Capital extends AsyncTask<String, Void, List<Article>> {
    public static final HashMap<String, String> map = new HashMap<>();

    static {
        map.put("Πολιτική", "http://www.capital.gr/api/tags/politiki");
        map.put("Οικονομία", "http://www.capital.gr/api/tags/oikonomia");
        map.put("Διεθνή", "http://www.capital.gr/api/tags/diethni");
        map.put("Επιχειρήσεις", "http://www.capital.gr/api/tags/epixeiriseis");
        map.put("Αγορές", "http://www.capital.gr/api/tags/agores");

    }

    protected List<Article> doInBackground(String... params) {
        List<Article> articles = new ArrayList<>();
        String url = params[0];
        try {
            String urlMap = map.get(url);
            Document document = Jsoup.connect(urlMap).get();
            Elements elements = document.select("entry");
            for (Element element : elements) {
                String title = element.select("title").text();
                String date = element.select("updated").text();
                String link = element.select("link").attr("href");
// 2016-03-22T19:00:24Z  2016-03-31T18:56:00+03:00
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date d = null;
                try {
                    d = formatter.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                formatter.applyPattern("EEE, d MMM yyyy HH:mm:ss z");
                String newDate = formatter.format(d);

                Article article = new Article("CAPITAL", url, title, newDate, link, "");
                articles.add(article);
                if (articles.size() > Integer.valueOf(params[1])){
                    System.out.println("xint limit break with " + params[1] );
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return articles;
    }
}
