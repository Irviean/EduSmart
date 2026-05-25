package com.example.projekuts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DashboardActivity extends AppCompatActivity {

    // PREF_NAME sekarang per-user: gunakan getPrefName(context) bukan PREF_NAME langsung
    public static final String PREF_NAME_BASE      = "AppPrefs";
    public static final String KEY_USER_NAME       = "USER_NAME";
    public static final String KEY_DEADLINE_TS     = "DEADLINE_TIMESTAMP";
    public static final String KEY_DEADLINE_MATERI = "DEADLINE_MATERI";

    // Streak keys
    private static final String KEY_STREAK_COUNT    = "STREAK_COUNT";
    private static final String KEY_STREAK_LAST     = "STREAK_LAST_DATE"; // format: yyyy-MM-dd

    // Aktivitas mingguan — simpan tanggal-tanggal aktif minggu ini
    private static final String KEY_WEEKLY_PREFIX   = "WEEKLY_";          // WEEKLY_2025-05-20 = true

    private TextView tvUser, tvAvatar, tvStreak;
    private TextView tvStatNilai, tvStatTotalQuiz;
    private ProgressBar pbCoding, pbUIUX, pbDesign;
    private TextView tvProgressCoding, tvProgressUIUX, tvProgressDesign;
    private TextView badgeQuizPertama, badgeNilaiSempurna, badgeSemuaMateri, badgeStreak;
    private TextView tvAktivitasInfo;
    private CardView cardDeadline, cardNoDeadline;
    private TextView tvDeadlineMateri, tvDeadlineWaktu, tvDeadlineBadge;
    private View deadlineBar;

    private final int[] DAY_BOX_IDS = {
        R.id.dayBox0, R.id.dayBox1, R.id.dayBox2,
        R.id.dayBox3, R.id.dayBox4, R.id.dayBox5, R.id.dayBox6
    };

    // ── Helper: ambil nama prefs berdasarkan email user yang sedang login ──
    public static String getPrefName(Context context) {
        SharedPreferences session = context.getSharedPreferences("Session", Context.MODE_PRIVATE);
        String email = session.getString("USER_EMAIL", "");
        if (email.isEmpty()) return PREF_NAME_BASE;
        return PREF_NAME_BASE + "_" + email;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Views
        tvUser             = findViewById(R.id.tvUserDashboard);
        tvAvatar           = findViewById(R.id.tvAvatar);
        tvStreak           = findViewById(R.id.tvStreak);
        tvStatNilai        = findViewById(R.id.tvStatNilai);
        tvStatTotalQuiz    = findViewById(R.id.tvStatTotalQuiz);
        pbCoding           = findViewById(R.id.pbCoding);
        pbUIUX             = findViewById(R.id.pbUIUX);
        pbDesign           = findViewById(R.id.pbDesign);
        tvProgressCoding   = findViewById(R.id.tvProgressCoding);
        tvProgressUIUX     = findViewById(R.id.tvProgressUIUX);
        tvProgressDesign   = findViewById(R.id.tvProgressDesign);
        badgeQuizPertama   = findViewById(R.id.badgeQuizPertama);
        badgeNilaiSempurna = findViewById(R.id.badgeNilaiSempurna);
        badgeSemuaMateri   = findViewById(R.id.badgeSemuaMateri);
        badgeStreak        = findViewById(R.id.badgeStreak);
        tvAktivitasInfo    = findViewById(R.id.tvAktivitasInfo);
        cardDeadline       = findViewById(R.id.cardDeadline);
        cardNoDeadline     = findViewById(R.id.cardNoDeadline);
        tvDeadlineMateri   = findViewById(R.id.tvDeadlineMateri);
        tvDeadlineWaktu    = findViewById(R.id.tvDeadlineWaktu);
        tvDeadlineBadge    = findViewById(R.id.tvDeadlineBadge);
        deadlineBar        = findViewById(R.id.deadlineBar);

        // Navbar
        NavHelper.setup(this, NavHelper.TAB_HOME);

        CardView cardMateri = findViewById(R.id.cardMateri);
        CardView btnQuiz    = findViewById(R.id.btnGoQuiz);
        if (cardMateri != null) cardMateri.setOnClickListener(v -> NavHelper.navigate(this, MateriActivity.class, true));
        if (btnQuiz    != null) btnQuiz.setOnClickListener(v   -> NavHelper.navigate(this, QuizCategoryActivity.class, true));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStreakAndActivity(); // harus pertama — catat hari aktif
        loadHeader();
        loadStats();
        loadProgressMateri();
        loadBadges();
        loadWeeklyActivity();
        loadDeadline();
    }

    // ── Header ──────────────────────────────────────────────────────────────

    private void loadHeader() {
        SharedPreferences prefs = getSharedPreferences(getPrefName(this), Context.MODE_PRIVATE);
        String nama = prefs.getString(KEY_USER_NAME, "Pelajar");
        tvUser.setText(nama);

        String[] parts = nama.trim().split("\\s+");
        String inisial = parts.length >= 2
                ? String.valueOf(parts[0].charAt(0)) + String.valueOf(parts[1].charAt(0))
                : nama.substring(0, Math.min(2, nama.length()));
        tvAvatar.setText(inisial.toUpperCase());

        int streak = prefs.getInt(KEY_STREAK_COUNT, 0);
        tvStreak.setText(streak + " hari streak");
    }

    // ── Stats ────────────────────────────────────────────────────────────────

    private void loadStats() {
        SharedPreferences prefs = getSharedPreferences(getPrefName(this), Context.MODE_PRIVATE);
        int skorCoding = prefs.getInt(QuizActivity.KEY_SKOR_PREFIX + "Coding", -1);
        int skorUIUX   = prefs.getInt(QuizActivity.KEY_SKOR_PREFIX + "UIUX",   -1);
        int skorDesign = prefs.getInt(QuizActivity.KEY_SKOR_PREFIX + "Design",  -1);
        int totalQuiz  = prefs.getInt(QuizActivity.KEY_TOTAL_QUIZ, 0);

        int nilaiBest = Math.max(skorCoding, Math.max(skorUIUX, skorDesign));
        tvStatNilai.setText(nilaiBest >= 0 ? nilaiBest + "" : "-");
        tvStatTotalQuiz.setText(String.valueOf(totalQuiz));
    }

    // ── Progress per materi ──────────────────────────────────────────────────

    private void loadProgressMateri() {
        SharedPreferences prefs = getSharedPreferences(getPrefName(this), Context.MODE_PRIVATE);

        int skorCoding = prefs.getInt(QuizActivity.KEY_SKOR_PREFIX + "Coding", -1);
        int skorUIUX   = prefs.getInt(QuizActivity.KEY_SKOR_PREFIX + "UIUX",   -1);
        int skorDesign = prefs.getInt(QuizActivity.KEY_SKOR_PREFIX + "Design",  -1);

        setProgress(pbCoding, tvProgressCoding, skorCoding);
        setProgress(pbUIUX,   tvProgressUIUX,   skorUIUX);
        setProgress(pbDesign, tvProgressDesign,  skorDesign);
    }

    private void setProgress(ProgressBar pb, TextView tv, int skor) {
        if (skor >= 0) {
            pb.setProgress(skor);
            tv.setText(skor + "%");
        } else {
            pb.setProgress(0);
            tv.setText("-");
        }
    }

    // ── Badge pencapaian ─────────────────────────────────────────────────────

    private void loadBadges() {
        SharedPreferences prefs = getSharedPreferences(getPrefName(this), Context.MODE_PRIVATE);

        int totalQuiz  = prefs.getInt(QuizActivity.KEY_TOTAL_QUIZ, 0);
        int skorCoding = prefs.getInt(QuizActivity.KEY_SKOR_PREFIX + "Coding", -1);
        int skorUIUX   = prefs.getInt(QuizActivity.KEY_SKOR_PREFIX + "UIUX",   -1);
        int skorDesign = prefs.getInt(QuizActivity.KEY_SKOR_PREFIX + "Design",  -1);
        int streak     = prefs.getInt(KEY_STREAK_COUNT, 0);

        boolean sudahBukaAndroid = prefs.getBoolean(DetailMateriActivity.PREFIX_MATERI_DIBUKA + "Coding",  false);
        boolean sudahBukaUIUX   = prefs.getBoolean(DetailMateriActivity.PREFIX_MATERI_DIBUKA + "UIUX",    false);
        boolean sudahBukaDesign = prefs.getBoolean(DetailMateriActivity.PREFIX_MATERI_DIBUKA + "Design",  false);

        // 🎯 Quiz pertama — sudah pernah kerjakan quiz
        setBadge(badgeQuizPertama, totalQuiz >= 1);

        // ⭐ Nilai sempurna — dapat 100 di salah satu quiz
        setBadge(badgeNilaiSempurna, skorCoding == 100 || skorUIUX == 100 || skorDesign == 100);

        // 📚 Semua materi — sudah buka ketiga materi
        setBadge(badgeSemuaMateri, sudahBukaAndroid && sudahBukaUIUX && sudahBukaDesign);

        // 🔥 Streak 7 hari
        setBadge(badgeStreak, streak >= 7);
    }

    private void setBadge(TextView badge, boolean unlocked) {
        badge.setAlpha(unlocked ? 1.0f : 0.25f);
    }

    // ── Streak & catat hari aktif ────────────────────────────────────────────

    private void updateStreakAndActivity() {
        SharedPreferences prefs = getSharedPreferences(getPrefName(this), Context.MODE_PRIVATE);
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String lastDate = prefs.getString(KEY_STREAK_LAST, "");

        // Catat hari aktif minggu ini
        prefs.edit().putBoolean(KEY_WEEKLY_PREFIX + today, true).apply();

        if (today.equals(lastDate)) return; // Sudah dicatat hari ini

        int streak = prefs.getInt(KEY_STREAK_COUNT, 0);

        // Cek apakah kemarin juga aktif
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime());

        if (yesterday.equals(lastDate)) {
            streak++; // Lanjutkan streak
        } else {
            streak = 1; // Reset streak
        }

        prefs.edit()
                .putInt(KEY_STREAK_COUNT, streak)
                .putString(KEY_STREAK_LAST, today)
                .apply();
    }

    // ── Aktivitas mingguan ───────────────────────────────────────────────────

    private void loadWeeklyActivity() {
        SharedPreferences prefs = getSharedPreferences(getPrefName(this), Context.MODE_PRIVATE);

        // Dapatkan Senin minggu ini
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        int aktifCount = 0;

        for (int i = 0; i < 7; i++) {
            String tanggal = sdf.format(cal.getTime());
            boolean aktif  = prefs.getBoolean(KEY_WEEKLY_PREFIX + tanggal, false);

            LinearLayout dayBox = findViewById(DAY_BOX_IDS[i]);
            if (dayBox != null) {
                View kotak = dayBox.getChildAt(0);
                if (kotak != null) {
                    kotak.setBackgroundResource(aktif
                            ? R.drawable.bg_day_active
                            : R.drawable.bg_day_inactive);
                }
            }

            if (aktif) aktifCount++;
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        if (aktifCount == 0) {
            tvAktivitasInfo.setText("Belum ada aktivitas minggu ini");
        } else if (aktifCount == 7) {
            tvAktivitasInfo.setText("🎉 Sempurna! Aktif 7 hari penuh minggu ini!");
        } else {
            tvAktivitasInfo.setText("Aktif " + aktifCount + " dari 7 hari minggu ini");
        }
    }

    // ── Deadline ─────────────────────────────────────────────────────────────

    public static void simpanDeadline(Context context, String namaMateri) {
        long tigaHari = TimeUnit.DAYS.toMillis(3);
        long deadlineTs = System.currentTimeMillis() + tigaHari;
        SharedPreferences prefs = context.getSharedPreferences(getPrefName(context), Context.MODE_PRIVATE);
        long existing = prefs.getLong(KEY_DEADLINE_TS, 0L);
        if (existing == 0L || System.currentTimeMillis() > existing) {
            prefs.edit()
                    .putLong(KEY_DEADLINE_TS, deadlineTs)
                    .putString(KEY_DEADLINE_MATERI, namaMateri)
                    .apply();
        }
    }

    private void loadDeadline() {
        SharedPreferences prefs = getSharedPreferences(getPrefName(this), Context.MODE_PRIVATE);
        long deadlineTs   = prefs.getLong(KEY_DEADLINE_TS, 0L);
        String namaMateri = prefs.getString(KEY_DEADLINE_MATERI, "-");

        if (deadlineTs == 0L) {
            cardDeadline.setVisibility(View.GONE);
            cardNoDeadline.setVisibility(View.VISIBLE);
            return;
        }

        long now     = System.currentTimeMillis();
        long selisih = deadlineTs - now;

        if (selisih <= 0) {
            prefs.edit().remove(KEY_DEADLINE_TS).remove(KEY_DEADLINE_MATERI).apply();
            cardDeadline.setVisibility(View.GONE);
            cardNoDeadline.setVisibility(View.VISIBLE);
            return;
        }

        long sisaHari  = TimeUnit.MILLISECONDS.toDays(selisih);
        long sisaJam   = TimeUnit.MILLISECONDS.toHours(selisih) % 24;
        long sisaMenit = TimeUnit.MILLISECONDS.toMinutes(selisih) % 60;

        String tglDeadline = new SimpleDateFormat("dd MMM yyyy, HH:mm", new Locale("id", "ID"))
                .format(new Date(deadlineTs));

        String sisaText = sisaHari > 0
                ? "Sisa " + sisaHari + " hari " + sisaJam + " jam · " + tglDeadline
                : sisaJam > 0
                    ? "Sisa " + sisaJam + " jam " + sisaMenit + " menit · SEGERA!"
                    : "Sisa " + sisaMenit + " menit · SEGERA!";

        String badgeText = sisaHari > 0 ? sisaHari + " Hari" : sisaJam + " Jam";

        int warna = sisaHari >= 2 ? Color.parseColor("#4CAF50")
                  : sisaHari == 1 ? Color.parseColor("#FF9800")
                  :                 Color.parseColor("#FF5252");

        deadlineBar.setBackgroundColor(warna);
        tvDeadlineBadge.setBackgroundColor(warna);
        tvDeadlineMateri.setText("Kerjakan Quiz: " + namaMateri);
        tvDeadlineWaktu.setText(sisaText);
        tvDeadlineBadge.setText(badgeText);

        cardDeadline.setVisibility(View.VISIBLE);
        cardNoDeadline.setVisibility(View.GONE);
    }
}
