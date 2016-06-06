package wolfusz.voidlauncher.essentials;

import android.util.Log;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by wolf2 on 12.04.2016.
 */
public class D {
    private final boolean debug = true;

    public <T> D(T s) { if (debug) Log.d("DEBUG", "" + s); }

    public <T> D(T[] a) {
        if (a.length == 0)
            new D("null");
        else {
            String s = "";
            for (T i : a)
                s += "\n" + i.toString();
            new D(s);
        }
    }

    public <T> D(List<T> a) { new D(a.toArray()); }

    public <K, V> D(LinkedHashMap<K, V> a) {
        if (a.isEmpty() == true)
            new D("null");
        else {
            String s = "";
            for (K k : a.keySet())
                s += "\n" + k.toString() + " -> " + a.get(k).toString();
            new D(s);
        }
    }
}
