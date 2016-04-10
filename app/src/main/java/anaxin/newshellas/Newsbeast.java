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
public class Newsbeast extends AsyncTask<String, Void, List<Article>> {

    public static final HashMap<String, String> map = new HashMap<>();

    static {
        map.put("Πολιτική", "http://www.newsbeast.gr/politiki/feed");
        map.put("Οικονομία", "http://www.newsbeast.gr/financial/feed");
        map.put("Διεθνή", "http://www.newsbeast.gr/world/feed");
        map.put("Κοινωνία", "http://www.newsbeast.gr/society/feed");
        map.put("Ελλάδα", "http://www.newsbeast.gr/greece/feed");
        map.put("Αθλητικά", "http://www.newsbeast.gr/sports/feed");
        map.put("Περιβάλλον", "http://www.newsbeast.gr/environment/feed");
        map.put("Media", "http://www.newsbeast.gr/media/feed");
        map.put("Διασκέδαση", "http://www.newsbeast.gr/entertainment/feed");
        map.put("Lifestyle", "http://www.newsbeast.gr/lifestyle/feed");
        map.put("Υγεία", "http://www.newsbeast.gr/health/feed");
        map.put("Τεχνολογία", "http://www.newsbeast.gr/technology/feed");
        map.put("Αυτοκίνητο", "http://www.newsbeast.gr/car/feed");
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
                String link = element.select("link").text();

                Article article = new Article("NEWSBEAST", url, title, date, link, "");
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
