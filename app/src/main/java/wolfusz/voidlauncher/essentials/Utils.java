package wolfusz.voidlauncher.essentials;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import wolfusz.voidlauncher.Globals;

/**
 * Created by wolf2 on 12.04.2016.
 */
public class Utils {

    // HashMap<Key, Value> result = sortHashMap( HashMap<Key, Value> hashMap );
    public static <K, V extends Comparable<? super V>> LinkedHashMap<K, V> sortHashMap(HashMap<K, V> HM) {
        class SortClass implements Comparable<SortClass> {
            K k; V v;
            public SortClass(K k, V v) { this.k = k; this.v = v; }
            @Override
            public int compareTo(SortClass another) {
                return this.v.compareTo(another.v);
            }
        }

        // list that helps sorting
        List<SortClass> sortL = new ArrayList<SortClass>();
        for (K k : HM.keySet())
            sortL.add( new SortClass(k, HM.get(k)) );

        // sort list
        Collections.sort(sortL);

        // apply to final product
        LinkedHashMap<K, V> sortedHM = new LinkedHashMap<K, V>();
        for (SortClass v : sortL)
            sortedHM.put(v.k, v.v);

        return sortedHM;
    }


    public static String capitalize(String s) {
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }


    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = Globals.homeScreen.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            result = Globals.homeScreen.getResources().getDimensionPixelSize(resourceId);
        return result;
    }
}
