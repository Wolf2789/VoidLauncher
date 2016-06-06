package wolfusz.voidlauncher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by wolf2 on 12.04.2016.
 */
public class CustomArrayAdapter<T> extends ArrayAdapter<T> {
    protected Context context;
    protected LayoutInflater layoutInflater;

    public CustomArrayAdapter(Context context, LayoutInflater layoutInflater, int layout, List<T> items) {
        super(context, layout, items);
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public long getItemId(int position) { return 0; }

    public void newData(List<T> items) {
        clear();
        addAll(items);
        notifyDataSetChanged();
    }
}
