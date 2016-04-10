package anaxin.newshellas;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

/**
 * Created by x on 31/03/2016.
 */
public class SharedPref {

    SharedPref() {

    }

    public static HashMap<String, List<String>> getMapFromSP(String name, Context context) {
        SharedPreferences preferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String wrapperStr = preferences.getString(name, "DEFAULT");
        MapWrapper wrapper = gson.fromJson(wrapperStr, MapWrapper.class);
        return wrapper.getMyMap();
    }

    public static void removeMapFromSp(String mapName, Context context) {
        SharedPreferences preferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(mapName);
        editor.commit();
    }
    public static boolean hasDefault(Context context){
        SharedPreferences preferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);

        return preferences.contains("default");



    }
    public static void saveMapToSP(String mapName, HashMap<String, List<String>> map, Context context) {
        SharedPreferences preferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        MapWrapper wrapper = new MapWrapper();
        wrapper.setMyMap(map);
        String serializedMap = gson.toJson(wrapper);
        editor.putString(mapName, serializedMap);
        editor.commit();
    }
}