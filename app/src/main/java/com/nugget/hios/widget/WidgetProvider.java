/*import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.nugget.hios.R;

public class WidgetProvider extends AppWidgetProvider {

    public static final String ACTION_HOME = "com.nugget.hios.ui.home.HomeFragment";
    public static final String ACTION_RESTAURANT = "com.nugget.hios.ui.dashboard.DashboardFragment";
    public static final String ACTION_HOTEL = "com.nugget.hios.ui.notifications.NotificationsFragment";
    public static final String ACTION_ROOMKEY = "com.nugget.hios.ui.settings.SettingsFragment";

    @Override
    public void onUpdate(Context context, AppWidgetProvider appWidgetProvider, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetProvider, appWidgetId);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        //PendingIntents
        Intent homeIntent = new Intent(ACTION_HOME);
        PendingIntent homePendingIntent = PendingIntent.getBroadcast(context, 0, homeIntent, PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_button_home, homePendingIntent);

        Intent restaurantIntent = new Intent(ACTION_RESTAURANT);
        PendingIntent restaurantPendingIntent = PendingIntent.getBroadcast(context, 0, restaurantIntent, PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_button_restaurant, restaurantPendingIntent);

        Intent hotelIntent = new Intent(ACTION_HOTEL);
        PendingIntent hotelPendingIntent = PendingIntent.getBroadcast(context, 0, hotelIntent, PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_button_hotel, hotelPendingIntent);

        Intent roomkeyIntent = new Intent(ACTION_ROOMKEY);
        PendingIntent roomkeyPendingIntent = PendingIntent.getBroadcast(context, 0, roomkeyIntent, PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_button_roomkey, roomkeyPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}*/