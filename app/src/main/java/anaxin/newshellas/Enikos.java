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
public class Enikos extends AsyncTask<String, Void, List<Article>> {

    public static final HashMap<String, String> map = new HashMap<>();

    static {
        map.put("Πολιτική", "http://www.enikos.gr/feeds/menu/politics.xml");
        map.put("Οικονομία", "http://www.enikos.gr/feeds/menu/economy.xml");
        map.put("Διεθνή", "http://www.enikos.gr/feeds/menu/international.xml");
        map.put("Κοινωνία", "http://www.enikos.gr/feeds/menu/society.xml");
        map.put("Αθλητικά", "http://www.enikos.gr/feeds/menu/sports.xml");
        map.put("Lifestyle", "http://www.enikos.gr/feeds/menu/lifestyle.xml");
        map.put("Media", "http://www.enikos.gr/feeds/menu/media.xml");
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
            Article article = new Article("ENIKOS", url, title, date, link, "");
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
