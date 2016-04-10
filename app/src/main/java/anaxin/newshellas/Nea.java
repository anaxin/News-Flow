package anaxin.newshellas;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by x on 26/02/2016.
 */
public class Nea extends AsyncTask<String, Void, List<Article>> {

    public static final HashMap<String, String> map = new HashMap<>();

    static
    {
        map.put("Πολιτική", "http://www.tanea.gr/rss.axd?pgid=10");
        map.put("Οικονομία", "http://www.tanea.gr/rss.axd?pgid=37");
        map.put("Ελλάδα", "http://www.tanea.gr/rss.axd?pgid=13");
        map.put("Κόσμος", "http://www.tanea.gr/rss.axd?pgid=16");
        map.put("Αθλητικά", "http://www.tanea.gr/rss.axd?pgid=49");
        map.put("Πολιτισμός", "http://www.tanea.gr/rss.axd?pgid=43");


    }


    protected List<Article> doInBackground(String... params) {
        List<Article> articles = new ArrayList<>();
        String url = params[0];
        try {
            String urlMap = map.get(url);
            Document document = Jsoup.connect(urlMap).get();
            Elements elements = document.select("item");
            for (Element element : elements) {
                String title = element.select("title").text();
                String date = element.select("pubDate").text();
                String description = element.select("description").text();
                String link = element.select("link").text();

                Article article = new Article("TA NEA", url, title, date, link, description);
                articles.add(article);
                if (articles.size() > Integer.valueOf(params[1])){
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return articles;
    }
}
