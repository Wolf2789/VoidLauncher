package wolfusz.voidlauncher.widget;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;

/**
 * Created by wolf2 on 12.04.2016.
 */
public class WidgetHost extends AppWidgetHost {

    public WidgetHost(Context context, int hostId) {
        super(context, hostId);
    }

    @Override
    public AppWidgetHostView onCreateView(Context context, int widgetId, AppWidgetProviderInfo info) {
        View view = new View(context);
        view.setAppWidget(widgetId, info);
        return view;
    }

    @Override
    public void stopListening() {
        super.stopListening();
        clearViews();
    }


    // HostView for displaying widget
    public class View extends AppWidgetHostView {

        public View(Context context) {
            super(context);
        }
    }

}
