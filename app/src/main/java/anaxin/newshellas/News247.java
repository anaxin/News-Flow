package anaxin.newshellas;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by x on 25/02/2016.
 */
public class News247 extends AsyncTask<String, Void, List<Article>> {
    public static final HashMap<String, String> map = new HashMap<>();

    static {
        map.put("Πολιτική", "http://news247.gr/eidiseis/politiki/?widget=rssfeed&view=feed&contentId=5328");
        map.put("Οικονομία", "http://news247.gr/eidiseis/oikonomia/?widget=rssfeed&view=feed&contentId=5328");
        map.put("Κοινωνία", "http://news247.gr/eidiseis/koinonia/?widget=rssfeed&view=feed&contentId=5328");
        map.put("Κόσμος", "http://news247.gr/eidiseis/kosmos/?widget=rssfeed&view=feed&contentId=5328");
        map.put("Αυτοκίνητο", "http://news247.gr/eidiseis/auto/?widget=rssfeed&view=feed&contentId=5328");
        map.put("Γνώμες", "http://news247.gr/eidiseis/gnomes/?widget=rssfeed&view=feed&contentId=5328");
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
                String date = element.select("published").text();
                String description = element.select("summary").text();
                String link = element.select("link[rel=alternate]").attr("href");
// 2016-03-22T19:00:24Z
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                Date d = null;
                try {
                    d = formatter.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                formatter.applyPattern("EEE, d MMM yyyy HH:mm:ss z");
                String newDate = formatter.format(d);
                Article article = new Article("NEWS 247", urlMap, title, newDate, link, description);
                articles.add(article);
                if (articles.size() > Integer.valueOf(params[1])){
                    System.out.println("xint limit break with " + params[1] );
                    break;
                }
            }

        } catch (
                IOException e
                )

        {
            e.printStackTrace();
        }
        return articles;
    }
}