package wolfusz.voidlauncher.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import wolfusz.voidlauncher.Globals;
import wolfusz.voidlauncher.Preferences;
import wolfusz.voidlauncher.R;
import wolfusz.voidlauncher.essentials.D;
import wolfusz.voidlauncher.essentials.Utils;

/**
 * Created by wolf2 on 12.04.2016.
 */
public class WidgetManager {
    private Activity context;

    public WidgetHost widgetHost;
    public AppWidgetManager widgetManager;

    public WidgetManager(final Activity context, ViewGroup widgetsView) {
        Globals.widgetManager = this;
        this.context = context;

        this.widgetManager = AppWidgetManager.getInstance(context);
        this.widgetHost = new WidgetHost(context, R.id.APPWIDGET_HOST_ID);

        this.widgetsView = widgetsView;
        // Initialize widgetView contextMenu
        widgetsView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(context);
                String [] items = {
                        "Add widget",
                        "Add shortcut"
                };

                b.setItems(items, new DialogInterface.OnClickListener() {
                    @SuppressWarnings({ "rawtypes", "unchecked" })
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){

                            // selected "Add widget"
                            case 0: {
                                int appWidgetId = Globals.widgetManager.widgetHost.allocateAppWidgetId();
                                Intent i = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
                                i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

                                // ADD EMPTY DATA
                                ArrayList customInfo = new ArrayList();
                                i.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo);
                                ArrayList customExtras = new ArrayList();
                                i.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras);

                                context.startActivityForResult(i, WidgetManager.REQUEST_PICK_APPWIDGET);
                            } break;

                            // selected "Add shortcut"
                            case 1: {
                                Intent i = new Intent(Intent.ACTION_PICK_ACTIVITY);
                                i.putExtra(Intent.EXTRA_INTENT, new Intent(Intent.ACTION_CREATE_SHORTCUT));
                                context.startActivityForResult(i, WidgetManager.REQUEST_PICK_SHORTCUT);
                            } break;

                        }
                    }
                });
                b.create().show();
                return true;
            }
        });

        widgets = new ArrayList<Widget>();
        // load already added widgets
        if (! Preferences.widgets().isEmpty()) {
            new D("ATTEMPT TO RECREATE WIDGET");
            for (WidgetData widgetData : Preferences.widgets()) {
                new D("-----");

                // load LayoutParams from preferences
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( (Integer)widgetData.get("width"), (Integer)widgetData.get("height") );
                params.leftMargin = (Integer)widgetData.get("left");
                params.rightMargin = (Integer)widgetData.get("right");
                params.topMargin = (Integer)widgetData.get("top");
                params.bottomMargin = (Integer)widgetData.get("bottom");
                new D(params.width+"x"+params.height+", "+params.leftMargin+":"+params.topMargin+", "+params.rightMargin+":"+params.bottomMargin);

                Widget widget = null;

                switch (widgetData.type) {
                    case Shortcut: {
                        new D("Shortcut");
                        // decode icon from base64 string
                        byte[] imageAsBytes = Base64.decode( ((String) widgetData.get("icon")).getBytes(), Base64.DEFAULT );
                        Bitmap icon = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                        try {
                            // create view
                            new D(widgetData.get("text"));
                            new D(widgetData.get("intent"));
                            widget = new Widget(widgetData.type,
                                    createShortcutView(icon, (String)widgetData.get("text"), Intent.parseUri((String)widgetData.get("intent"), Intent.URI_INTENT_SCHEME)),
                                    params);
                        } catch (Exception e) { }
                    } break;

                    case Widget: {
                        new D("Widget");
                        // get app widget
                        int appWidgetId = (Integer)widgetData.get("id");
                        new D(appWidgetId);
                        // create widget with specified id
                        AppWidgetProviderInfo appWidgetInfo = widgetManager.getAppWidgetInfo(appWidgetId);
                        WidgetHost.View widgetView = (WidgetHost.View) widgetHost.createView(context, appWidgetId, appWidgetInfo);
                        widget = new Widget(widgetData.type, widgetView, params);
                    } break;
                }

                if (widget != null) {
                    new D("Yay let's do this!");
                    // create widget
                    widget.putData(widgetData);
                    widgetsView.addView(widget.view, widget.params);
                    widgets.add(widget);
                }
            }
        }
    }


    public void reset() {
        for (Widget widget : new ArrayList<Widget>(widgets))
            removeWidget(widget);
        widgets.clear();
    }


    // WIDGETS MANAGING
    public ViewGroup widgetsView;
    public List<Widget> widgets;

    public void addWidget(Widget widget) {
        widgetsView.addView(widget.view, widget.params);
        widgets.add(widget);
        Preferences.addWidget(widget.data);
    }

    public void removeWidget(Widget widget) {
        if (widget.data.type == Widget.Type.Widget)
            widgetHost.deleteAppWidgetId(((WidgetHost.View) widget.view).getAppWidgetId());
        widgetsView.removeView(widget.view);
        widgets.remove(widget);
        Preferences.removeWidget(widget.data);
    }


    // WIDGETS UPDATING
    public void startListening() { this.widgetHost.startListening(); }
    public void stopListening() { this.widgetHost.stopListening(); }


    // HANDLING REQUESTS
    public final static int REQUEST_CREATE_APPWIDGET = 603;
    public final static int REQUEST_PICK_APPWIDGET  = 602;
    public final static int REQUEST_CREATE_SHORTCUT  = 601;
    public final static int REQUEST_PICK_SHORTCUT  = 600;

    public boolean onActivityResult(int request, int result, Intent data) {
        if (result == Activity.RESULT_OK) {
            switch (request) {
                case REQUEST_CREATE_APPWIDGET:	{	createWidget(data);			} break;
                case REQUEST_PICK_APPWIDGET:	{	configureWidget(data);		} break;
                case REQUEST_CREATE_SHORTCUT:	{	createShortcut(data);		} break;
                case REQUEST_PICK_SHORTCUT:		{	configureShortcut(data);	} break;
            }
        } else if (result == Activity.RESULT_CANCELED && data != null) {
            int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
            if (appWidgetId != -1)
                widgetHost.deleteAppWidgetId(appWidgetId);
        }
        return false;
    }



    // ACTUAL MANAGING WIDGETS AND SHORTCUTS
    // Widgets
    public void configureWidget(Intent data) {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidgetInfo = widgetManager.getAppWidgetInfo(appWidgetId);
        if (appWidgetInfo.configure == null)
            createWidget(data);
        else {
            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            intent.setComponent(appWidgetInfo.configure);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            context.startActivityForResult(intent, WidgetManager.REQUEST_CREATE_APPWIDGET);
        }
    }

    public void createWidget(Intent data) {
        Bundle extras = data.getExtras();
        int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
        AppWidgetProviderInfo appWidgetInfo = widgetManager.getAppWidgetInfo(appWidgetId);
        WidgetHost.View widgetView = (WidgetHost.View) widgetHost.createView(context, appWidgetId, appWidgetInfo);
        widgetView.setAppWidget(appWidgetId, appWidgetInfo);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = 100;
        params.topMargin = 100;

        Widget widget = new Widget(Widget.Type.Widget, widgetView, params);
        widget.putData("id", appWidgetId);
        addWidget(widget);
    }


    // Shortcuts
    public void configureShortcut(Intent intent) {
        context.startActivityForResult(intent, WidgetManager.REQUEST_CREATE_SHORTCUT);
    }

    public View createShortcutView(Bitmap icon, String label, Intent tag) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.desktop_item, null);

        ImageView image = (ImageView)layout.findViewById(R.id.icon);
        image.setImageDrawable(new BitmapDrawable(context.getResources(), icon));

        TextView item = (TextView)layout.findViewById(R.id.label);
        item.setText(label);

        layout.setTag(tag);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity( (Intent) v.getTag() );
            }
        });
        return layout;
    }

    public void createShortcut(Intent intent) {
        new D(intent.toUri(Intent.URI_INTENT_SCHEME));
        Intent.ShortcutIconResource iconResource = intent.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE);
        Bitmap icon                              = intent.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON);
        String shortcutLabel                     = intent.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);
        Intent shortIntent                       = intent.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT);

        if (icon==null) {
            if (iconResource!=null){
                Resources resources = null;
                try {
                    resources = context.getPackageManager().getResourcesForApplication(iconResource.packageName);
                } catch(PackageManager.NameNotFoundException e) { }
                if (resources != null) {
                    int id = resources.getIdentifier(iconResource.resourceName, null, null);
                    if (resources.getDrawable(id) instanceof StateListDrawable) {
                        Drawable d = ((StateListDrawable)resources.getDrawable(id)).getCurrent();
                        icon = ((BitmapDrawable)d).getBitmap();
                    }else
                        icon = ((BitmapDrawable)resources.getDrawable(id)).getBitmap();
                }
            }
        }

        if (shortcutLabel != null && shortIntent != null && icon != null){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
            params.leftMargin = 10;
            params.topMargin = Utils.getStatusBarHeight()+5;

            RelativeLayout layout = (RelativeLayout) createShortcutView(icon, shortcutLabel, shortIntent);

            // CONVERT ICON TO BASE64 STRING
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            icon.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String iconBase64 = Base64.encodeToString(b, Base64.DEFAULT);


            Widget widget = new Widget(Widget.Type.Shortcut, layout, params);
            widget.putData("icon", iconBase64);
            widget.putData("text", shortcutLabel);
            widget.putData("intent", shortIntent.toUri(Intent.URI_INTENT_SCHEME));
            addWidget(widget);
        }
    }

}
