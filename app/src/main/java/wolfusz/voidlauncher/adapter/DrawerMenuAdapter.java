package wolfusz.voidlauncher.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import wolfusz.voidlauncher.HomeScreenActivity;
import wolfusz.voidlauncher.R;
import wolfusz.voidlauncher.essentials.FlashLight;

/**
 * Created by wolf2 on 12.04.2016.
 */
public class DrawerMenuAdapter extends ArrayAdapter<Integer> implements AdapterView.OnItemClickListener {

    private Activity context;
    private LayoutInflater layoutInflater;
    private List<Integer> items;

    public DrawerMenuAdapter(Activity context, LayoutInflater layoutInflater, List<Integer> items) {
        super(context, R.layout.drawer_menu_item, items);
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.items = items;
    }

    @Override
    public int getCount() { return items.size(); }

    @Override
    public Integer getItem(int position) { return items.get(position); }

    @Override
    public long getItemId(int position) { return 0; }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView viewHolder = null;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.drawer_menu_item, parent, false);
            viewHolder = (ImageView)convertView.findViewById(R.id.item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ImageView) convertView.getTag();
        }

        Integer item = items.get(position);
        if (item != null) {
            switch(item) {
                case R.mipmap.menu_flashlight:
                    viewHolder.setImageResource( FlashLight.on == true ? R.mipmap.menu_flashlight_on : R.mipmap.menu_flashlight );
                    break;
                default:
                    viewHolder.setImageResource( item );
                    break;
            }
        }

        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Integer item = items.get(position);
        switch(item) {

            case R.mipmap.menu_flashlight:
                FlashLight.toggle();

                ((ImageView)view).setImageResource(FlashLight.on == true ? R.mipmap.menu_flashlight_on : R.mipmap.menu_flashlight);
                break;

            case R.mipmap.menu_restart:
                context.setResult(0);
                context.finish();
                context.startActivityForResult( new Intent(context, HomeScreenActivity.class), 0 );
                break;

            case R.mipmap.menu_systemsettings:
                Intent i = context.getPackageManager().getLaunchIntentForPackage("com.android.settings");
                if (i != null)
                    context.startActivity(i);
                ((DrawerLayout)(((HomeScreenActivity)context).findViewById(R.id.homescreen_drawer))).closeDrawers();
                break;

        }
    }
}