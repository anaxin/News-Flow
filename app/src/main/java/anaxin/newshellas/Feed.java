package anaxin.newshellas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class Feed extends AppCompatActivity {
    ListView listView;
    int index = 0;
    List<Map<String, String>> data;
    List<Article> feed = new ArrayList<>();
    private PopupWindow pw;
    TextView titleField, categField, summaryField;
    HashMap<String, List<String>> tempSitesCategories;
    SimpleAdapter adapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        index = 0;
        context = this;
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        setTitle("Ειδήσεις");
        myToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(Feed.this);
            }
        });

        Intent intent = getIntent();
        String activity = intent.getStringExtra("activity");
        System.out.println("xint activity " + activity);
        if (activity.equals("categories")) {
            tempSitesCategories = SharedPref.getMapFromSP("tempSitesCategories", context);
        } else {
            tempSitesCategories = SharedPref.getMapFromSP("default", context);
        }
        getFeed(tempSitesCategories);
        Collections.sort(feed, Collections.reverseOrder());
        data = new ArrayList<>();
        addArticles(10, feed);

        adapter = new SimpleAdapter(Feed.this, data, R.layout.listview,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.textTop, R.id.textBot}) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                return view;
            }
        };
        listView = (ListView) findViewById(R.id.feedView);
        listView.setAdapter(adapter);//set initial amount??
        listView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {

                try {
                    addArticles(10, feed);
                    adapter.notifyDataSetChanged();
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }

        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final TextView top = (TextView) view.findViewById(R.id.textTop);
                top.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = top.getText().toString();
                        String url = Article.getLinkFromTitle(title);
                        gotoSite(url);
                    }
                });
                final TextView bot = (TextView) view.findViewById(R.id.textBot);
                bot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = top.getText().toString();
                        String[] arr = Article.getSummaryFromTitle(title);
                        String url = Article.getLinkFromTitle(title);
                        getPopupWindow(title, arr, url);
                    }
                });
                final ImageView img = (ImageView) view.findViewById(R.id.imgView);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = top.getText().toString();
                        String[] arr = Article.getSummaryFromTitle(title);
                        String url = Article.getLinkFromTitle(title);
                        getPopupWindow(title, arr, url);
                    }
                });
            }
        });
    }

    public void gotoSite(String url) {
        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browse);
    }

    public void addArticles(int amount, List<Article> feed) {
        int temp = index + amount;

        for (int i = index; i < temp; i++) {
            Article article = feed.get(i);
            Map<String, String> datum = new HashMap<>(2);
            datum.put("First Line", article.getTop());
            datum.put("Second Line", article.getBot());
            data.add(datum);
            index++;
        }
    }

    public void getPopupWindow(String title, String[] arr, String url) {
        final String URL = url;
        try {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup,
                    (ViewGroup) findViewById(R.id.popup_element));
            pw = new PopupWindow(layout, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT, false);

            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

            titleField = (TextView) layout.findViewById(R.id.title);
            titleField.setText(title);
            categField = (TextView) layout.findViewById(R.id.category);
            categField.setText(arr[0] + ", " + arr[1]);
            summaryField = (TextView) layout.findViewById(R.id.summary);
            summaryField.setText(arr[2]);
            Button backButton = (Button) layout.findViewById(R.id.backButton);
            Button goToSiteButton = (Button) layout.findViewById(R.id.goToSiteButton);
            goToSiteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoSite(URL);
                }
            });
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pw.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//todo highlight background doesnt work properly
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getTitle().equals("ΑΛΛΑΓΗ ΠΡΟΕΠΙΛΟΓΩΝ")) {
            SharedPref.removeMapFromSp("default", context);
            Intent intent = new Intent(Feed.this, MainActivity.class);
            Feed.this.startActivity(intent);
        } else {
            HashMap<String, List<String>> map = new HashMap<>();
            tempSitesCategories = SharedPref.getMapFromSP("tempSitesCategories", context);

            for (String site : tempSitesCategories.keySet()) {
                if (tempSitesCategories.get(site).contains(item.getTitle())) {
                    map.put(site, new ArrayList<>(Arrays.asList((String) item.getTitle())));
                }
            }
            getFeed(map);

            Collections.sort(feed, Collections.reverseOrder());
            data.clear();
            addArticles(10, feed);
            adapter.notifyDataSetChanged();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        SharedPreferences preferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String wrapperStr = preferences.getString("tempSitesCategories", "DEFAULT");
        MapWrapper wrapper = gson.fromJson(wrapperStr, MapWrapper.class);
        HashMap<String, List<String>> tempSitesCategories = wrapper.getMyMap();
        HashSet<String> list = new HashSet<>();
        for (String key : tempSitesCategories.keySet()) {
            list.addAll(tempSitesCategories.get(key));
        }
        menu.add(0, 0, 0, "ΑΛΛΑΓΗ ΠΡΟΕΠΙΛΟΓΩΝ");
        int i = 1;
        for (String s : list) {
            menu.add(0, i, 0, s);
            i++;
        }
        return true;
    }

    public void getFeed(HashMap<String, List<String>> map) {
        feed.clear();
        index = 0;
        int count = 0;
        int max = 300;
        int maxPerCat;

        for (String site : map.keySet()) {
            System.out.println("xint limit site-cat " + site + " - " + map.get(site));
            for (int i = 0; i < map.get(site).size(); i++) {
                count++;
            }
            System.out.println("xint limit count " + count);
        }
        maxPerCat = max / count;

        for (String site : map.keySet()) {
            List<String> categories = map.get(site);
            switch (site) {
                case "ENIKOS":
                    for (String category : categories) {
                        try {
                            feed.addAll(new Enikos().execute(category, Integer.toString(maxPerCat)).get());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "KATHIMERINI":
                    for (String category : categories) {
                        try {
                            feed.addAll(new Kathimerini().execute(category, Integer.toString(maxPerCat)).get());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "IN.gr":
                    for (String category : categories) {
                        try {
                            feed.addAll(new In().execute(category, Integer.toString(maxPerCat)).get());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "TA NEA":
                    for (String category : categories) {
                        try {
                            feed.addAll(new Nea().execute(category, Integer.toString(maxPerCat)).get());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "NEWS 247":
                    for (String category : categories) {
                        try {
                            feed.addAll(new News247().execute(category, Integer.toString(maxPerCat)).get());
                        } catch (InterruptedException e) {
                        } catch (ExecutionException e) {
                        }
                    }
                    break;
                case "NEWSBEAST":
                    for (String category : categories) {
                        try {
                            feed.addAll(new Newsbeast().execute(category, Integer.toString(maxPerCat)).get());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "TO BHMA":
                    for (String category : categories) {
                        try {
                            feed.addAll(new Vima().execute(category, Integer.toString(maxPerCat)).get());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "CAPITAL":
                    for (String category : categories) {
                        try {
                            feed.addAll(new Capital().execute(category, Integer.toString(maxPerCat)).get());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid site");
            }
            categories.clear();
        }
    }

    @Override
    public void onBackPressed() {
        if (pw != null) {
            pw.dismiss();
            pw = null;
        } else {
            super.onBackPressed();
        }
    }
}
