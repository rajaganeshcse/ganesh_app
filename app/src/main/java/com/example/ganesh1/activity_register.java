package com.example.ganesh1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

public class activity_register extends AppCompatActivity {

    EditText registerEmail, registerPassword;
    Button btnRegister;
    TextView txtGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtGoToLogin = findViewById(R.id.txtGoToLogin);

        btnRegister.setOnClickListener(v -> {
            String email = registerEmail.getText().toString();
            String password = registerPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity_register.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(activity_register.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
