package wolfusz.voidlauncher.packages;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import wolfusz.voidlauncher.essentials.Utils;

/**
 * Created by wolf2 on 12.04.2016.
 */
public class PackageInfo implements Comparable<PackageInfo> {

    // FIELDS
    public CharSequence label;
    public Drawable icon;

    // CONSTRUCTORS
    public PackageInfo(CharSequence label, Drawable icon) {
        super();
        this.label = label;
        this.icon = icon;
    }
    public PackageInfo(CharSequence label)				  { this(label, null);		}
    public PackageInfo()								  { this("");				}

    @Override
    public String toString() {
        return this.label.toString();
    }

    // Collections.sort( List<PackageInfo> );
    // used in Utils.sortHashMap too :)
    @Override
    public int compareTo(PackageInfo another) {
        return Utils.capitalize( this.toString() ).compareTo( Utils.capitalize(another.toString()) );
    }


    // VIEW HOLDER FOR ADAPTERS
    public static class ViewHolder {
        public ImageView icon;
        public TextView label;
    }

}
