package com.nugget.hios;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.google.android.material.appbar.MaterialToolbar;

public class SettingsActivity extends AppCompatActivity implements
        PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private static final String TITLE_TAG = "settingsActivityTitle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new HeaderFragment())
                    .commit();
        } else {
            setTitle(savedInstanceState.getCharSequence(TITLE_TAG));
        }

        //setting the toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //setting the colour of the toolbar to be the same as the colour of the statusbar
        int statusBarColour = getWindow().getStatusBarColor();
        toolbar.setBackgroundColor(statusBarColour);

        //setting the settings pages background colour to be the same as the statusbar for consistency
        FrameLayout settingsLayout = findViewById(R.id.settings);
        settingsLayout.setBackgroundColor(statusBarColour);

        //setting the system navbar colour to be the same as the statusbar
        getWindow().setNavigationBarColor(statusBarColour);

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                            setTitle(R.string.title_activity_settings);
                        }
                    }
                });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save current activity title so we can set it again after a configuration change
        outState.putCharSequence(TITLE_TAG, getTitle());
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getSupportFragmentManager().popBackStackImmediate()) {
            return true;
        }
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        // Instantiate the new Fragment
        final Bundle args = pref.getExtras();
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());
        fragment.setArguments(args);
        fragment.setTargetFragment(caller, 0);
        // Replace the existing Fragment with the new Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings, fragment)
                .addToBackStack(null)
                .commit();
        setTitle(pref.getTitle());
        return true;
    }

    public static class HeaderFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.header_settingsactivity, rootKey);
        }
    }

    //NEW PREFERENCES FRAGMENTS!!!

    public static class AppearancePreferences extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.appearance_preferences, rootKey);
        }
    }

    public static class UpdatesPreferences extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.updates_preferences, rootKey);
        }
    }

        //MANUAL UPDATE DOWNLOAD
    public void downloadfile(View view) {
        DownloadManager manager;
        manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse("https://github.com/aarjay123/harmony/releases/latest/download/hiosmobile.apk");
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        long reference = manager.enqueue(request);
    }//TODO: MAKE MANUAL UPDATE WORK.

    public static class AppsPreferences extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstance, String rootKey) {
            setPreferencesFromResource(R.xml.apps_preferences, rootKey);
        }
    }

    public static class WebsitePreferences extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstance, String rootKey) {
            setPreferencesFromResource(R.xml.websites_preferences, rootKey);
        }
    }

    public static class SocialsPreferences extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstance, String rootKey) {
            setPreferencesFromResource(R.xml.socials_preferences, rootKey);
        }
    }
}