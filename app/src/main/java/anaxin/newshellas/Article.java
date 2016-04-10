package anaxin.newshellas;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

/**
 * Handles Articles
 * Created by x on 26/02/2016.
 */
public class Article implements Comparable<Article> {
    String site, category, title, link, description;
    Date date;
    public static HashSet<Article> allArticles = new HashSet<>();

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Article(String site, String category, String title, String date, String link, String description) {

        DateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
        Date d = null;
        try {
            d = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.site = site;
        this.category = category;
        this.title = title;
        this.date = d;
        this.link = link;
        this.description = description;
        allArticles.add(this);
    }

    public String getTop() {
        return title;
    }

    public String getBot() {
        return site + ", " + category + ", " + date;
    }

    @Override
    public String toString() {
        return "Article{" +
                ", title='" + title + '\'' +
                "category='" + category + '\'' +
                ", site='" + site + '\'' +
                ", date='" + date + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public HashSet<Article> getAllArticles() {
        return allArticles;
    }

    public void addToAllArticles(Article article) {
        allArticles.add(article);
    }

    public void setAllArticles(HashSet<Article> allArticles) {
        this.allArticles = allArticles;
    }

    @Override
    public int compareTo(Article o) {
        return getDate().compareTo(o.getDate());
    }

    public static String getLinkFromTitle(String title) {
        for (Article a : allArticles) {
            String temp = a.getTitle();
            if (temp.equals(title)) {
                return a.getLink();
            }
        }
        return null;
    }
    public static String[] getSummaryFromTitle(String title) {
        for (Article a : allArticles) {
            String titleTemp = a.getTitle();
            if (titleTemp.equals(title)) {
             String[] arr = new String[3];
                arr[0] = a.getSite();
                arr[1] = a.getCategory();
                arr[2] = a.getDescription();
                return arr;
            }
        }
        return null;
    }

}
