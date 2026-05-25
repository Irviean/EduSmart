package com.example.projekuts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MateriActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materi);

        NavHelper.setup(this, NavHelper.TAB_MATERI);

        CardView cardAndroid = findViewById(R.id.cardMateriAndroid);
        CardView cardUIUX    = findViewById(R.id.cardMateriUIUX);
        CardView cardDesign  = findViewById(R.id.cardMateriDesign);

        cardAndroid.setOnClickListener(v -> bukaDetailMateri(
            "Materi 1: Dasar Android & Java",
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "📁 1. STRUKTUR FOLDER ANDROID\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "Setiap proyek Android terdiri dari beberapa folder utama:\n\n" +
            "• manifests/ → AndroidManifest.xml\n" +
            "  Mendaftarkan semua Activity, izin aplikasi, dan konfigurasi utama.\n\n" +
            "• java/ → Logika aplikasi (.java / .kt)\n" +
            "  Tempat semua file Activity, Fragment, dan class pendukung.\n\n" +
            "• res/ → Resource aplikasi\n" +
            "  - layout/  : file XML tampilan\n" +
            "  - drawable/ : gambar & icon\n" +
            "  - values/   : warna, string, style\n\n" +
            "• Gradle Scripts → build.gradle\n" +
            "  Konfigurasi dependency dan versi SDK.\n\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "🔄 2. ACTIVITY LIFECYCLE\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "Activity punya siklus hidup yang harus dipahami:\n\n" +
            "onCreate()   → Activity pertama kali dibuat\n" +
            "onStart()    → Activity mulai terlihat user\n" +
            "onResume()   → Activity aktif & bisa diinteraksi\n" +
            "onPause()    → Activity sebagian tertutup\n" +
            "onStop()     → Activity tidak terlihat\n" +
            "onDestroy()  → Activity dihancurkan\n\n" +
            "📌 Contoh penggunaan:\n\n" +
            "┌─────────────────────────────────┐\n" +
            "│ @Override                       │\n" +
            "│ protected void onCreate(        │\n" +
            "│     Bundle savedInstanceState) {│\n" +
            "│   super.onCreate(               │\n" +
            "│       savedInstanceState);      │\n" +
            "│   setContentView(               │\n" +
            "│       R.layout.activity_main);  │\n" +
            "│                                 │\n" +
            "│   TextView tv =                 │\n" +
            "│     findViewById(R.id.tvHello); │\n" +
            "│   tv.setText(\"Hello World!\");   │\n" +
            "│ }                               │\n" +
            "└─────────────────────────────────┘\n\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "📐 3. LAYOUTING XML\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "• LinearLayout  : susun vertikal / horizontal\n" +
            "• ConstraintLayout : posisi fleksibel\n" +
            "• RelativeLayout : relatif terhadap parent\n\n" +
            "📌 Contoh LinearLayout:\n\n" +
            "┌─────────────────────────────────┐\n" +
            "│ <LinearLayout                   │\n" +
            "│   android:orientation=          │\n" +
            "│     \"vertical\">                 │\n" +
            "│   <TextView                     │\n" +
            "│     android:text=\"Halo!\"        │\n" +
            "│     android:textSize=\"18sp\"/>   │\n" +
            "│ </LinearLayout>                 │\n" +
            "└─────────────────────────────────┘\n\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "🔗 4. INTENT & NAVIGASI\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "📌 Pindah Activity + kirim data:\n\n" +
            "┌─────────────────────────────────┐\n" +
            "│ Intent intent = new Intent(     │\n" +
            "│   this, DetailActivity.class);  │\n" +
            "│ intent.putExtra(\"NAMA\",         │\n" +
            "│   \"Irviean\");                   │\n" +
            "│ startActivity(intent);          │\n" +
            "└─────────────────────────────────┘\n\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "💾 5. SHAREDPREFERENCES\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "📌 Menyimpan data:\n\n" +
            "┌─────────────────────────────────┐\n" +
            "│ SharedPreferences prefs =       │\n" +
            "│   getSharedPreferences(         │\n" +
            "│     \"AppPrefs\", MODE_PRIVATE);  │\n" +
            "│ prefs.edit()                    │\n" +
            "│   .putString(\"nama\",\"Irviean\")  │\n" +
            "│   .apply();                     │\n" +
            "└─────────────────────────────────┘",
            "Coding"
        ));

        cardUIUX.setOnClickListener(v -> bukaDetailMateri(
            "Materi 2: Pengenalan UI / UX",
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "🎨 1. APA ITU UI & UX?\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "UI (User Interface) adalah semua elemen visual yang dilihat dan disentuh pengguna.\n\n" +
            "UX (User Experience) adalah keseluruhan pengalaman pengguna saat menggunakan produk.\n\n" +
            "📌 Analogi:\n" +
            "UI = tampilan restoran\n" +
            "UX = pengalaman makan di sana\n\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "📐 2. PRINSIP DESAIN UI\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "• Konsistensi  : warna & font seragam\n" +
            "• Hierarki     : judul > subjudul > isi\n" +
            "• Feedback     : respons visual saat interaksi\n" +
            "• Whitespace   : ruang napas tampilan\n" +
            "• Aksesibilitas: kontras & ukuran cukup\n\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "🎯 3. MATERIAL DESIGN\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "📌 Contoh Snackbar:\n\n" +
            "┌─────────────────────────────────┐\n" +
            "│ Snackbar.make(                  │\n" +
            "│   view,                         │\n" +
            "│   \"Data berhasil disimpan!\",    │\n" +
            "│   Snackbar.LENGTH_SHORT)        │\n" +
            "│   .show();                      │\n" +
            "└─────────────────────────────────┘\n\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "🌈 4. TEORI WARNA DALAM UI\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "🔵 Biru   → Kepercayaan, profesional\n" +
            "🟢 Hijau  → Sukses, konfirmasi\n" +
            "🔴 Merah  → Bahaya, peringatan\n" +
            "🟡 Kuning → Perhatian, energi\n\n" +
            "Tips: Gunakan maksimal 3 warna utama.",
            "UIUX"
        ));

        cardDesign.setOnClickListener(v -> bukaDetailMateri(
            "Materi 3: Dasar Desain Grafis",
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "🎨 1. TEORI WARNA\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "• RGB  → layar digital (0–255)\n" +
            "• CMYK → media cetak\n" +
            "• HEX  → #RRGGBB\n\n" +
            "Contoh: Putih = #FFFFFF, Hitam = #000000\n\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "✍️ 2. TIPOGRAFI\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "• Serif      : Times New Roman (formal)\n" +
            "• Sans-Serif : Roboto, Arial (digital)\n" +
            "• Monospace  : Consolas (kode)\n\n" +
            "Hierarki:\n" +
            "H1: 24–32sp Bold\n" +
            "H2: 18–22sp SemiBold\n" +
            "Body: 14–16sp Regular\n\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "🖼️ 3. FORMAT FILE GAMBAR\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "• JPEG  → foto, tidak ada transparansi\n" +
            "• PNG   → mendukung transparansi\n" +
            "• SVG   → vektor, tidak pecah diperbesar\n" +
            "• WebP  → modern, ukuran kecil\n\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
            "📐 4. PRINSIP DESAIN GRAFIS\n" +
            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
            "• Balance   : distribusi elemen merata\n" +
            "• Contrast  : perbedaan mencolok untuk fokus\n" +
            "• Repetition: pengulangan untuk konsistensi\n" +
            "• Proximity : elemen terkait berdekatan\n" +
            "• Alignment : perataan untuk tampilan rapi",
            "Design"
        ));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProgressBars();
    }

    private void loadProgressBars() {
        SharedPreferences prefs = getSharedPreferences(DashboardActivity.getPrefName(this), Context.MODE_PRIVATE);

        int skorCoding = prefs.getInt(QuizActivity.KEY_SKOR_PREFIX + "Coding", -1);
        int skorUIUX   = prefs.getInt(QuizActivity.KEY_SKOR_PREFIX + "UIUX",   -1);
        int skorDesign = prefs.getInt(QuizActivity.KEY_SKOR_PREFIX + "Design",  -1);

        setProgressBar(R.id.pbMateriAndroid, R.id.tvPctAndroid, skorCoding);
        setProgressBar(R.id.pbMateriUIUX,    R.id.tvPctUIUX,    skorUIUX);
        setProgressBar(R.id.pbMateriDesign,  R.id.tvPctDesign,  skorDesign);

        // Update subtitle header
        int selesai = 0;
        if (skorCoding >= 0) selesai++;
        if (skorUIUX   >= 0) selesai++;
        if (skorDesign >= 0) selesai++;

        TextView subtitle = findViewById(R.id.tvSubtitleMateri);
        if (subtitle != null) {
            subtitle.setText("3 materi tersedia · " + selesai + " quiz selesai");
        }
    }

    private void setProgressBar(int pbId, int tvId, int skor) {
        ProgressBar pb = findViewById(pbId);
        TextView tv    = findViewById(tvId);
        if (pb == null || tv == null) return;
        if (skor >= 0) {
            pb.setProgress(skor);
            tv.setText(skor + "/100");
        } else {
            pb.setProgress(0);
            tv.setText("Belum dikerjakan");
        }
    }

    private void bukaDetailMateri(String judul, String konten, String kategoriQuiz) {
        Intent intent = new Intent(this, DetailMateriActivity.class);
        intent.putExtra("JUDUL", judul);
        intent.putExtra("KONTEN", konten);
        intent.putExtra("KATEGORI_QUIZ", kategoriQuiz);
        NavHelper.navigateForward(this, intent);
    }

    @Override
    public void onBackPressed() {
        NavHelper.navigateBack(this);
    }
}
