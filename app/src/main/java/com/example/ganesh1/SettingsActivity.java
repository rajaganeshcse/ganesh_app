package com.example.ganesh1;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

    ImageButton profileImage;
    EditText etFullName, etEmail, etPhone;
    Button btnSave, btnLogout, btnContact, btnHome, btnMessage, btnMap;
    SharedPreferences sharedPreferences;
    Bitmap selectedBitmap = null;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // ✅ Set title and back button in ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Settings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // ✅ Initialize all views
        profileImage = findViewById(R.id.profileImage);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        btnSave = findViewById(R.id.btnSave);
        btnLogout = findViewById(R.id.btnLogout);
        btnContact = findViewById(R.id.btnContact);
        btnHome = findViewById(R.id.nav_home);
        btnMessage = findViewById(R.id.nav_map);
        btnMap = findViewById(R.id.nav_sms);

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        loadUserData();

        // ✅ Select image from gallery
        profileImage.setOnClickListener(v -> {
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickImageIntent, PICK_IMAGE);
        });

        // ✅ Save data
        btnSave.setOnClickListener(v -> saveUserData());

        // ✅ Logout
        btnLogout.setOnClickListener(v -> {
            sharedPreferences.edit().clear().apply();
            Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // ✅ Contact Us
        btnContact.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@ganesh1app.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact - Ganesh1 App");
            startActivity(Intent.createChooser(emailIntent, "Contact Us"));
        });

        // ✅ Open Home Activity
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, activity_home.class);
            startActivity(intent);
            finish();
        });

        // ✅ Open Message Activity
        btnMessage.setOnClickListener(v -> {
            Intent intent = new Intent(this, activity_home.class);
            startActivity(intent);
        });

        // ✅ Open Map Activity
        btnMap.setOnClickListener(v -> {
            Intent intent = new Intent(this, activity_home.class);
            startActivity(intent);
        });

        // ✅ Handle back press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(SettingsActivity.this, activity_home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    // ✅ Handle ActionBar back arrow click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(SettingsActivity.this, activity_home.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ✅ Handle image result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImage.setImageBitmap(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ✅ Convert Bitmap → Base64 String
    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    // ✅ Convert Base64 → Bitmap
    private Bitmap decodeBase64ToBitmap(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    // ✅ Save user info & image
    private void saveUserData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", etFullName.getText().toString());
        editor.putString("email", etEmail.getText().toString());
        editor.putString("phone", etPhone.getText().toString());

        if (selectedBitmap != null) {
            String base64Image = encodeImageToBase64(selectedBitmap);
            editor.putString("profileImage", base64Image);
        }

        editor.apply();
        Toast.makeText(this, "Details saved successfully!", Toast.LENGTH_SHORT).show();
    }

    // ✅ Load saved info
    private void loadUserData() {
        etFullName.setText(sharedPreferences.getString("name", ""));
        etEmail.setText(sharedPreferences.getString("email", ""));
        etPhone.setText(sharedPreferences.getString("phone", ""));

        String base64Image = sharedPreferences.getString("profileImage", null);
        if (base64Image != null) {
            Bitmap bitmap = decodeBase64ToBitmap(base64Image);
            profileImage.setImageBitmap(bitmap);
        }
    }
}
