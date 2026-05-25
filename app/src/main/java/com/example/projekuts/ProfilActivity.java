package com.example.projekuts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        NavHelper.setup(this, NavHelper.TAB_PROFIL);

        EditText etNama    = findViewById(R.id.etNamaEdit);
        EditText etNim     = findViewById(R.id.etNimEdit);
        Button   btnSimpan = findViewById(R.id.btnSimpan);
        Button   btnLogout = findViewById(R.id.btnLogout);

        SharedPreferences prefs = getSharedPreferences(DashboardActivity.getPrefName(this), Context.MODE_PRIVATE);

        etNama.setText(prefs.getString(DashboardActivity.KEY_USER_NAME, ""));
        etNim.setText(prefs.getString("USER_NIM", ""));

        loadStats(prefs);

        // Simpan perubahan nama
        btnSimpan.setOnClickListener(v -> {
            String nama = etNama.getText().toString().trim();
            String nim  = etNim.getText().toString().trim();

            if (nama.isEmpty()) {
                etNama.setError("Nama tidak boleh kosong");
                etNama.requestFocus();
                return;
            }

            // Update displayName di Firebase
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(nama)
                        .build();
                user.updateProfile(profileUpdates);
            }

            // Update lokal
            prefs.edit()
                    .putString(DashboardActivity.KEY_USER_NAME, nama)
                    .putString("USER_NIM", nim)
                    .apply();

            getSharedPreferences("Session", Context.MODE_PRIVATE)
                    .edit()
                    .putString("USER_NAME", nama)
                    .apply();

            loadStats(prefs);
            Toast.makeText(this, "Profil berhasil disimpan! ✓", Toast.LENGTH_SHORT).show();
        });

        // Logout dari Firebase
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();

            // Hapus sesi lokal
            getSharedPreferences("Session", Context.MODE_PRIVATE)
                    .edit().clear().apply();

            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences(DashboardActivity.getPrefName(this), Context.MODE_PRIVATE);
        loadStats(prefs);
    }

    private void loadStats(SharedPreferences prefs) {
        String nama  = prefs.getString(DashboardActivity.KEY_USER_NAME, "Pelajar");
        String email = prefs.getString("USER_EMAIL", "-");
        int streak   = prefs.getInt("STREAK_COUNT", 0);
        int totalQuiz = prefs.getInt(QuizActivity.KEY_TOTAL_QUIZ, 0);

        int skorCoding = prefs.getInt(QuizActivity.KEY_SKOR_PREFIX + "Coding", -1);
        int skorUIUX   = prefs.getInt(QuizActivity.KEY_SKOR_PREFIX + "UIUX",   -1);
        int skorDesign = prefs.getInt(QuizActivity.KEY_SKOR_PREFIX + "Design",  -1);
        int nilaiBest  = Math.max(skorCoding, Math.max(skorUIUX, skorDesign));

        TextView tvAvatar = findViewById(R.id.tvAvatarProfil);
        if (tvAvatar != null) {
            String[] parts = nama.trim().split("\\s+");
            String inisial = parts.length >= 2
                    ? String.valueOf(parts[0].charAt(0)) + String.valueOf(parts[1].charAt(0))
                    : nama.substring(0, Math.min(2, nama.length()));
            tvAvatar.setText(inisial.toUpperCase());
        }

        TextView tvNama = findViewById(R.id.tvNamaProfil);
        if (tvNama != null) tvNama.setText(nama);

        TextView tvEmail = findViewById(R.id.tvEmailProfil);
        if (tvEmail != null) tvEmail.setText(email);

        TextView tvEmailReadonly = findViewById(R.id.tvEmailReadonly);
        if (tvEmailReadonly != null) tvEmailReadonly.setText(email);

        TextView tvNilai = findViewById(R.id.tvProfilNilai);
        if (tvNilai != null) tvNilai.setText(nilaiBest >= 0 ? String.valueOf(nilaiBest) : "-");

        TextView tvQuiz = findViewById(R.id.tvProfilQuiz);
        if (tvQuiz != null) tvQuiz.setText(String.valueOf(totalQuiz));

        TextView tvStreak = findViewById(R.id.tvProfilStreak);
        if (tvStreak != null) tvStreak.setText(streak + "🔥");
    }

    @Override
    public void onBackPressed() {
        NavHelper.navigateBack(this);
    }
}
