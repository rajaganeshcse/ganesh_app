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

        // ✅ CORRECT - DO NOT USE new String[]
        UserModel model = new UserModel(
                FirebaseUtil.currentUserId(),      // UID
                FirebaseUtil.currentUserPhone(),   // Phone number
                username                           // Username
        );

        FirebaseUtil.currentUserDatabaseRef()
                .setValue(model)
                .addOnCompleteListener(task -> {

                    progressBar.setVisibility(View.GONE);
                    btnLetMeIn.setEnabled(true);

                    if (task.isSuccessful()) {

                        Toast.makeText(this, "Username saved!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(username_activity.this, activity_home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    } else {
                        Toast.makeText(this,
                                "Failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
