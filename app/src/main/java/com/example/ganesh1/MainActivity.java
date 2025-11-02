package com.example.ganesh1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText loginEmail, loginPassword;
    Button btnLogin;
    TextView txtGoToRegister, txtPhoneLogin;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);   // ✅ your XML file name

        // ✅ Initialize Firebase
        auth = FirebaseAuth.getInstance();

        // ✅ Initialize UI
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtGoToRegister = findViewById(R.id.txtGoToRegister);
        txtPhoneLogin = findViewById(R.id.txtPhoneLogin);

        // ✅ Login Button
        btnLogin.setOnClickListener(v -> loginUser());

        // ✅ Register Button
        txtGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,activity_register.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this, "Register Page", Toast.LENGTH_SHORT).show();
        });

        // ✅ Phone Login Button
        txtPhoneLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,plogin_activity.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this, "Phone Login Page", Toast.LENGTH_SHORT).show();
        });
    }

    // ✅ 1. REGISTER USER + SEND EMAIL VERIFY
    private void registerUser() {
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        if (email.isEmpty()) {
            loginEmail.setError("Enter Email");
            return;
        }
        if (password.isEmpty()) {
            loginPassword.setError("Enter Password");
            return;
        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser user = auth.getCurrentUser();

                        if (user != null) {
                            user.sendEmailVerification()
                                    .addOnSuccessListener(aVoid ->
                                            Toast.makeText(MainActivity.this,
                                                    "Verification Email Sent!",
                                                    Toast.LENGTH_LONG).show()
                                    )
                                    .addOnFailureListener(e ->
                                            Toast.makeText(MainActivity.this,
                                                    "Failed: " + e.getMessage(),
                                                    Toast.LENGTH_LONG).show()
                                    );
                        }

                    } else {
                        Toast.makeText(MainActivity.this,
                                "Register Error: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // ✅ 2. LOGIN USER (CHECK EMAIL VERIFIED)
    private void loginUser() {
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        if (email.isEmpty()) {
            loginEmail.setError("Enter Email");
            return;
        }
        if (password.isEmpty()) {
            loginPassword.setError("Enter Password");
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        FirebaseUser user = auth.getCurrentUser();

                        if (user != null && user.isEmailVerified()) {

                            Toast.makeText(MainActivity.this,
                                    "Login Successful!",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, activity_home.class);

                            startActivity(intent);

                            // ✅ Here you can open HomeActivity
                            // startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                        } else {

                            Toast.makeText(MainActivity.this,
                                    "Please verify your email first!",
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, activity_register.class);

                            startActivity(intent);
                        }

                    } else {
                        Toast.makeText(MainActivity.this,
                                "Login Failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                });
    }
}
