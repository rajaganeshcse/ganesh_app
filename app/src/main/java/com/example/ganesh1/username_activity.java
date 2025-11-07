package com.example.ganesh1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ganesh1.model.UserModel;
import com.example.ganesh1.utils.FirebaseUtil;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class username_activity extends AppCompatActivity {

    EditText usernameEt;
    Button btnLetMeIn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        usernameEt = findViewById(R.id.login_username);
        btnLetMeIn = findViewById(R.id.login_let_me_in_btn);
        progressBar = findViewById(R.id.login_progress_bar);

        progressBar.setVisibility(View.GONE);

        btnLetMeIn.setOnClickListener(v -> saveUsername());
    }

    private void saveUsername() {
        String username = usernameEt.getText().toString().trim();

        if (username.isEmpty() || username.length() < 3) {
            usernameEt.setError("Username must be at least 3 characters");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnLetMeIn.setEnabled(false);

        // ✅ Create a user model object
        UserModel user = new UserModel();
        user.setUserId(FirebaseUtil.currentUserId());
        user.setPhone(FirebaseUtil.currentUserPhone());
        user.setUsername(username);
        user.setProfileImage(""); // optional: can set later

        // ✅ Save to Firestore instead of Firebase DB
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(FirebaseUtil.currentUserId());

        userRef.set(user)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    btnLetMeIn.setEnabled(true);
                    Toast.makeText(this, "Username saved successfully!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(username_activity.this, activity_home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnLetMeIn.setEnabled(true);
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
