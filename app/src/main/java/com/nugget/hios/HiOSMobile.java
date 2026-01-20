package com.nugget.hios;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.preference.PreferenceManager;

import com.google.android.material.color.DynamicColors;
import com.google.android.material.color.DynamicColorsOptions;

public class HiOSMobile extends Application {

    public static final String KEY_DYNAMIC_COLOUR = "pref_dynamic_colour";
    public static final String KEY_THEME_MODE = "pref_theme_mode";

    private static int mapThemeMode(String value) {
        if ("light".equals(value)) return AppCompatDelegate.MODE_NIGHT_NO;
        if ("dark".equals(value)) return AppCompatDelegate.MODE_NIGHT_YES;
        return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM; //auto
    }

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String mode = sp.getString(KEY_THEME_MODE, "auto");
        AppCompatDelegate.setDefaultNightMode(mapThemeMode(mode));

        DynamicColorsOptions options = new DynamicColorsOptions.Builder()
                .setPrecondition((Activity activity, int themeResId) -> {
                    if (!DynamicColors.isDynamicColorAvailable()) return false;

                    PreferenceManager.getDefaultSharedPreferences(activity);

                    return sp.getBoolean(KEY_DYNAMIC_COLOUR, true);
                })
                .build();

        DynamicColors.applyToActivitiesIfAvailable(this, options);
    }
}
