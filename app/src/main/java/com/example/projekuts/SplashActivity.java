package com.example.projekuts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            // Cek sesi login langsung dari Firebase (bukan SharedPreferences)
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser != null) {
                // Pastikan data sesi lokal tersedia
                String nama  = currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "Pelajar";
                String email = currentUser.getEmail() != null ? currentUser.getEmail() : "";

                getSharedPreferences("Session", Context.MODE_PRIVATE)
                        .edit()
                        .putBoolean("isLoggedIn", true)
                        .putString("USER_EMAIL", email)
                        .putString("USER_NAME", nama)
                        .apply();

                getSharedPreferences(DashboardActivity.getPrefName(this), Context.MODE_PRIVATE)
                        .edit()
                        .putString(DashboardActivity.KEY_USER_NAME, nama)
                        .putString("USER_EMAIL", email)
                        .apply();

                startActivity(new Intent(this, DashboardActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }

            finish();
        }, 2500);
    }
}
