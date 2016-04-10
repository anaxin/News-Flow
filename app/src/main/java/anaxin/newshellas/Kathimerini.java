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
 * Created by x on 31/03/2016.
 */
public class Kathimerini extends AsyncTask<String, Void, List<Article>> { public static final HashMap<String, String> map = new HashMap<>();

    static {
        map.put("Πολιτική", "http://www.kathimerini.gr/rss?i=news.el.politikh");
        map.put("Οικονομία", "http://www.kathimerini.gr/rss?i=news.el.ellhnikh-oikonomia");
        map.put("Απόψεις", "http://www.kathimerini.gr/rss?i=news.el.search&q=&t=0&w=&c=&s=p&type=OPINION&edition=&author=0&fromDate=&toDate=");

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
                String summary = element.select("description").text();

                if (summary.contains("<p>")){
                    summary = summary.replace("<p>", "");
                }
                if (summary.contains("</p>")){
                    summary = summary.replace("</p>", "");
                }
                System.out.println("REAL.gr" +" \ntitle "+ title + "\n date " + date +" \n link "+ link+" \n summary " +summary);
                Article article = new Article("KATHIMERINI", url, title, date, link, summary);
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
