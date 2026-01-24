package com.nugget.hios.ui.common;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import com.google.android.material.materialswitch.MaterialSwitch;


import androidx.webkit.internal.ApiFeature;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.nugget.hios.HiClubSettingsActivity;
import com.nugget.hios.HelpcenterActivity;
import com.nugget.hios.R;
import com.nugget.hios.ui.preferences.AboutFragment;
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

            case "apps_settings":
                bindAppsSettings(root, activity);
                break;

            /*case "websites_settings":
                bindWebsitesSettings(root, activity);
                break;

                //TODO: was needing to add websites and socials settings...
            case "socials_settings":
                bindSocialsSettings(root, activity);*/

            case "helpcenter_home":
                bindHelpcenterHome(root, activity);
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

        View about = root.findViewById(R.id.btn_about);
        if (about != null) {
            // FIXED: Was 'appearance.setOnClickListener'
            about.setOnClickListener(view -> {
                if (activity instanceof HiClubSettingsActivity) {
                    ((HiClubSettingsActivity) activity).openAbout();
                }
            });
        }

        View privacyPolicy = root.findViewById(R.id.btn_privacypolicy);
        if (privacyPolicy != null) {
            // FIXED: Was 'appearance.setOnClickListener'
            privacyPolicy.setOnClickListener(view -> {
                if (activity instanceof HiClubSettingsActivity) {
                    ((HiClubSettingsActivity) activity).openPrivacyPolicy();
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

        View apps = root.findViewById(R.id.btn_apps);
        if (apps != null) {
            apps.setOnClickListener(view -> {
                if (activity instanceof HiClubSettingsActivity) {
                    ((HiClubSettingsActivity) activity).openApps();
                }
            });
        }

        /*View websites = root.findViewById(R.id.btn_websites);
        if (websites != null) {
            websites.setOnClickListener(view -> {
                /*if (activity instanceof HiClubSettingsActivity) {
                    ((HiClubSettingsActivity) activity)
                }*//*
                Toast.makeText(activity, "Screen coming soon...", Toast.LENGTH_SHORT).show();
            });
        }*/

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

        View prereleaseUpdate = root.findViewById(R.id.btn_unstable_update);
        if (prereleaseUpdate != null) {
            prereleaseUpdate.setOnClickListener(view -> {
                openWebsite(activity, "https://github.com/aarjay123/harmony/releases");
            });
        }

        View whatsapp = root.findViewById(R.id.btn_update_notifications);
        if (whatsapp != null) {
            whatsapp.setOnClickListener(view -> {
                openWebsite(activity, "https://whatsapp.com/channel/0029Va8U5lXISTkDAhk4bj3J");
            });
        }

        View changelog = root.findViewById(R.id.btn_changelog);
        if (changelog != null) {
            changelog.setOnClickListener(view -> {
                openWebsite(activity, "https://github.com/aarjay123/harmony/releases/latest");
            });
        }

        TextView versionSummary = root.findViewById(R.id.currentversion_summary);
        if (versionSummary != null) {
            String versionName = getVersionName(activity);
            versionSummary.setText(activity.getString(R.string.currentversion_summary, versionName));
        }
    }

    private static void bindAppsSettings(View root, Activity activity) {
        View harmonyButton = root.findViewById(R.id.btn_harmony);
        if (harmonyButton != null) {
            harmonyButton.setOnClickListener(view -> {
                openWebsite(activity, "https://hienterprises.github.io/harmony/home");
            });
        }

        View hiosmusicButton = root.findViewById(R.id.btn_hiosmusic);
        if (hiosmusicButton != null) {
            hiosmusicButton.setOnClickListener(view -> {
                openWebsite(activity, "https://github.com/aarjay123/hiosmusic/releases/latest");
            });
        }

        View nuggetdevButton = root.findViewById(R.id.btn_nuggetdev);
        if (nuggetdevButton != null) {
            nuggetdevButton.setOnClickListener(view -> {
                openWebsite(activity, "https://hienterprises.github.io/nuggetdev/home");
            });
        }
    }

    /*
    This is the ScreenBinder for the help center -- it just needs the helpcenterhome screen binder, since it's all fragments.
     */
    private static void bindHelpcenterHome(View root, Activity activity) {
        View tutorial = root.findViewById(R.id.btn_tutorial);
        if (tutorial != null) {
            tutorial.setOnClickListener(view -> {
                if (activity instanceof HelpcenterActivity) {
                    ((HelpcenterActivity) activity).openTutorial();
                }
            });
        }

        View food = root.findViewById(R.id.btn_foodhelp);
        if (food != null) {
            food.setOnClickListener(view -> {
                if (activity instanceof HelpcenterActivity) {
                    ((HelpcenterActivity) activity).openFood();
                }
            });
        }

        View hotel = root.findViewById(R.id.btn_hotelhelp);
        if (hotel != null) {
            hotel.setOnClickListener(view -> {
                if (activity instanceof HelpcenterActivity) {
                    ((HelpcenterActivity) activity).openHotel();
                }
            });
        }

        View roomkey = root.findViewById(R.id.btn_roomkeyhelp);
        if (roomkey != null) {
            roomkey.setOnClickListener(view -> {
                if (activity instanceof HelpcenterActivity) {
                    ((HelpcenterActivity) activity).openRoomkey();
                }
            });
        }

        View support = root.findViewById(R.id.btn_supporthelp);
        if (support != null) {
            support.setOnClickListener(view -> {
                if (activity instanceof HelpcenterActivity) {
                    ((HelpcenterActivity) activity).openSupport();
                }
            });
        }

        View internet = root.findViewById(R.id.btn_internethelp);
        if (internet != null) {
            internet.setOnClickListener(view -> {
                if (activity instanceof HelpcenterActivity) {
                    ((HelpcenterActivity) activity).openInternet();
                }
            });
        }

        View updates = root.findViewById(R.id.btn_updateshelp);
        if (updates != null) {
            updates.setOnClickListener(view -> {
                if (activity instanceof HelpcenterActivity) {
                    ((HelpcenterActivity) activity).openUpdates();
                }
            });
        }

        View comingSoon = root.findViewById(R.id.btn_comingsoon);
        if (comingSoon != null) {
            comingSoon.setOnClickListener(view -> {
                if (activity instanceof HelpcenterActivity) {
                    ((HelpcenterActivity) activity).comingSoon();
                }
            });
        }

        View termsConditions = root.findViewById(R.id.btn_termsconditions);
        if (termsConditions != null) {
            termsConditions.setOnClickListener(view -> {
                if (activity instanceof HelpcenterActivity) {
                    ((HelpcenterActivity) activity).termsConditions();
                }
            });
        }

        View feedback = root.findViewById(R.id.btn_feedback);
        if (feedback != null) {
            feedback.setOnClickListener(view -> {
                if (activity instanceof HelpcenterActivity) {
                    ((HelpcenterActivity) activity).sendFeedback();
                }
            });
        }
    }

    //Universal method for opening a website -- eliminates code repetition
    private static void openWebsite(Activity activity, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
    }

    //method for getting the installed version of HiOSMobile's version name
    private static String getVersionName(Activity activity) {
        try {
            PackageManager pm = activity.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(activity.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "Unknown";
        }
    }
}