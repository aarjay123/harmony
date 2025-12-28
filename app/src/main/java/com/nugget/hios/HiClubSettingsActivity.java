package com.nugget.hios;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.nugget.hios.ui.preferences.AppearanceFragment;

// We use standard AppCompatActivity because we are not using PreferenceFragments here.
public class HiClubSettingsActivity extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;

    //helper to help loading fragments
    private void loadFragment(Fragment fragment, String title) {
        getSupportFragmentManager().beginTransaction()
                //animation for nice entry
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
                //replace settings container content with new fragment
                .replace(R.id.settings_container, fragment)
                //add back stack so back button works
                .addToBackStack(null)
                .commit();

        //updating the title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiclub_settings);

        // 1. Setup Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        appBarLayout = findViewById(R.id.appbar);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Preferences");

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                collapsingToolbar.setTitle("Preferences");
                appBarLayout.setExpanded(true, true);
            }
        });

        // 2. Setup Manual Click Listeners
        // Appearance
        findViewById(R.id.btn_appearance).setOnClickListener(v -> {
            loadFragment(new AppearanceFragment(), "Appearance");
            appBarLayout.setExpanded(false, true);
        });

        // Websites
        LinearLayout btnWebsites = findViewById(R.id.btn_websites);
        btnWebsites.setOnClickListener(v -> {
            Toast.makeText(this, "Opening Websites...", Toast.LENGTH_SHORT).show();
        });

        // Apps
        LinearLayout btnApps = findViewById(R.id.btn_apps);
        btnApps.setOnClickListener(v -> {
            Toast.makeText(this, "Opening Apps...", Toast.LENGTH_SHORT).show();
        });

        // Socials
        LinearLayout btnSocials = findViewById(R.id.btn_socials);
        btnSocials.setOnClickListener(v -> {
            Toast.makeText(this, "Opening Socials...", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (getSupportFragmentManager().popBackStackImmediate()) {
            return true;
        }

        finish();
        return true;
    }
}