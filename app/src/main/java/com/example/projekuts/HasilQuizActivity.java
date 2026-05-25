package com.example.projekuts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HasilQuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil_quiz);

        int    nilai     = getIntent().getIntExtra("NILAI", 0);
        int    benar     = getIntent().getIntExtra("BENAR", 0);
        int    total     = getIntent().getIntExtra("TOTAL", 5);
        String kategori  = getIntent().getStringExtra("KATEGORI");
        if (kategori == null) kategori = "-";

        // Views
        TextView tvEmoji      = findViewById(R.id.tvHasilEmoji);
        TextView tvJudul      = findViewById(R.id.tvHasilJudul);
        TextView tvNilai      = findViewById(R.id.tvNilai);
        TextView tvBenarSalah = findViewById(R.id.tvBenarSalah);
        TextView tvFeedback   = findViewById(R.id.tvFeedback);
        TextView tvKategori   = findViewById(R.id.tvHasilKategori);
        TextView tvRekap      = findViewById(R.id.tvRekap);
        Button   btnUlang     = findViewById(R.id.btnUlangQuiz);
        Button   btnHome      = findViewById(R.id.btnKeHome);

        // Hitung nilai kategori sebelumnya (untuk rekap)
        SharedPreferences prefs = getSharedPreferences(DashboardActivity.getPrefName(this), Context.MODE_PRIVATE);
        int skorCoding = prefs.getInt(QuizActivity.KEY_SKOR_PREFIX + "Coding", -1);
        int skorUIUX   = prefs.getInt(QuizActivity.KEY_SKOR_PREFIX + "UIUX",   -1);
        int skorDesign = prefs.getInt(QuizActivity.KEY_SKOR_PREFIX + "Design",  -1);
        int totalQuiz  = prefs.getInt(QuizActivity.KEY_TOTAL_QUIZ, 0);

        // Set nilai & info
        tvNilai.setText(String.valueOf(nilai));
        tvBenarSalah.setText(benar + " benar dari " + total + " soal");
        tvKategori.setText("Quiz " + kategori);

        // Feedback berdasarkan nilai
        String emoji, judul, feedback;
        int    warnaCard;
        if (nilai >= 80) {
            emoji     = "🎉";
            judul     = "Luar Biasa!";
            feedback  = "Kamu menguasai materi ini dengan sangat baik. Pertahankan semangat belajarmu!";
            warnaCard = Color.parseColor("#E8F5E9"); // hijau muda
            tvNilai.setTextColor(Color.parseColor("#2E7D32"));
        } else if (nilai >= 60) {
            emoji     = "👍";
            judul     = "Bagus!";
            feedback  = "Pemahaman kamu cukup baik. Coba ulang untuk memperkuat materi yang belum dikuasai.";
            warnaCard = Color.parseColor("#FFF8E1"); // kuning muda
            tvNilai.setTextColor(Color.parseColor("#F57F17"));
        } else if (nilai >= 40) {
            emoji     = "📖";
            judul     = "Perlu Belajar Lagi";
            feedback  = "Masih ada beberapa bagian yang perlu dipelajari ulang. Baca kembali materinya ya!";
            warnaCard = Color.parseColor("#FFF3E0"); // oranye muda
            tvNilai.setTextColor(Color.parseColor("#E65100"));
        } else {
            emoji     = "💪";
            judul     = "Jangan Menyerah!";
            feedback  = "Jangan sedih! Coba baca materinya sekali lagi dan kerjakan ulang quiznya.";
            warnaCard = Color.parseColor("#FFEBEE"); // merah muda
            tvNilai.setTextColor(Color.parseColor("#C62828"));
        }

        tvEmoji.setText(emoji);
        tvJudul.setText(judul);
        tvFeedback.setText(feedback);
        findViewById(R.id.cardNilai).setBackgroundColor(warnaCard);

        // Rekap semua nilai tersimpan
        StringBuilder rekap = new StringBuilder("📊 Rekap Nilai Kamu:\n");
        rekap.append(skorCoding >= 0 ? "  • Coding      : " + skorCoding + "/100\n" : "  • Coding      : Belum dikerjakan\n");
        rekap.append(skorUIUX   >= 0 ? "  • UI/UX       : " + skorUIUX   + "/100\n" : "  • UI/UX       : Belum dikerjakan\n");
        rekap.append(skorDesign >= 0 ? "  • Design      : " + skorDesign + "/100\n" : "  • Design      : Belum dikerjakan\n");
        rekap.append("\nTotal quiz diselesaikan: " + totalQuiz + "x");
        tvRekap.setText(rekap.toString());

        // Tombol aksi
        String finalKategori = kategori;
        btnUlang.setOnClickListener(v -> {
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra("KATEGORI_QUIZ", finalKategori);
            NavHelper.navigateForward(this, intent);
            finish();
        });

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        // Dari hasil quiz, back → ke Dashboard (bukan ke soal quiz)
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}
