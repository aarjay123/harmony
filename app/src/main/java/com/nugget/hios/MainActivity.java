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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.MenuCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigationrail.NavigationRailView;
import com.nugget.hios.databinding.ActivityMainBinding;

import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback;
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.DialogPropertiesPendulum;
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.NoInternetDialogPendulum;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ActivityMainBinding binding;
    private ProgressBar progressBar;

    /*private BroadcastReceiver widgetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case .ACTION_HOME: {

                    }
                        break;
                    }
                    case WidgetProvider.ACTION_RESTAURANT: {
                }
            }
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateNavigationMode();

        // Note: ProgressBar was removed from activity_main.xml (Phone Layout),
        // so this might be null in phone mode. It should be handled with null checks.
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
        properties.setPleaseTurnOnText("Please turn on"); // Optional
        properties.setWifiOnButtonText("Wi-Fi"); // Optional
        properties.setMobileDataOnButtonText("Mobile data"); // Optional

        properties.setOnAirplaneModeTitle("No Internet"); // Optional
        properties.setOnAirplaneModeMessage("Aeroplane mode is switched on"); // Optional
        properties.setPleaseTurnOffText("Please turn off"); // Optional
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
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (isTablet) {
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
            WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            // --- Updated for FAB Menu Logic (No Toolbar) ---

            // Set up Floating Action Button for the Menu
            FloatingActionButton fab = findViewById(R.id.fab_menu);
            if (fab != null) {
                fab.setOnClickListener(this::showFabMenu);
            }

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            // No ActionBar setup needed since we removed the Toolbar

            //Setup navigation with BottomNavigationView
            NavigationUI.setupWithNavController(binding.navView, navController);

            //insets
            final BottomNavigationView bottomNav = binding.navView;
            final View root = binding.getRoot();

            // Save the original padding + bottom margin from XML
            final int pL = bottomNav.getPaddingLeft();
            final int pT = bottomNav.getPaddingTop();
            final int pR = bottomNav.getPaddingRight();
            final int pB = bottomNav.getPaddingBottom();

            final ViewGroup.MarginLayoutParams lp =
                    (ViewGroup.MarginLayoutParams) bottomNav.getLayoutParams();
            final int baseBottomMargin = lp.bottomMargin;

            // 1) Override BottomNavigationView's own inset handling (prevents it adding padding)
            ViewCompat.setOnApplyWindowInsetsListener(bottomNav, (v, insets) -> {
                // keep your original padding so items stay centered
                v.setPadding(pL, pT, pR, pB);
                return insets; // (we’re just preventing padding changes)
            });

            // 2) Apply nav bar inset as extra *margin* so the whole pill lifts above the system bar
            ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
                Insets nav = insets.getInsets(WindowInsetsCompat.Type.navigationBars());
                Insets gestures = insets.getInsets(WindowInsetsCompat.Type.systemGestures());
                int bottomInset = Math.max(nav.bottom, gestures.bottom);

                ViewGroup.MarginLayoutParams params =
                        (ViewGroup.MarginLayoutParams) bottomNav.getLayoutParams();
                params.bottomMargin = baseBottomMargin + bottomInset;
                bottomNav.setLayoutParams(params);

                return insets;
            });

            ViewCompat.requestApplyInsets(root);
        }
    }

    private void showFabMenu(View v) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.layout_menu_bottom_sheet, null);
        bottomSheetDialog.setContentView(sheetView);

        sheetView.findViewById(R.id.topmenuSettings).setOnClickListener(view -> {
            settings(null);
            bottomSheetDialog.dismiss();
        });

        sheetView.findViewById(R.id.topmenuHelp).setOnClickListener(view -> {
            help(null);
            bottomSheetDialog.dismiss();
        });

        sheetView.findViewById(R.id.topmenuBlog).setOnClickListener(view -> {
            visitBlog(null);
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

    //SHOW TOOLBAR THREE DOT ICON (Still used for Tablet/Rail mode)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbaricon, menu);

        MenuCompat.setGroupDividerEnabled(menu, true);

        return super.onCreateOptionsMenu(menu);
    }

    //ONCLICK LISTENERS GOING TO PAGES ON TOOLBAR/POPUP


    public boolean settings(MenuItem item) {
        startActivity(new Intent(MainActivity.this, HiClubSettingsActivity.class));
        return true;
    }

    public boolean help(MenuItem item) {
        startActivity(new Intent(MainActivity.this, HelpcenterActivity.class));
        return true;
    }

    public boolean visitBlog(MenuItem item) {
        Uri uri = Uri.parse("https://hienterprises.blogspot.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        return true;
    }

    public void showProgressBar() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }

    public void setTheProgress(int progress) {
        if (progressBar != null) progressBar.setProgress(progress);
    }
}