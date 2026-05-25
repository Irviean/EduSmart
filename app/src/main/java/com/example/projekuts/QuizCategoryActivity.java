package com.example.projekuts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class QuizCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_category);

        NavHelper.setup(this, NavHelper.TAB_QUIZ);

        CardView btnCatCoding = findViewById(R.id.btnCatCoding);
        CardView btnCatUIUX   = findViewById(R.id.btnCatUIUX);
        CardView btnCatDesign = findViewById(R.id.btnCatDesign);

        btnCatCoding.setOnClickListener(v -> cekDanBukaQuiz("Coding",  "Materi 1: Dasar Android & Java"));
        btnCatUIUX.setOnClickListener(v   -> cekDanBukaQuiz("UIUX",    "Materi 2: Pengenalan UI / UX"));
        btnCatDesign.setOnClickListener(v -> cekDanBukaQuiz("Design",  "Materi 3: Dasar Desain Grafis"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshSkorLabels();
    }

    // ── Tampilkan skor + status lock di tiap card ────────────────────────────

    private void refreshSkorLabels() {
        SharedPreferences prefs = getSharedPreferences(DashboardActivity.getPrefName(this), Context.MODE_PRIVATE);

        updateLabel(prefs, R.id.tvSkorCoding, "Coding",  "#007BFF");
        updateLabel(prefs, R.id.tvSkorUIUX,   "UIUX",    "#9C27B0");
        updateLabel(prefs, R.id.tvSkorDesign, "Design",  "#FF9800");
    }

    private void updateLabel(SharedPreferences prefs, int tvId, String kategori, String warnaSkor) {
        TextView tv = findViewById(tvId);
        if (tv == null) return;

        boolean sudahBukaMateri = prefs.getBoolean(
                DetailMateriActivity.PREFIX_MATERI_DIBUKA + kategori, false);
        int skor = prefs.getInt(QuizActivity.KEY_SKOR_PREFIX + kategori, -1);
        String tanggal = prefs.getString(QuizActivity.KEY_TANGGAL_PREFIX + kategori, "");

        if (!sudahBukaMateri) {
            tv.setText("🔒 Buka materi dulu");
            tv.setTextColor(android.graphics.Color.parseColor("#9E9E9E"));
        } else if (skor >= 0) {
            String teks = "Nilai terbaik: " + skor + "/100";
            if (!tanggal.isEmpty()) teks += " · " + tanggal;
            tv.setText(teks);
            tv.setTextColor(android.graphics.Color.parseColor(warnaSkor));
        } else {
            tv.setText("Belum dikerjakan · Mulai sekarang!");
            tv.setTextColor(android.graphics.Color.parseColor("#9E9E9E"));
        }
    }

    // ── Cek unlock & buka quiz ───────────────────────────────────────────────

    private void cekDanBukaQuiz(String kategori, String namaMateri) {
        SharedPreferences prefs = getSharedPreferences(DashboardActivity.getPrefName(this), Context.MODE_PRIVATE);
        boolean sudahBuka = prefs.getBoolean(
                DetailMateriActivity.PREFIX_MATERI_DIBUKA + kategori, false);

        if (sudahBuka) {
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra("KATEGORI_QUIZ", kategori);
            NavHelper.navigateForward(this, intent);
        } else {
            Toast.makeText(this, "Baca " + namaMateri + " dulu ya! 📚", Toast.LENGTH_LONG).show();
            NavHelper.navigate(this, MateriActivity.class, false);
        }
    }

    @Override
    public void onBackPressed() {
        NavHelper.navigateBack(this);
    }
}
