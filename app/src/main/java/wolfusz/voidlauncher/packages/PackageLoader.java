package wolfusz.voidlauncher.packages;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

import wolfusz.voidlauncher.Globals;
import wolfusz.voidlauncher.Preferences;
import wolfusz.voidlauncher.essentials.Label;
import wolfusz.voidlauncher.essentials.Utils;

/**
 * Created by wolf2 on 12.04.2016.
 */
public class PackageLoader {
    public Context context;
    public PackageManager pm;

    // package => { name, icon, hidden }
    private LinkedHashMap<String,PackageInfo> packages;

    public PackageLoader(Context context) {
        Globals.packageLoader = this;
        this.context = context;
        this.packages = new LinkedHashMap<String,PackageInfo>();
    }


    public void loadPackages() {
        pm = context.getPackageManager();

        // reset packages list
        if (packages == null)
            packages = new LinkedHashMap<String, PackageInfo>();
        packages.clear();

        // create intent filter to show only launchable apps
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> availableActivities = pm.queryIntentActivities(i, 0);

        for (ResolveInfo ri : availableActivities) {
            // do nothing if package is excluded
            if (Preferences.excludedPackages().contains(ri.activityInfo.packageName))
                continue;

            // load label and icon for package
            PackageInfo pi = new PackageInfo( ri.loadLabel(pm), ri.loadIcon(pm) );

            // relate package with packageinfo
            packages.put(ri.activityInfo.packageName, pi);
        }

        // sort results
        LinkedHashMap<String, PackageInfo> result = Utils.sortHashMap(packages);
        // put results to global list
        packages.clear();
        packages.putAll(result);
    }


    // GET NORMAL AND/OR HIDDEN PACKAGES
    public List<String> getApps(boolean hidden) {
        List<String> result = new ArrayList<String>();
        for (String p : packages.keySet()) {
            if (Preferences.hiddenPackages().contains(p.toString()) == hidden)
                result.add(p);
        }
        // if result is empty then return null
        return result.isEmpty() ? null : result;
    }

    public List<String> getPackages() { return getApps(false); }
    public List<String> getHiddenPackages() { return getApps(true); }

    public LinkedHashMap<String, PackageInfo> getAllPackages() { return packages; }


    // HIDING AND EXCLUDING PACKAGES
    public void hidePackage(String name) {
        Preferences.hidePackage(name);
    }

    public void unhidePackage(String name) {
        Preferences.unhidePackage(name);
    }

    public void excludePackage(String name) {
        packages.remove(name);
        Preferences.exludePackage(name);
    }


    // LABELS
    // Load default labels
    public static List<Label> loadDefaultLabels() {
        List<Label> labels = new ArrayList<Label>();
        // deafult labels
        labels.add(new Label("!@#"));
        labels.add(new Label("0-9","^[0-9]"));

        String[] sortLabels = new String[]{
                "A","Ą","B","C","Ć","D","E","Ę",
                "F","G","H","I","J",
                "K","L","Ł","M","N","Ń","O","Ó",
                "P","Q","R","S","Ś",
                "T","U","V",
                "W","X","Y","Z","Ź","Ż"
        };
        String defaultRegex = "^[^";
        for (String l : sortLabels) {
            Label L = new Label(l, "^[" + l.toUpperCase()+l.toLowerCase() + "]");
            labels.add(L);
            defaultRegex += l.toUpperCase()+l.toLowerCase();
        }
        // regex for first label
        labels.get(0).setRegex(defaultRegex+"]");

        return labels;
    }


    // loading labels for packages and sorting them by name
    public static List<Label> loadLabels(List<String> packagesList) {
        if (packagesList == null || packagesList.isEmpty())
            return new ArrayList<Label>();

        // loading default labels
        List<Label> labels = PackageLoader.loadDefaultLabels();

        // load labels from packages list
        int pos = -1;
        for (Label l : labels) {
            Pattern p = Pattern.compile(l.getRegex());
            for (String name : packagesList) {
                PackageInfo item = Globals.packageLoader.packages.get(name);
                if (item != null) {
                    if (p.matcher( item.label ).find()){
                        pos += 1;
                        if (l.getFirstApp() == -1)
                            l.setFirstApp(pos);
                    }
                }
            }
        }

        // delete labels that doesn't match any package
        Iterator<Label> iterator = labels.iterator();
        while (iterator.hasNext()) {
            Label l = iterator.next();
            if (l.getFirstApp() == -1)
                iterator.remove();
        }

        return labels;
    }

}
