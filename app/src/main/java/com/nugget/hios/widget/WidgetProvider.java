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

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        CharSequence widgetText = context.getString(R.string.app_name);
        CharSequence buttonText = context.getString(R.string.app_name);

        //Construct RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        //Set text to widget
        views.setTextViewText(R.id.widget_title, widgetText);
        views.setTextViewText(R.id.widget_button, buttonText);

        //Create intent to launch when the button is clicked
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        //Set the click listener for the button
        views.setOnClickPendingIntent(R.id.widget_button, pendingIntent);

        //Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onEnabled(Context context) {
        //relevant actions for when first widget created
    }

    @Override
    public void onDisabled(Context context) {
        //relevant actions for when last widget removed
    }
}
