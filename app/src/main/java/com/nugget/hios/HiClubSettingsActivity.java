package com.nugget.hios;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.nugget.hios.ui.common.GenericLayoutFragment;
import com.nugget.hios.ui.preferences.AboutFragment;
import com.nugget.hios.ui.preferences.AppearanceFragment;
import com.nugget.hios.ui.preferences.PrivacypolicyFragment;

public class HiClubSettingsActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;

    private static final String TITLE_HOME = "Settings";

    // Helper to load sub-pages
    private void loadFragment(Fragment fragment, String title) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in, android.R.anim.fade_out
                )
                .replace(R.id.settings_container, fragment)
                .addToBackStack(title)   // store title for back navigation
                .commit();

        collapsingToolbar.setTitle(title);
        appBarLayout.setExpanded(false, true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiclub_settings);

        // Toolbar setup
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        appBarLayout = findViewById(R.id.appbar);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(TITLE_HOME);

        // Load the home fragment once
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.settings_container,
                            GenericLayoutFragment.newInstance(
                                    R.layout.fragment_home_settings,
                                    "Settings",
                                    "settings_home"
                            )
                    )
                    .commit();
        }

        // Keep title in sync with back stack
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            int count = getSupportFragmentManager().getBackStackEntryCount();

            if (count == 0) {
                collapsingToolbar.setTitle(TITLE_HOME);
                appBarLayout.setExpanded(true, true);
            } else {
                String title = getSupportFragmentManager()
                        .getBackStackEntryAt(count - 1)
                        .getName();
                collapsingToolbar.setTitle(title);
                appBarLayout.setExpanded(false, true);
            }
        });
    }

    // --- Called from SettingsHomeFragment ---

    public void openAppearance() {
        loadFragment(new AppearanceFragment(), "Customise HiOSCore");
    }

    public void openUpdates() {
        loadFragment(GenericLayoutFragment.newInstance(
                R.layout.fragment_updates_settings,
                "Updates",
                "updates_settings"
            ),
            "Updates"
        );
    }

    public void openAbout() {
        loadFragment(new AboutFragment(), "About HiClub");
    }

    public void openPrivacyPolicy() {
        loadFragment(new PrivacypolicyFragment(), "Privacy Policy");
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