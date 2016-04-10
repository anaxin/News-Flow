package anaxin.newshellas;

import java.util.HashMap;
import java.util.List;

public class MapWrapper {
    private HashMap<String, List<String>> MyMap;

    public HashMap<String, List<String>> getMyMap() {
        return MyMap;
    }

    public void setMyMap(HashMap<String, List<String>> myMap) {
        MyMap = myMap;
    }
}
