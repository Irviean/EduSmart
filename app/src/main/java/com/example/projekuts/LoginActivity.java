package com.example.projekuts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvKeRegister;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth        = FirebaseAuth.getInstance();
        etEmail      = findViewById(R.id.etEmail);
        etPassword   = findViewById(R.id.etPassword);
        btnLogin     = findViewById(R.id.btnLogin);
        tvKeRegister = findViewById(R.id.tvKeRegister);
        progressBar  = findViewById(R.id.progressBar); // tambahkan di layout jika belum ada

        btnLogin.setOnClickListener(v -> prosesLogin());
        tvKeRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Jika sudah login sebelumnya, langsung ke Dashboard
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            simpanSesiDanLanjut(currentUser);
        }
    }

    private void prosesLogin() {
        String email    = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email tidak boleh kosong");
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password tidak boleh kosong");
            etPassword.requestFocus();
            return;
        }

        setLoading(true);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    setLoading(false);
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            simpanSesiDanLanjut(user);
                        }
                    } else {
                        Toast.makeText(this, "Login gagal: Email atau password salah!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void simpanSesiDanLanjut(FirebaseUser user) {
        // Simpan email & displayName ke Session prefs
        String nama  = user.getDisplayName() != null ? user.getDisplayName() : "Pelajar";
        String email = user.getEmail() != null ? user.getEmail() : "";

        getSharedPreferences("Session", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("isLoggedIn", true)
                .putString("USER_EMAIL", email)
                .putString("USER_NAME", nama)
                .apply();

        // Simpan juga ke prefs per-user (untuk Dashboard)
        getSharedPreferences(DashboardActivity.getPrefName(this), Context.MODE_PRIVATE)
                .edit()
                .putString(DashboardActivity.KEY_USER_NAME, nama)
                .putString("USER_EMAIL", email)
                .apply();

        Intent i = new Intent(this, DashboardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    private void setLoading(boolean loading) {
        if (progressBar != null)
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!loading);
    }
}
