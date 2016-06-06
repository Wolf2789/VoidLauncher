package wolfusz.voidlauncher.widget;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by wolf2 on 12.04.2016.
 */
public class WidgetData implements Serializable {
    private static final long serialVersionUID = 8376976418077663573L;

    public Widget.Type type;
    public HashMap<String, Object> data;

    public WidgetData(Widget.Type type) {
        this.type = type;
        this.data = new HashMap<String, Object>();
    }

    @Override
    public String toString() {
        String str_type = "";
        switch (type) {
            case Shortcut: str_type = "shortcut"; break;
            case Widget: str_type = "widget"; break;
            default: str_type = "wtf?";
        }

        String str_data = " ";
        for (String key : data.keySet()) {
            if (str_data != " ")
                str_data += ", ";
            String str = data.get(key).toString();
            str_data += key +" -> "+ (str.length()>30 ? str.substring(0,30)+".." : str) +" ";
        }

        return "Widget ["+ str_type +",{"+ str_data +"}]";
    }

    public void put(String k, Object v) { this.data.put(k, v); }
    public void put(WidgetData data) { this.data.putAll(data.data); }

    public Object get(String k) { return this.data.get(k); }
    public HashMap<String, Object> get() { return this.data; }
}
