package com.example.ganesh1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class activity_home extends AppCompatActivity {

    DrawerLayout drawer;

    ImageView btnMenu,headerProfile;

    // ✅ Content Screens
    LinearLayout contentHome, contentMap, contentSMS, contentSettings;

    // ✅ Footer Navigation
    LinearLayout navHome, navMap, navSMS, navSettings;

    LinearLayout leftMenu;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Drawer
        drawer = findViewById(R.id.drawerLayout);
        leftMenu = findViewById(R.id.leftMenu);
        headerProfile=findViewById(R.id.headerProfile);

        btnMenu = findViewById(R.id.btnMenu);

        // ✅ Get Content Layouts
        contentHome     = findViewById(R.id.contentHome);
        contentMap      = findViewById(R.id.contentmap);     // ✅ FIXED
        contentSMS      = findViewById(R.id.contentSMS);
        contentSettings = findViewById(R.id.contentSettings);

        // ✅ Footer Navigation Buttons
        navHome     = findViewById(R.id.nav_home);
        navMap      = findViewById(R.id.nav_map);
        navSMS      = findViewById(R.id.nav_sms);
        navSettings = findViewById(R.id.nav_settings);

        // ✅ Open Drawer (LEFT)
        btnMenu.setOnClickListener(v -> drawer.openDrawer(Gravity.LEFT));
        headerProfile.setOnClickListener(v -> {

        Intent intent = new Intent(activity_home.this, search_user.class);
        startActivity(intent); });

        // ✅ Footer Navigation Clicks
        navHome.setOnClickListener(v -> showScreen(1));
        navMap.setOnClickListener(v -> showScreen(2));
        navSMS.setOnClickListener(v -> showScreen(3));
        navSettings.setOnClickListener(v -> showScreen(4));

        // ✅ Default Screen
        showScreen(1);
    }

    private void showScreen(int id) {

        // ✅ Hide all content
        contentHome.setVisibility(View.GONE);
        contentMap.setVisibility(View.GONE);
        contentSMS.setVisibility(View.GONE);
        contentSettings.setVisibility(View.GONE);

        // ✅ Show selected screen
        switch (id) {
            case 1:
                contentHome.setVisibility(View.VISIBLE);
                break;

            case 2:
                contentMap.setVisibility(View.VISIBLE);
                break;

            case 3:
                contentSMS.setVisibility(View.VISIBLE);
                break;

            case 4:
                contentSettings.setVisibility(View.VISIBLE);
                break;
        }

        // ✅ Close Drawer if open
        drawer.closeDrawer(Gravity.LEFT);
    }
}
