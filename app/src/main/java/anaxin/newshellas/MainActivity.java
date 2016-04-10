package anaxin.newshellas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    ListView listView;
    List<String> nea = Arrays.asList("Πολιτική", "Οικονομία", "Ελλάδα", "Κόσμος",
            "Αθλητικά", "Πολιτισμός");
    List<String> vima = Arrays.asList("Πολιτική", "Οικονομία", "Επιστήμη", "Κοινωνία",
            "Πολιτισμός", "Αθλητικά", "Κόσμος", "Γνώμες");
    List<String> newsbeast = Arrays.asList("Πολιτική", "Οικονομία", "Διεθνή", "Κοινωνία",
            "Ελλάδα", "Αθλητικά", "Περιβάλλον", "Media", "Διασκέδαση", "Lifestyle",
            "Υγεία", "Τεχνολογία", "Αυτοκίνητο");
    List<String> enikos = Arrays.asList("Πολιτική", "Οικονομία", "Διεθνή", "Κοινωνία",
            "Αθλητικά", "Lifestyle", "Media");
    List<String> in = Arrays.asList("Ελλάδα", "Κόσμος", "Οικονομία", "Επιστήμη",
            "Πολιτισμός", "Αθλητικά", "Τεχνολογία");
    List<String> news247 = Arrays.asList("Πολιτική", "Οικονομία", "Κοινωνία", "Κόσμος", "Αυτοκίνητο", "Γνώμες");
    List<String> kathimerini = Arrays.asList("Πολιτική", "Οικονομία", "Απόψεις");
    List<String> capital = Arrays.asList("Πολιτική", "Οικονομία", "Διεθνή", "Επιχειρήσεις", "Αγορές");

    HashMap<String, List<String>> sitesCategories = new HashMap<>();
    HashMap<String, List<String>> userSitesCategories = new HashMap<>();
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        if (SharedPref.hasDefault(context)) {
            Intent intent = new Intent(MainActivity.this, LoadingScreen.class);
            intent.putExtra("activity", "main");
            MainActivity.this.startActivity(intent);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Ιστοσελίδες");

        sitesCategories.put("TA NEA", nea);
        sitesCategories.put("TO BHMA", vima);
        sitesCategories.put("NEWSBEAST", newsbeast);
        sitesCategories.put("ENIKOS", enikos);
        sitesCategories.put("IN.gr", in);
        sitesCategories.put("NEWS 247", news247);
        sitesCategories.put("KATHIMERINI", kathimerini);
        sitesCategories.put("CAPITAL", capital);
        Integer[] imageID = {R.drawable.tanea_logo, R.drawable.tobhma_logo, R.drawable.newsbeast_logo,
                R.drawable.enikos_logo, R.drawable.in_logo, R.drawable.news247_logo, R.drawable.kathimerini_logo,
                R.drawable.capital_logo};

        SharedPref.saveMapToSP("sitesCategories", sitesCategories, context);
        Button button = (Button) findViewById(R.id.button);

        final List<String> list = new ArrayList<>();
        list.clear();
        list.add("TA NEA");
        list.add("TO BHMA");
        list.add("NEWSBEAST");
        list.add("ENIKOS");
        list.add("IN.gr");
        list.add("NEWS 247");
        list.add("KATHIMERINI");
        list.add("CAPITAL");


        listView = (ListView) findViewById(R.id.sitesView);
        ArrayList data = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> datum = new HashMap<>(2);
            datum.put("image", Integer.toString(imageID[i]));
            datum.put("text", list.get(i));
            data.add(datum);
        }
        SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, data,
                R.layout.main_listview, new String[]{"image", "text"},
                new int[]{R.id.imageMain, R.id.text1}) {
            @Override
            public int getViewTypeCount() {
                return getCount();
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }
        };

        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView checkView = (CheckedTextView) ((ViewGroup) view).findViewById(R.id.text1);
                checkView.toggle();

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SparseBooleanArray checked = listView.getCheckedItemPositions();
                List<String> checkedSites = new ArrayList<>();
                for (int i = 0; i < listView.getCount(); i++) {
                    if (checked.get(i)) {
                        checkedSites.add(list.get(i));
                    }
                }

                if (checkedSites.size() == 0) {
                    Toast.makeText(getApplicationContext(), "You must choose at least one site",
                            Toast.LENGTH_SHORT).show();
                } else {
                    userSitesCategories.clear();
                    for (String site : checkedSites) {
                        userSitesCategories.put(site, sitesCategories.get(site));
                    }

                    SharedPref.removeMapFromSp("userSitesCategories", context);
                    SharedPref.saveMapToSP("userSitesCategories", userSitesCategories, context);

                    Intent intent = new Intent(MainActivity.this, Categories.class);
                    MainActivity.this.startActivity(intent);

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void toggle(CheckedTextView v) {
        if (v.isChecked()) {
            v.setChecked(false);
        } else {
            v.setChecked(true);
        }
    }

}
