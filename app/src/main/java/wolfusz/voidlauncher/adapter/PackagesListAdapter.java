package wolfusz.voidlauncher.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wolfusz.voidlauncher.AppsListActivity;
import wolfusz.voidlauncher.Globals;
import wolfusz.voidlauncher.R;
import wolfusz.voidlauncher.packages.PackageDialog;
import wolfusz.voidlauncher.packages.PackageInfo;

/**
 * Created by wolf2 on 12.04.2016.
 */
public class PackagesListAdapter extends CustomArrayAdapter<String> implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    protected String option;

    public PackagesListAdapter(Activity context, LayoutInflater layoutInflater, String option, List<String> packages) {
        super(context, layoutInflater, R.layout.apps_list_item, packages);
        this.option = option;
        if (packages.isEmpty())
            add(":empty");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PackageInfo.ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.apps_list_item, parent, false);
            viewHolder = new PackageInfo.ViewHolder();
            viewHolder.icon = (ImageView)convertView.findViewById(R.id.icon);
            viewHolder.label = (TextView)convertView.findViewById(R.id.label);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PackageInfo.ViewHolder) convertView.getTag();
        }

        String item = getItem(position);
        if (item != null) {
            if (item.equals(":empty")) {
                viewHolder.icon.setImageResource(R.mipmap.icon_info);
                viewHolder.label.setText( "This list is empty." );
            } else {
                PackageInfo pi = Globals.packageLoader.getAllPackages().get(item);
                viewHolder.icon.setImageDrawable( pi.icon );
                viewHolder.label.setText( pi.label );
            }
        }

        return convertView;
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//		((PackagesListActivity)context).doNotClose = true;
        String pkg = getItem(position);

//        new D(pkg);
//        new D(Globals.packageLoader.getAllPackages().get(pkg).label.toString());

        new PackageDialog( (AppsListActivity)context,
                option,
                Globals.packageLoader.getAllPackages().get(pkg).label.toString(),
                pkg,
                (PackagesListAdapter) parent.getAdapter()
        ).show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = context.getPackageManager().getLaunchIntentForPackage(getItem(position));
        context.startActivity(i);
        ((AppsListActivity)context).finish();
    };

}
