package wolfusz.voidlauncher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import wolfusz.voidlauncher.AppsListActivity;
import wolfusz.voidlauncher.R;
import wolfusz.voidlauncher.essentials.Label;

/**
 * Created by wolf2 on 12.04.2016.
 */
public class LabelListAdapter extends CustomArrayAdapter<Label> implements AdapterView.OnItemClickListener {

    public LabelListAdapter(Context context, LayoutInflater layoutInflater, List<Label> labels) {
        super(context, layoutInflater, R.layout.labels_list_item, labels);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView viewHolder = null;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.labels_list_item, parent, false);
            viewHolder = (TextView)convertView.findViewById(R.id.label);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TextView) convertView.getTag();
        }

        Label label = getItem(position);
        if (label != null) {
            viewHolder.setText(label.getText());
        }

        return convertView;
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Label label = getItem(position);
        ListView lv = (ListView)((AppsListActivity)context).findViewById(R.id.packages_list);
        lv.smoothScrollToPosition(label.getFirstApp());
    };

}
