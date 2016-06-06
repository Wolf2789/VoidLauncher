package wolfusz.voidlauncher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import wolfusz.voidlauncher.adapter.LabelListAdapter;
import wolfusz.voidlauncher.adapter.PackagesListAdapter;
import wolfusz.voidlauncher.essentials.D;
import wolfusz.voidlauncher.essentials.Label;
import wolfusz.voidlauncher.packages.PackageLoader;
import wolfusz.voidlauncher.packages.PackageReceiver;

public class AppsListActivity extends Activity {
    // ACTIVITY
//	public boolean doNotClose;

    public String activity;
    public PackagesListAdapter packagesAdapter;
    public LabelListAdapter labelsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Globals.packagesList = this;
        super.onCreate(savedInstanceState);

        // register receiver
        new PackageReceiver();

        start("normal", "");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Preferences.savePreferences();
    }

    public void start(String activityType, String from) {
        new D("init , "+ activityType);
        activity = activityType;

        setContentView(R.layout.activity_packages_list);

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.packages_drawer);
        drawer.setScrimColor(0x00000000);

        loadPackages();

//		doNotClose = from.equals("hidden");
    }


    @Override
    public void onBackPressed() {
        Preferences.savePreferences();
        if (! activity.equals("hidden")) {
            new D("back to home");
            startActivity( new Intent(AppsListActivity.this, HomeScreenActivity.class) );
            finish();
        } else {
            new D("back to normal");
            start("normal", "hidden");
        }
    }


    // MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.packages_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (activity.equals("hidden"))
            return false;
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.activity_hidden_packages)
            start("hidden", "normal");
        return true;
    }


    // RELOADING PACKAGES
    public void loadPackages() {
        Globals.packageLoader.loadPackages();

        String option = "Hide";
        List<String> packages = null;
        if (activity.equals("hidden")) {
            option = "Unhide";
            packages = Globals.packageLoader.getHiddenPackages();
        } else
            packages = Globals.packageLoader.getPackages();

        if (packages == null || packages.isEmpty())
            packages = new ArrayList<String>();

        initPackagesList((ListView)findViewById(R.id.packages_list), option, packages);
        initLabelsDrawer((ListView)findViewById(R.id.labels_list), PackageLoader.loadLabels(packages));
    }

    // PACKAGES LIST
    public void initPackagesList(ListView lv, String option, List<String> packages) {
        if (packages != null) new D(packages.size());
        packagesAdapter = new PackagesListAdapter(this, getLayoutInflater(), option, packages);
        lv.setAdapter(packagesAdapter);
        lv.setOnItemClickListener(packagesAdapter);
        lv.setOnItemLongClickListener(packagesAdapter);
    }

    // LABELS DRAWER
    public void initLabelsDrawer(ListView list, List<Label> labels) {
        if (labels != null) new D(labels.size());
        labelsAdapter = new LabelListAdapter(this, getLayoutInflater(), labels );
        list.setAdapter(labelsAdapter);
        list.setOnItemClickListener(labelsAdapter);
    }

}
