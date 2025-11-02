package com.example.ganesh1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class activity_otp_verify extends AppCompatActivity {

    EditText loginOtp;
    Button loginNextBtn;
    ProgressBar loginProgressBar;
    TextView resendOtpTextview;

    String verificationId;
    FirebaseAuth mAuth;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);

        loginOtp = findViewById(R.id.login_otp);
        loginNextBtn = findViewById(R.id.login_next_btn);
        loginProgressBar = findViewById(R.id.login_progress_bar);
        resendOtpTextview = findViewById(R.id.resend_otp_textview);

        mAuth = FirebaseAuth.getInstance();

        // Get verificationId and phone number from previous activity
        verificationId = getIntent().getStringExtra("verificationId");
        phoneNumber = getIntent().getStringExtra("phoneNumber");

        loginProgressBar.setVisibility(View.GONE);

        // Resend OTP countdown
        startResendTimer();

        resendOtpTextview.setOnClickListener(v -> {
            resendVerificationCode(phoneNumber);
        });

        loginNextBtn.setOnClickListener(v -> {
            String code = loginOtp.getText().toString().trim();

            if (code.isEmpty() || code.length() < 6) {
                loginOtp.setError("Enter valid 6-digit OTP");
                return;
            }

            loginProgressBar.setVisibility(View.VISIBLE);
            verifyCode(code);
        });
    }

    private void verifyCode(String code) {
        if (verificationId == null) {
            Toast.makeText(this, "Verification ID not found. Please resend OTP.", Toast.LENGTH_SHORT).show();
            loginProgressBar.setVisibility(View.GONE);
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    loginProgressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(activity_otp_verify.this, "Verification Successful", Toast.LENGTH_SHORT).show();

                        // Go to HomeActivity after success
                        Intent intent = new Intent(activity_otp_verify.this,username_activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(activity_otp_verify.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void resendVerificationCode(String phoneNumber) {
        loginProgressBar.setVisibility(View.VISIBLE);

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        signInWithCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        loginProgressBar.setVisibility(View.GONE);
                        Toast.makeText(activity_otp_verify.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String newVerificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        loginProgressBar.setVisibility(View.GONE);
                        Toast.makeText(activity_otp_verify.this, "OTP resent successfully", Toast.LENGTH_SHORT).show();
                        verificationId = newVerificationId;
                        startResendTimer();
                    }
                })
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void startResendTimer() {
        resendOtpTextview.setEnabled(false);
        new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                resendOtpTextview.setText("Resend OTP in " + (millisUntilFinished / 1000) + " sec");
            }

            @Override
            public void onFinish() {
                resendOtpTextview.setText("Resend OTP");
                resendOtpTextview.setEnabled(true);
            }
        }.start();
    }
}
