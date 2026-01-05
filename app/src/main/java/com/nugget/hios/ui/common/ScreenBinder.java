package com.nugget.hios.ui.common;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.webkit.internal.ApiFeature;

import com.nugget.hios.HiClubSettingsActivity;
import com.nugget.hios.R;

public final class ScreenBinder {

    private ScreenBinder() {}

    public static void bind(String screenKey, View root, Activity activity) {
        switch (screenKey) {
            case "settings_home":
                bindSettingsHome(root, activity);
                break;

            case "updates_settings":
                bindUpdatesSettings(root, activity);
                break;

            default:
                break;
        }
    }

    private static void bindSettingsHome(View root, Activity activity) {
        View appearance = root.findViewById(R.id.btn_appearance);
        if (appearance != null) {
            appearance.setOnClickListener(view -> {
                if (activity instanceof HiClubSettingsActivity) {
                    ((HiClubSettingsActivity) activity).openAppearance();
                }
            });
        }

        View updates = root.findViewById(R.id.btn_updates);
        if (updates != null) {
            updates.setOnClickListener(view -> {
                if (activity instanceof HiClubSettingsActivity) {
                    ((HiClubSettingsActivity) activity).openUpdates();
                }
            });
        }

        View websites = root.findViewById(R.id.btn_websites);
        if (websites != null) {
            websites.setOnClickListener(view -> {
                /*if (activity instanceof HiClubSettingsActivity) {
                    ((HiClubSettingsActivity) activity)
                }*/
                Toast.makeText(activity, "Screen coming soon...", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private static void bindUpdatesSettings(View root, Activity activity) {
        View manuallyUpdate = root.findViewById(R.id.btn_manual_update);
        if (manuallyUpdate != null) {
            manuallyUpdate.setOnClickListener(view -> {
                Uri uri = Uri.parse("https://github.com/aarjay123/harmony/releases/latest");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(intent);
            });
        }
    }
}
