package com.nugget.hios;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

// We use standard AppCompatActivity because we are not using PreferenceFragments here.
public class HiClubSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiclub_settings);

        // 1. Setup Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Club Settings");
        }

        // 2. Setup Manual Click Listeners
        // Appearance
        LinearLayout btnAppearance = findViewById(R.id.btn_appearance);
        btnAppearance.setOnClickListener(v -> {
            // For now, let's toast. Ideally, you launch a new Activity or Fragment here.
            Toast.makeText(this, "Opening Appearance...", Toast.LENGTH_SHORT).show();
            // Example: startActivity(new Intent(this, ClubAppearanceActivity.class));
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
        finish();
        return true;
    }
}