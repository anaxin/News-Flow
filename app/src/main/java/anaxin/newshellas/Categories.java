package anaxin.newshellas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Categories extends AppCompatActivity {

    HashMap<String, List<String>> sitesCategories = new HashMap<>();
    ListView listView;
    Set<String> categoriesList = new HashSet<>();
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        //toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Κατηγορίες");
        myToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(Categories.this);
            }
        });
        context = this;
        Button button2 = (Button) findViewById(R.id.button2);
        final Button button3 = (Button) findViewById(R.id.button3);

        final HashMap<String, List<String>> userSitesCategories = SharedPref.getMapFromSP("userSitesCategories", context);

        for (String key : userSitesCategories.keySet()) {
            categoriesList.addAll(userSitesCategories.get(key));
        }

        final List<String> tempList = new ArrayList<>(categoriesList);
        Collator collator = Collator.getInstance(new Locale("el", "GR"));
        Collections.sort(tempList, collator);

        listView = (ListView) findViewById(R.id.categView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.
                simple_list_item_multiple_choice, android.R.id.text1, tempList);
        listView.setAdapter(adapter);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, List<String>> tempSitesCategories = new HashMap<>(SharedPref.getMapFromSP("userSitesCategories", context));
                SparseBooleanArray checked = listView.getCheckedItemPositions();
                System.out.println("xint b4 onclick userSitesCategories " + userSitesCategories);

                for (int i = 0; i < listView.getCount(); i++) {
                    if (!checked.get(i)) {
                        String tempCat = listView.getItemAtPosition(i).toString();
                        for (String key : tempSitesCategories.keySet()) {
                            if (tempSitesCategories.get(key).contains(tempCat)) {
                                tempSitesCategories.get(key).remove(tempCat);
                            }
                        }
                    }
                }

                boolean hasValue = false;
                Set<String> tempCount = new HashSet<>();

                for (Map.Entry<String, List<String>> entry : tempSitesCategories.entrySet()) {
                    tempCount.addAll(entry.getValue());
                    System.out.println("xint limit value  " + Arrays.asList(entry.getValue()));
                    System.out.println("xint limit value size  " +entry.getValue().size());
                    if (!entry.getValue().isEmpty()) {
                        hasValue = true;
                    }
                }
                if (!hasValue) {
                    System.out.println("xint limit count should be 0 " + tempCount);
                    Toast.makeText(getApplicationContext(), "You must choose at least one category",
                            Toast.LENGTH_SHORT).show();
                } else if (tempCount.size() > 5){
                    System.out.println("xint limit count should be over 6 " + tempCount);
                    Toast.makeText(getApplicationContext(), "You must choose a maximum of 5 categories",
                            Toast.LENGTH_SHORT).show();
                } else {
                    SharedPref.removeMapFromSp("tempSitesCategories", context);
                    SharedPref.saveMapToSP("tempSitesCategories", tempSitesCategories, context);

                    Intent intent = new Intent(Categories.this, LoadingScreen.class);
                    intent.putExtra("activity","categories");
                    Categories.this.startActivity(intent);

                }
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, List<String>> tempSitesCategories = new HashMap<>(SharedPref.getMapFromSP("userSitesCategories", context));
                SparseBooleanArray checked = listView.getCheckedItemPositions();
                System.out.println("xint b4 onclick userSitesCategories " + userSitesCategories);

                for (int i = 0; i < listView.getCount(); i++) {
                    if (!checked.get(i)) {
                        String tempCat = listView.getItemAtPosition(i).toString();
                        for (String key : tempSitesCategories.keySet()) {
                            if (tempSitesCategories.get(key).contains(tempCat)) {
                                tempSitesCategories.get(key).remove(tempCat);
                            }
                        }
                    }
                }

                boolean hasValue = false;
                Set<String> tempCount = new HashSet<>();
                for (Map.Entry<String, List<String>> entry : tempSitesCategories.entrySet()) {
                    tempCount.addAll(entry.getValue());
                    System.out.println("xint limit value  " + Arrays.asList(entry.getValue()));
                    System.out.println("xint limit value size " +entry.getValue().size());
                    if (!entry.getValue().isEmpty()) {
                        hasValue = true;
                    }
                }
                if (!hasValue) {
                    Toast.makeText(getApplicationContext(), "You must choose at least one category",
                            Toast.LENGTH_SHORT).show();
                }  else if (tempCount.size() > 5){
                    System.out.println("xint limit count should be over 6 " + tempCount);
                    Toast.makeText(getApplicationContext(), "You must choose a maximum of 5 categories",
                            Toast.LENGTH_SHORT).show();
                } else {
                    SharedPref.removeMapFromSp("tempSitesCategories", context);
                    SharedPref.saveMapToSP("tempSitesCategories", tempSitesCategories, context);
                    SharedPref.removeMapFromSp("default", context);
                    SharedPref.saveMapToSP("default", tempSitesCategories , context);

                    Intent intent = new Intent(Categories.this, LoadingScreen.class);
                    intent.putExtra("activity","categories");
                    Categories.this.startActivity(intent);

                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_favorite:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

