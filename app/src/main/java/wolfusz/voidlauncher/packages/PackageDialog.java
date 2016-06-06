package wolfusz.voidlauncher.packages;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import wolfusz.voidlauncher.Globals;
import wolfusz.voidlauncher.R;
import wolfusz.voidlauncher.adapter.PackagesListAdapter;

/**
 * Created by wolf2 on 12.04.2016.
 */
public class PackageDialog extends Dialog {

    public PackageDialog instance = this;

    public PackageDialog(final Activity context, final String option, String app, final String currentPackage, final PackagesListAdapter adapter) {
        super(context);

        // remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // remove background
        getWindow().setBackgroundDrawable(new ColorDrawable(R.color.transparent));

        // set custom layout
        setContentView(R.layout.app_dialog);

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.app_dialog);

        // set title
        ((TextView)layout.findViewById(R.id.title)).setText(app);
        ((TextView)layout.findViewById(R.id.package_info)).setText(currentPackage);

        // set button events
        // info
        ((Button)layout.findViewById(R.id.btn_info)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + currentPackage));
                instance.dismiss();
                context.startActivity(i);
            }
        });
        // hide/unhide
        ((Button)layout.findViewById(R.id.btn_hide)).setText(option);
        ((Button)layout.findViewById(R.id.btn_hide)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (option == "Hide") {
                    Globals.packageLoader.hidePackage(currentPackage);
                } else {
                    Globals.packageLoader.unhidePackage(currentPackage);
                }
                adapter.remove(currentPackage);
                adapter.notifyDataSetChanged();
                instance.dismiss();
                Toast.makeText(context,
                        context.getResources().getString(
                                (option == "Hide") ? R.string.hidden_apps_add_msg : R.string.hidden_apps_rem_msg
                        ),
                        Toast.LENGTH_SHORT).show();
            }
        });
        // exclude
        ((Button)layout.findViewById(R.id.btn_exclude)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Globals.packageLoader.excludePackage(currentPackage);
                adapter.remove(currentPackage);
                adapter.notifyDataSetChanged();
                instance.dismiss();
                Toast.makeText(context,
                        context.getResources().getString(R.string.hidden_apps_exc_msg),
                        Toast.LENGTH_SHORT).show();
            }
        });
        // uninstall
        ((Button)layout.findViewById(R.id.btn_uninstall)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, Uri.parse("package:"+ currentPackage));
                instance.dismiss();
                context.startActivity(i);
            }
        });
    }

}
