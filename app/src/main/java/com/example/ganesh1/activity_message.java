package com.example.ganesh1;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class activity_message extends AppCompatActivity {

    EditText etMessage;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = etMessage.getText().toString().trim();

                if (message.isEmpty()) {
                    Toast.makeText(activity_message.this, "Please type a message", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity_message.this, "Message Sent: " + message, Toast.LENGTH_SHORT).show();
                    etMessage.setText("");
                }
            }
        });
    }
}
