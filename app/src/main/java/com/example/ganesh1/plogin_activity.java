package com.example.ganesh1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class plogin_activity extends AppCompatActivity {

    CountryCodePicker ccp;
    EditText edtMobile;
    Button btnSendOtp;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    String verificationId;
    String fullNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plogin);

        ccp = findViewById(R.id.ccp);
        edtMobile = findViewById(R.id.edtMobile);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        btnSendOtp.setOnClickListener(view -> {

            // ✅ Button direct open activity_otp_verify


            String mobile = edtMobile.getText().toString().trim();

            if (mobile.isEmpty() || mobile.length() < 10) {
                edtMobile.setError("Enter valid number");
                edtMobile.requestFocus();
                return;
            }

            fullNumber = "+" + ccp.getSelectedCountryCode() + mobile;

            sendOTP(fullNumber);
        });
    }

    private void sendOTP(String phoneNumber) {

        progressBar.setVisibility(View.VISIBLE);
        btnSendOtp.setEnabled(false);

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(callbacks)
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                    Toast.makeText(plogin_activity.this, "Auto Verified", Toast.LENGTH_SHORT).show();
                    signInWithCredential(credential);
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    progressBar.setVisibility(View.GONE);
                    btnSendOtp.setEnabled(true);
                    Toast.makeText(plogin_activity.this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCodeSent(@NonNull String vId,
                                       @NonNull PhoneAuthProvider.ForceResendingToken token) {

                    progressBar.setVisibility(View.GONE);
                    btnSendOtp.setEnabled(true);

                    verificationId = vId;

                    Toast.makeText(plogin_activity.this, "OTP Sent", Toast.LENGTH_SHORT).show();

                    // ✅ Send to OTP verify page with data
                    Intent intent = new Intent(plogin_activity.this, activity_otp_verify.class);
                    intent.putExtra("verificationId", verificationId);
                    intent.putExtra("phoneNumber", fullNumber);
                    startActivity(intent);
                }
            };

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);

                    if (task.isSuccessful()) {
                        Toast.makeText(plogin_activity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                        // ✅ After auto-verification go to Home OR OTP page
                        Intent intent = new Intent(plogin_activity.this, activity_otp_verify.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    } else {
                        Toast.makeText(plogin_activity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
