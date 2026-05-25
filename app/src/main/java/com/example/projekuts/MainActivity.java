package com.example.projekuts;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView item1 = findViewById(R.id.itemMateri1);
        Button btnBack = findViewById(R.id.btnBackMain);

        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Nanti bisa diarahkan ke halaman detail materi
                Toast.makeText(MainActivity.this, "Membuka Materi...", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Tutup MainActivity, balik ke Dashboard
            }
        });
    }
}