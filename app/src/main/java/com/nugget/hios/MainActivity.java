package com.nugget.hios;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.nugget.hios.databinding.ActivityMainBinding;

import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback;
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.DialogPropertiesPendulum;
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.NoInternetDialogPendulum;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    //defining the icons array for the alert dialog popup
    private int[] icons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.TransparentStatusBar);

        //Initialize Firebase
        FirebaseApp.initializeApp(this);

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

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //defining the icons in the array 'icons'
        icons = new int[] {
              R.drawable.ic_menu,
              R.drawable.ic_settings,
              R.drawable.ic_moreicon,
              R.drawable.ic_help
        };

        //creating the alert dialog, with the onclicks etc.
        View moreItemView = navView.findViewById(R.id.navigation_more);
        moreItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showPopup(v);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.title_more);

                String[] moreItems = {getString(R.string.download_menus), getString(R.string.title_activity_settings), getString(R.string.title_comingsoon), getString(R.string.title_activity_help)};

                CustomAdapter adapter = new CustomAdapter(MainActivity.this, moreItems, icons);
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Uri uri = Uri.parse("https://www.dropbox.com/scl/fo/7gmlnnjcau1np91ee83ht/h?rlkey=ifj506k3aal7ko7tfecy8oqyq&dl=0");
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                                break;
                            case 1:
                                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                                break;
                            case 2:
                                startActivity(new Intent(MainActivity.this, ComingsoonActivity.class));
                                break;
                            case 3:
                                startActivity(new Intent(MainActivity.this, HelpcenterActivity.class));
                                break;
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_popup_background);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.toolbaricon_fab);

        //fab.setOnClickListener(new View.OnClickListener() {
          //  @Override
            //public void onClick(View v) {
              //  showPopup(v);
            //}
        //});
    }

    //adapter for the alert dialog that shows the icons.
    public static class CustomAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] items;
        private final int[] icons;

        public CustomAdapter(Context context, String[] items, int[] icons) {
            super(context, R.layout.dialog_item, items);
            this.context = context;
            this.items = items;
            this.icons = icons;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.dialog_item, parent, false);

            ImageView icon = rowView.findViewById(R.id.item_icon);
            TextView text = rowView.findViewById(R.id.item_text);

            icon.setImageResource(icons[position]);
            text.setText(items[position]);

            return rowView;
        }
    }

    private void showPopup(View v) {
        //PopupMenu popup = new PopupMenu(this, v);
        //MenuInflater inflater = popup.getMenuInflater();
        //inflater.inflate(R.menu.toolbaricon, popup.getMenu());

        //MenuCompat.setGroupDividerEnabled(popup.getMenu(), true);

        //popup.show();
    }

    //SHOW TOOLBAR THREE DOT ICON
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbaricon, menu);

        MenuCompat.setGroupDividerEnabled(menu, true);

        return super.onCreateOptionsMenu(menu);
    }

    //DISPLAY TOOLBARICON

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

    public boolean comingsoon(MenuItem item) {
        startActivity(new Intent(MainActivity.this, ComingsoonActivity.class));
        return true;
    }

    public boolean help(MenuItem item) {
        startActivity(new Intent(MainActivity.this, HelpcenterActivity.class));
        return true;
    }

    /*public boolean feedback(MenuItem item) {
        startActivity(new Intent(MainActivity.this, FeedbackActivity.class));
        return true;
    }*/
}