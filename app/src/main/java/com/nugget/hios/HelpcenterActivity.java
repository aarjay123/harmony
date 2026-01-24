package com.nugget.hios;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.nugget.hios.ui.comingsoon.ComingsoonFragment;
import com.nugget.hios.ui.common.GenericLayoutFragment;
import com.nugget.hios.ui.feedback.FeedbackFragment;
import com.nugget.hios.ui.helpcenter.HotelFragment;
import com.nugget.hios.ui.helpcenter.InternetFragment;
import com.nugget.hios.ui.helpcenter.RestaurantFragment;
import com.nugget.hios.ui.helpcenter.RoomkeyFragment;
import com.nugget.hios.ui.helpcenter.TutorialFragment;
import com.nugget.hios.ui.helpcenter.UpdatesFragment;
import com.nugget.hios.ui.support.SupportFragment;
import com.nugget.hios.ui.tc.TcFragment;

public class HelpcenterActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;

    private static final String TITLE_HOME = "Help";

    //Helper to load subpages
    private void loadFragment(Fragment fragment, String title) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right
                )
                .replace(R.id.helpcenter_container, fragment)
                .addToBackStack(title)
                .commit();

        collapsingToolbar.setTitle(title);
        appBarLayout.setExpanded(false, true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpcenter);

        //toolbar setup
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        appBarLayout = findViewById(R.id.appbar);
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(TITLE_HOME);

        //load home fragment once.
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.helpcenter_container,
                            GenericLayoutFragment.newInstance(
                                    R.layout.fragment_home_helpcenter,
                                    "Help",
                                    "helpcenter_home"
                            )
                    )
                    .commit();
        }

        //keep title in sync with back stack.
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

    //called from HelpcenterHomeFragment
    public void openTutorial() {
        loadFragment(new TutorialFragment(), "Navigation Tutorial");
    }

    public void openFood() {
        loadFragment(new RestaurantFragment(), "Eat");
    }

    public void openHotel() {
        loadFragment(new HotelFragment(), "Stay");
    }

    public void openRoomkey() {
        loadFragment(new RoomkeyFragment(), "Room Key");
    }

    public void openSupport() {
        loadFragment(new SupportFragment(), "Customer Support");
    }

    public void openInternet() {
        loadFragment(new InternetFragment(), "WiFi and Internet");
    }

    public void openUpdates() {
        loadFragment(new UpdatesFragment(), "Updates");
    }

    public void comingSoon() {
        loadFragment(new ComingsoonFragment(), "Coming Soon");
    }

    public void termsConditions() {
        loadFragment(new TcFragment(), "Terms and Conditions");
    }

    public void sendFeedback() {
        loadFragment(new FeedbackFragment(), "Send Feedback");
    }

    /*e.g...
        public void openUpdates() {
        loadFragment(GenericLayoutFragment.newInstance(
                R.layout.fragment_updates_settings,
                "Updates",
                "updates_settings"
            ),
            "Updates"
        );
    }

    public void openApps() {
        loadFragment(GenericLayoutFragment.newInstance(
                R.layout.fragment_apps_settings,
                "Apps and Services",
                "apps_settings"
            ),
                "Apps and Services"
        );
    } etc etc etc
     */

    @Override
    public boolean onSupportNavigateUp() {
        if (getSupportFragmentManager().popBackStackImmediate()) {
            return true;
        }
        finish();
        return true;
    }
}
