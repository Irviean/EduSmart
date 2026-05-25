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
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNama, etEmail, etPassword, etKonfirmasi;
    private Button btnDaftar;
    private TextView tvKeLogin;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth        = FirebaseAuth.getInstance();
        etNama       = findViewById(R.id.etNama);
        etEmail      = findViewById(R.id.etEmailReg);
        etPassword   = findViewById(R.id.etPasswordReg);
        etKonfirmasi = findViewById(R.id.etKonfirmasi);
        btnDaftar    = findViewById(R.id.btnDaftar);
        tvKeLogin    = findViewById(R.id.tvKeLogin);
        progressBar  = findViewById(R.id.progressBar); // tambahkan di layout jika belum ada

        btnDaftar.setOnClickListener(v -> prosesRegister());
        tvKeLogin.setOnClickListener(v -> finish());
    }

    private void prosesRegister() {
        String nama       = etNama.getText().toString().trim();
        String email      = etEmail.getText().toString().trim();
        String password   = etPassword.getText().toString().trim();
        String konfirmasi = etKonfirmasi.getText().toString().trim();

        // Validasi
        if (TextUtils.isEmpty(nama)) {
            etNama.setError("Nama tidak boleh kosong");
            etNama.requestFocus(); return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email tidak boleh kosong");
            etEmail.requestFocus(); return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Format email tidak valid");
            etEmail.requestFocus(); return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password tidak boleh kosong");
            etPassword.requestFocus(); return;
        }
        if (password.length() < 6) {
            etPassword.setError("Password minimal 6 karakter");
            etPassword.requestFocus(); return;
        }
        if (!password.equals(konfirmasi)) {
            etKonfirmasi.setError("Konfirmasi password tidak cocok");
            etKonfirmasi.requestFocus(); return;
        }

        setLoading(true);

        // Daftarkan ke Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Simpan displayName (nama) ke profil Firebase
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(nama)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(profileTask -> {
                                        setLoading(false);
                                        // Simpan sesi lokal
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

                                        Toast.makeText(this, "Akun berhasil dibuat!", Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(this, DashboardActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        finish();
                                    });
                        }
                    } else {
                        setLoading(false);
                        String errMsg = task.getException() != null
                                ? task.getException().getMessage()
                                : "Registrasi gagal";
                        Toast.makeText(this, errMsg, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setLoading(boolean loading) {
        if (progressBar != null)
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnDaftar.setEnabled(!loading);
    }
}
