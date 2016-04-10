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
public class In extends AsyncTask<String, Void, List<Article>> {
    public static final HashMap<String, String> map = new HashMap<>();

    static {
        map.put("Ελλάδα", "http://rss.in.gr/feed/news/greece/");
        map.put("Κόσμος", "http://rss.in.gr/feed/news/world/");
        map.put("Οικονομία", "http://rss.in.gr/feed/news/economy/");
        map.put("Επιστήμη", "http://rss.in.gr/feed/news/science/");
        map.put("Πολιτισμός", "http://rss.in.gr/feed/news/culture/");
        map.put("Αθλητικά", "http://rss.in.gr/feed/sports/");
        map.put("Τεχνολογία", "http://rss.in.gr/feed/technology/main/");
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

                Article article = new Article("IN.gr", url, title, date, link, description);
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
