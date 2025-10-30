package com.example.ganesh1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.content.Intent;

public class activity_home extends AppCompatActivity {

    TextView txtWelcome;
    LinearLayout nav_home, nav_map, nav_logout, nav_settings;
    TextView headerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Header
        headerTitle = findViewById(R.id.headerTitle);

        // Body
        txtWelcome = findViewById(R.id.txtWelcome);

        // Footer
        nav_home = findViewById(R.id.nav_home);
        nav_map = findViewById(R.id.nav_map);
        nav_logout = findViewById(R.id.nav_settings);
        nav_settings = findViewById(R.id.nav_settings);

        // Set welcome message
        String email = getIntent().getStringExtra("email");
        if (email != null) {
            txtWelcome.setText("Welcome, " + email + " 👋");
        }

        // Footer navigation clicks
        nav_home.setOnClickListener(v ->
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show());

        nav_map.setOnClickListener(v ->
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show());

        // ✅ Open Settings Activity
        nav_settings.setOnClickListener(v -> {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        });

        // ✅ Logout and go to MainActivity
        nav_logout.setOnClickListener(v -> {
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
