package wolfusz.voidlauncher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import wolfusz.voidlauncher.adapter.DrawerMenuAdapter;
import wolfusz.voidlauncher.essentials.D;
import wolfusz.voidlauncher.packages.PackageLoader;
import wolfusz.voidlauncher.widget.WidgetManager;

public class HomeScreenActivity extends FragmentActivity {
    private View decorView;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new D("init , home");
        Globals.homeScreen = this;
        super.onCreate(savedInstanceState);

        //set current layout
        setContentView(R.layout.activity_home_screen);

        //load preferences
        Preferences.loadPreferences(this);
        new PackageLoader(this);
        new WidgetManager(this, (RelativeLayout)findViewById(R.id.desktop));

        //hide navigation bar after getting decor view
        decorView = getWindow().getDecorView();
        HideNavBar();

        //configure homescreen drawer
        drawer = (DrawerLayout)findViewById(R.id.homescreen_drawer);
        drawer.setScrimColor(0x00000000);
        initDrawerMenu( (ListView)findViewById(R.id.drawer_menu) );

        Globals.widgetManager.startListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Preferences.savePreferences();
        Globals.widgetManager.stopListening();
    }


    // HIDING NAVIGATION BAR
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus)
            HideNavBar();
    }

    @Override
    public void onResume() {
        super.onResume();
        HideNavBar();
    }

    private void HideNavBar() {
        decorView.setSystemUiVisibility(
//    		View.SYSTEM_UI_FLAG_LOW_PROFILE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }


    // SYSTEM MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.reset_preferences) {
            Globals.widgetManager.reset();
            Preferences.reset();
        }
        return true;
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);
    }

    // SYSTEM BUTTONS
    @Override
    public void onBackPressed() {
        setResult(RESULT_CLOSE_ALL);
        finish();
    }


    // INIT DRAWER MENU
    private void initDrawerMenu(ListView list) {
        List<Integer> items = new ArrayList<Integer>();
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
            items.add(R.mipmap.menu_flashlight);
        items.add(R.mipmap.menu_restart);
        items.add(R.mipmap.menu_systemsettings);

        DrawerMenuAdapter adapter = new DrawerMenuAdapter(this, getLayoutInflater(), items);
        list.setAdapter(adapter);
        list.setOnItemClickListener(adapter);
    }


    // BOTTOM BAR BUTTONS
    public void LaunchPhone(View v) {
        Intent i = new Intent(Intent.ACTION_VIEW, CallLog.Calls.CONTENT_URI);
        if (i != null)
            startActivity(i);
    }

    public void LaunchContacts(View v) {
        Intent i = new Intent(Intent.ACTION_VIEW, ContactsContract.Contacts.CONTENT_URI);
        if (i != null)
            startActivity(i);
    }

    public void LaunchSMS(View v) {
        String defaultApplication = Settings.Secure.getString(getContentResolver(), "sms_default_application");
        Intent i = getPackageManager().getLaunchIntentForPackage(defaultApplication);
        if (i != null)
            startActivity(i);
    }

    public void ShowApps(View v) {
        Intent i = new Intent(HomeScreenActivity.this, AppsListActivity.class);
        i.putExtra("activity", "normal");
        startActivity(i);
    }


    private final static int RESULT_CLOSE_ALL = 400;

    @Override
    public void onActivityResult(int request, int result, Intent data) {
        if (result == RESULT_CLOSE_ALL) {
            setResult(RESULT_CLOSE_ALL);
            finish();
        } else
            Globals.widgetManager.onActivityResult(request, result, data);
    }

}
