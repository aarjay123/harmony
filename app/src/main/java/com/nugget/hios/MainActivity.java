package com.nugget.hios;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigationrail.NavigationRailView;
import com.nugget.hios.databinding.ActivityMainBinding;

import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback;
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.DialogPropertiesPendulum;
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.NoInternetDialogPendulum;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ActivityMainBinding binding;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateNavigationMode();

        progressBar = findViewById(R.id.activity_progress_bar);

        // No Internet Dialog: Pendulum
        NoInternetDialogPendulum.Builder builder = new NoInternetDialogPendulum.Builder(
                this,
                getLifecycle()
        );

        DialogPropertiesPendulum properties = builder.getDialogProperties();

        properties.setConnectionCallback(new ConnectionCallback() { // Optional
            @Override
            public void hasActiveConnection(boolean hasActiveConnection) {
                // ...
            }
        });

        properties.setCancelable(false); // Optional
        properties.setNoInternetConnectionTitle("No Internet"); // Optional
        properties.setNoInternetConnectionMessage("Check your network connection and try again"); // Optional
        properties.setShowInternetOnButtons(true); // Optional
        properties.setPleaseTurnOnText("Please switch on"); // Optional
        properties.setWifiOnButtonText("WiFi"); // Optional
        properties.setMobileDataOnButtonText("Mobile Data"); // Optional

        properties.setOnAirplaneModeTitle("No Internet"); // Optional
        properties.setOnAirplaneModeMessage("Aeroplane mode is switched on"); // Optional
        properties.setPleaseTurnOffText("Please switch off"); // Optional
        properties.setAirplaneModeOffButtonText("Aeroplane mode"); // Optional
        properties.setShowAirplaneModeOffButtons(true); // Optional

        builder.build();

        //setting the system navbar colour to be the same as the bottom nav bar
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true);
        int windowBackgroundColor = typedValue.data;

        getWindow().setNavigationBarColor(windowBackgroundColor);

        //Register preference change listener
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("nav_mode")) {
            updateNavigationMode();
        }
    }

    private void updateNavigationMode() {
        String navigationMode = PreferenceManager.getDefaultSharedPreferences(this).getString("nav_mode", "auto");

        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (navigationMode.equals("auto")) {
            if (isTablet || isLandscape) {
                setContentView(R.layout.activity_main_rail);

                MaterialToolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                toolbar.setElevation(0);

                //setting the colour of the toolbar to be the same as the colour of the statusbar
                int statusBarColour = getWindow().getStatusBarColor();
                toolbar.setBackgroundColor(statusBarColour);

                //Initialise NavigationRailView
                NavigationRailView navigationRailView = findViewById(R.id.navigation_rail);

                //Passing each menu ID as a set of Ids because each
                //menu should be considered as top level destinations.
                AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.navigation_home,
                        R.id.navigation_dashboard,
                        R.id.navigation_notifications,
                        R.id.navigation_settings
                ).build();

                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
                NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

                //Setup navigation with NavigationRailView
                NavigationUI.setupWithNavController(navigationRailView, navController);
            } else {
                binding = ActivityMainBinding.inflate(getLayoutInflater());
                setContentView(binding.getRoot());

                MaterialToolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                toolbar.setElevation(0);

                //setting the colour of the toolbar to be the same as the colour of the statusbar
                int statusBarColour = getWindow().getStatusBarColor();
                toolbar.setBackgroundColor(statusBarColour);

                //Initialise BottomNavigationView
                BottomNavigationView navView = findViewById(R.id.nav_view);

                //Passing each menu ID as a set of Ids because each
                //menu should be considered as top level destinations.
                AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.navigation_home,
                        R.id.navigation_dashboard,
                        R.id.navigation_notifications,
                        R.id.navigation_settings
                ).build();

                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
                NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

                //Setup navigation with BottomNavigationView
                NavigationUI.setupWithNavController(binding.navView, navController);
            }
        } else if (navigationMode.equals("bottom")) {
            setContentView(R.layout.activity_main);

            MaterialToolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            toolbar.setElevation(0);

            //setting the colour of the toolbar to be the same as the colour of the statusbar
            int statusBarColour = getWindow().getStatusBarColor();
            toolbar.setBackgroundColor(statusBarColour);

            //Initialise BottomNavigationView
            BottomNavigationView navView = findViewById(R.id.nav_view);

            //Passing each menu ID as a set of Ids because each
            //menu should be considered as top level destinations.
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home,
                    R.id.navigation_dashboard,
                    R.id.navigation_notifications,
                    R.id.navigation_settings
            ).build();

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

            //Setup navigation with BottomNavigationView
            NavigationUI.setupWithNavController(binding.navView, navController);
        } else if (navigationMode.equals("rail")) {
            setContentView(R.layout.activity_main_rail);

            MaterialToolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            toolbar.setElevation(0);

            //setting the colour of the toolbar to be the same as the colour of the statusbar
            int statusBarColour = getWindow().getStatusBarColor();
            toolbar.setBackgroundColor(statusBarColour);

            //Initialise NavigationRailView
            NavigationRailView navigationRailView = findViewById(R.id.navigation_rail);

            //Passing each menu ID as a set of Ids because each
            //menu should be considered as top level destinations.
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home,
                    R.id.navigation_dashboard,
                    R.id.navigation_notifications,
                    R.id.navigation_settings
            ).build();

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

            //Setup navigation with NavigationRailView
            NavigationUI.setupWithNavController(navigationRailView, navController);
        }
    }

    //SHOW TOOLBAR THREE DOT ICON
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbaricon, menu);

        MenuCompat.setGroupDividerEnabled(menu, true);

        return super.onCreateOptionsMenu(menu);
    }

    //ONCLICK LISTENERS GOING TO PAGES ON TOOLBAR POPUP
    public boolean downloadmenus(MenuItem item) {
        Uri uri = Uri.parse("https://www.dropbox.com/scl/fo/7gmlnnjcau1np91ee83ht/h?rlkey=ifj506k3aal7ko7tfecy8oqyq&dl=0");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        return true;
    }

    public boolean settings(MenuItem item) {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        return true;
    }

    public boolean help(MenuItem item) {
        startActivity(new Intent(MainActivity.this, HelpcenterActivity.class));
        return true;
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    public void setTheProgress(int progress) {
        progressBar.setProgress(progress);
    }
}