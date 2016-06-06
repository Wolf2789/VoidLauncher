package wolfusz.voidlauncher;

import android.app.Activity;
import android.content.Context;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import wolfusz.voidlauncher.essentials.D;
import wolfusz.voidlauncher.widget.WidgetData;

/**
 * Created by wolf2 on 12.04.2016.
 */
public class Preferences {

    // DEFAULT PREFERENCES
    public static final String[] default_excludePackages = new String[]{
            "wolfusz.voidlauncher"
    };

    public static final String[] default_hiddenPackages = new String[]{
            "org.adaway"
    };


    // CLASS THAT HELPS SAVING AND LOADING DATA
    public static class PreferencesObject implements Serializable {
        private static final long serialVersionUID = -3583198283520231862L;

        public List<String> hiddenPackages;
        public List<String> excludedPackages;
        public List<WidgetData> widgets;

        public PreferencesObject() {
            hiddenPackages = new ArrayList<String>();
            excludedPackages = new ArrayList<String>();
            widgets = new ArrayList<WidgetData>();
        }

        public boolean isEmpty() {
            return (hiddenPackages == null		|| hiddenPackages.isEmpty()		||
                    excludedPackages == null	|| excludedPackages.isEmpty()	||
                    widgets == null				|| widgets.isEmpty()
            );
        }
    }


    // LOADED PREFERENCES
    public static PreferencesObject loadedPreferences;

    public static List<String> hiddenPackages() { return loadedPreferences.hiddenPackages; }
    public static List<String> excludedPackages() { return loadedPreferences.excludedPackages; }

    public static void hidePackage(String packageToHide) { loadedPreferences.hiddenPackages.add(packageToHide); }
    public static void unhidePackage(String packageToUnhide) { loadedPreferences.hiddenPackages.remove(packageToUnhide); }
    public static void exludePackage(String packageToExclude) { loadedPreferences.excludedPackages.add(packageToExclude); }

    public static List<WidgetData> widgets() { return loadedPreferences.widgets; }

    public static void addWidget(WidgetData widget) { loadedPreferences.widgets.add(widget); }
    public static void removeWidget(WidgetData widget) { loadedPreferences.widgets.remove(widget); }

    private static Activity context;

    // ACTUAL SAVING AND LOADING
    public static void savePreferences() {
        new D("# SAVING PREFERENCES");
        new D("@ HIDDEN");
        new D(loadedPreferences.hiddenPackages);
        new D("@ EXCLUDED");
        new D(loadedPreferences.excludedPackages);
        new D("@ WIDGETS");
        new D(loadedPreferences.widgets);

        FileOutputStream fos;
        try {

            PreferencesObject pref = new PreferencesObject();
            pref.hiddenPackages.addAll( Preferences.hiddenPackages() );
            pref.excludedPackages.addAll( Preferences.excludedPackages() );
            pref.widgets.addAll( Preferences.widgets() );

            fos = context.openFileOutput("preferences", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(pref);
            os.close();

        } catch (Exception e) { new D(e.getMessage()); }
    }

    public static void loadPreferences(Activity context) {
        new D("# LOADING PREFERENCES");

        Preferences.context = context;
        loadedPreferences = null;
        ObjectInputStream is = null;
        try {

            is = new ObjectInputStream(context.openFileInput("preferences"));
            Object obj = is.readObject();
            if (obj instanceof PreferencesObject)
                loadedPreferences = (PreferencesObject)obj;

        } catch (Exception e) {
            new D(e.getMessage());
//			Toast.makeText(null, "Failed to load preferences. Loading default preferences.", Toast.LENGTH_LONG).show();
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (Exception e) { new D(e.getMessage()); }
        }

        if (loadedPreferences == null || loadedPreferences.isEmpty()) {
            new D("DEFAULT");
            loadedPreferences = new PreferencesObject();
            loadedPreferences.hiddenPackages.addAll( Arrays.asList(default_hiddenPackages) );
            loadedPreferences.excludedPackages.addAll( Arrays.asList(default_excludePackages) );
        }
        new D("@ HIDDEN");
        new D(loadedPreferences.hiddenPackages);
        new D("@ EXCLUDED");
        new D(loadedPreferences.excludedPackages);
        new D("@ WIDGETS");
        new D(loadedPreferences.widgets);
    }

    public static void reset() {
        new D("# RESETTING PREFERENCES");
        loadedPreferences.hiddenPackages.clear();
        loadedPreferences.excludedPackages.clear();
        loadedPreferences.widgets.clear();
        savePreferences();
    }

}
