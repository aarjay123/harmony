package com.nugget.hios.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.nugget.hios.MainActivity;
import com.nugget.hios.R;

public class WidgetProvider extends AppWidgetProvider {

    public static final String HOME_PAGE = "com.nugget.hios.ui.home.HomeFragment";
    public static final String RESTAURANT_PAGE = "com.nugget.hios.ui.dashboard.DashboardFragment";
    public static final String HOTEL_PAGE = "com.nugget.hios.ui.notifications.NotificationsFragment";
    public static final String ROOMKEY_PAGE = "com.nugget.hios.ui.settings.SettingsFragment";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        //create PendingIntents for each button
        Intent homePageIntent = new Intent(HOME_PAGE);
        PendingIntent homePagePendingIntent = PendingIntent.getBroadcast(context, 0, homePageIntent, PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_home, homePagePendingIntent);

        Intent restaurantPageIntent = new Intent(RESTAURANT_PAGE);
        PendingIntent restaurantPagePendingIntent = PendingIntent.getBroadcast(context, 0, restaurantPageIntent, PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_restaurant, restaurantPagePendingIntent);

        Intent hotelPageIntent = new Intent(HOTEL_PAGE);
        PendingIntent hotelPagePendingIntent = PendingIntent.getBroadcast(context, 0, hotelPageIntent, PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_hotel, hotelPagePendingIntent);

        Intent roomkeyPageIntent = new Intent(ROOMKEY_PAGE);
        PendingIntent roomkeyPagePendingIntent = PendingIntent.getBroadcast(context, 0, roomkeyPageIntent, PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_roomkey, roomkeyPagePendingIntent);

        //update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
