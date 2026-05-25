package com.example.projekuts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DetailMateriActivity extends AppCompatActivity {

    public static final String PREFIX_MATERI_DIBUKA = "MATERI_DIBUKA_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_materi);

        Toolbar toolbar = findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Drawable upArrow = toolbar.getNavigationIcon();
            if (upArrow != null) {
                upArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                getSupportActionBar().setHomeAsUpIndicator(upArrow);
            }
        }

        TextView tvTitle   = findViewById(R.id.tvDetailTitle);
        TextView tvContent = findViewById(R.id.tvDetailContent);
        Button   btnQuiz   = findViewById(R.id.btnMulaiQuiz);

        String judulMateri  = getIntent().getStringExtra("JUDUL");
        String isiMateri    = getIntent().getStringExtra("KONTEN");
        String kategoriQuiz = getIntent().getStringExtra("KATEGORI_QUIZ");

        if (judulMateri != null) tvTitle.setText(judulMateri);
        if (isiMateri   != null) tvContent.setText(isiMateri);

        if (kategoriQuiz != null) {
            SharedPreferences prefs = getSharedPreferences(DashboardActivity.getPrefName(this), Context.MODE_PRIVATE);
            prefs.edit().putBoolean(PREFIX_MATERI_DIBUKA + kategoriQuiz, true).apply();
            DashboardActivity.simpanDeadline(this, judulMateri);

            btnQuiz.setVisibility(View.VISIBLE);
            String finalKategoriQuiz = kategoriQuiz;
            btnQuiz.setOnClickListener(v -> {
                Intent intent = new Intent(this, QuizActivity.class);
                intent.putExtra("KATEGORI_QUIZ", finalKategoriQuiz);
                NavHelper.navigateForward(this, intent);
            });
        } else {
            btnQuiz.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavHelper.navigateBack(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        NavHelper.navigateBack(this);
    }
}
