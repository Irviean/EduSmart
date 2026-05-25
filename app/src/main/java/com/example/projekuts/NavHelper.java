package com.example.projekuts;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * NavHelper — setup bottom navbar + animasi transisi smooth untuk semua halaman.
 *
 * Cara pakai di setiap Activity:
 *   NavHelper.setup(this, NavHelper.TAB_MATERI);
 */
public class NavHelper {

    public static final int TAB_HOME   = 0;
    public static final int TAB_MATERI = 1;
    public static final int TAB_QUIZ   = 2;
    public static final int TAB_PROFIL = 3;

    public static void setup(Activity activity, int activeTab) {
        LinearLayout navHome   = activity.findViewById(R.id.navHome);
        LinearLayout navMateri = activity.findViewById(R.id.navMateri);
        LinearLayout navQuiz   = activity.findViewById(R.id.navQuiz);
        LinearLayout navProfil = activity.findViewById(R.id.navProfil);

        if (navHome == null || navMateri == null || navQuiz == null || navProfil == null) return;

        // Set tab aktif
        setActive(navHome,   activeTab == TAB_HOME);
        setActive(navMateri, activeTab == TAB_MATERI);
        setActive(navQuiz,   activeTab == TAB_QUIZ);
        setActive(navProfil, activeTab == TAB_PROFIL);

        // Listener tiap tab
        navHome.setOnClickListener(v -> {
            if (activeTab != TAB_HOME)
                navigate(activity, DashboardActivity.class, false);
        });
        navMateri.setOnClickListener(v -> {
            if (activeTab != TAB_MATERI)
                navigate(activity, MateriActivity.class, activeTab < TAB_MATERI);
        });
        navQuiz.setOnClickListener(v -> {
            if (activeTab != TAB_QUIZ)
                navigate(activity, QuizCategoryActivity.class, activeTab < TAB_QUIZ);
        });
        navProfil.setOnClickListener(v -> {
            if (activeTab != TAB_PROFIL)
                navigate(activity, ProfilActivity.class, activeTab < TAB_PROFIL);
        });
    }

    /**
     * Navigasi antar tab navbar dengan animasi fade.
     * goRight=true  → slide ke kanan (tab lebih tinggi)
     * goRight=false → slide ke kiri  (tab lebih rendah / Home)
     */
    public static void navigate(Activity from, Class<?> to, boolean goRight) {
        Intent intent = new Intent(from, to);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        from.startActivity(intent);
        if (goRight) {
            from.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            from.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        // Jangan finish — biarkan back stack tetap ada
    }

    /**
     * Navigasi maju (buka Detail Materi, QuizActivity, dll) — slide dari kanan.
     */
    public static void navigateForward(Activity from, Intent intent) {
        from.startActivity(intent);
        from.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * Panggil di onBackPressed() atau tombol back toolbar — slide balik ke kiri.
     */
    public static void navigateBack(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    // Warna teks/ikon tab aktif vs non-aktif
    private static void setActive(LinearLayout tab, boolean active) {
        int color = active ? 0xFF007BFF : 0xFF9E9E9E;
        for (int i = 0; i < tab.getChildCount(); i++) {
            View child = tab.getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setTextColor(color);
            }
        }
        // Dot indikator (View anak terakhir kalau ada)
        View lastChild = tab.getChildAt(tab.getChildCount() - 1);
        if (lastChild != null && !(lastChild instanceof TextView)) {
            lastChild.setVisibility(active ? View.VISIBLE : View.GONE);
        }
    }
}
