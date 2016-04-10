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
public class Vima extends AsyncTask<String, Void, List<Article>> {
    public static final HashMap<String, String> map = new HashMap<>();

    static
    {
        map.put("Πολιτική", "http://www.tovima.gr/feed/politics/");
        map.put("Οικονομία", "http://www.tovima.gr/feed/finance/");
        map.put("Επιστήμη", "http://www.tovima.gr/feed/science/");
        map.put("Κοινωνία", "http://www.tovima.gr/feed/society/");
        map.put("Πολιτισμός", "http://www.tovima.gr/feed/culture/");
        map.put("Αθλητικά", "http://www.tovima.gr/feed/sports/");
        map.put("Κόσμος", "http://www.tovima.gr/feed/world/");
        map.put("Γνώμες", "http://www.tovima.gr/feed/opinions/");

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

                Article article = new Article("TO BHMA", url, title, date, link, description);
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
