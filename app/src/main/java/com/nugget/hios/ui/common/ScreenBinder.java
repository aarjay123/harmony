package com.nugget.hios.ui.common;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import com.google.android.material.materialswitch.MaterialSwitch;


import androidx.webkit.internal.ApiFeature;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.nugget.hios.HiClubSettingsActivity;
import com.nugget.hios.R;
import com.nugget.hios.updates.GithubUpdater;

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
        //TODO: DYNAMIC COLOUR BREAKS AFTER CHANGING THEME!!!!!!

        // --- Dark mode dropdown ---
        MaterialAutoCompleteTextView dropdown = root.findViewById(R.id.dropdown_theme);
        if (dropdown != null) {
            final String KEY_THEME_MODE = "pref_theme_mode";
            final String[] items = new String[]{"Auto", "Light", "Dark"};

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    activity, android.R.layout.simple_list_item_1, items
            ) {
                private final android.widget.Filter noFilter = new android.widget.Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {
                        FilterResults results = new FilterResults();
                        results.values = items;
                        results.count = items.length;
                        return results;
                    }

                    @Override
                    protected void publishResults(CharSequence constraint, FilterResults results) {
                        notifyDataSetChanged();
                    }
                };

                @Override
                public android.widget.Filter getFilter() {
                    return noFilter; // <- disables filtering
                }
            };

            dropdown.setAdapter(adapter);
            dropdown.setKeyListener(null);        // prevent typing
            dropdown.setOnClickListener(v -> dropdown.showDropDown()); // always show full list

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
            String saved = sp.getString(KEY_THEME_MODE, "auto");

            if ("light".equals(saved)) dropdown.setText("Light", false);
            else if ("dark".equals(saved)) dropdown.setText("Dark", false);
            else dropdown.setText("Auto", false);

            dropdown.setOnItemClickListener((parent, itemView, position, id) -> {
                String value = (position == 1) ? "light" : (position == 2) ? "dark" : "auto";
                sp.edit().putString(KEY_THEME_MODE, value).apply();

                int nightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                if ("light".equals(value)) nightMode = AppCompatDelegate.MODE_NIGHT_NO;
                if ("dark".equals(value)) nightMode = AppCompatDelegate.MODE_NIGHT_YES;

                AppCompatDelegate.setDefaultNightMode(nightMode);
                activity.recreate();
            });
        }

        // --- Dynamic colour toggle ---
        MaterialSwitch dynamicSwitch = root.findViewById(R.id.switch_dynamic_colour);
        if (dynamicSwitch != null) {
            final String KEY_DYNAMIC_COLOUR = "pref_dynamic_colour"; // MUST match Application class

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);

            // Set initial state without triggering the listener
            boolean enabled = sp.getBoolean(KEY_DYNAMIC_COLOUR, true);

            dynamicSwitch.setOnCheckedChangeListener(null);
            dynamicSwitch.setChecked(enabled);

            dynamicSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                sp.edit().putBoolean(KEY_DYNAMIC_COLOUR, isChecked).apply();
                activity.recreate(); // needed so the overlay is applied/removed
            });
        }

    }

    private static void bindUpdatesSettings(View root, Activity activity) {
        View manuallyUpdate = root.findViewById(R.id.btn_manual_update);
        if (manuallyUpdate != null) {
            manuallyUpdate.setOnClickListener(view -> {
                GithubUpdater.checkAndUpdate(activity, "aarjay123", "harmony");
            });
        }
    }
}
