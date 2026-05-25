package com.example.projekuts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    public static final String KEY_SKOR_PREFIX    = "SKOR_";
    public static final String KEY_TANGGAL_PREFIX = "TANGGAL_";
    public static final String KEY_TOTAL_QUIZ     = "TOTAL_QUIZ_SELESAI";

    // Layout views
    private ProgressBar pbQuizProgress;
    private TextView tvQuizTitle, tvProgress, tvNomorSoal, tvQuestion, tvScoreCount;
    private CardView btnOption1, btnOption2, btnOption3, btnOption4;
    private TextView tvOption1, tvOption2, tvOption3, tvOption4;
    private TextView letterA, letterB, letterC, letterD;
    private Button btnNext;

    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int selectedOption = -1;
    private String kategoriTerpilih;

    // Warna state
    private static final int COLOR_DEFAULT_CARD   = Color.WHITE;
    private static final int COLOR_SELECTED_CARD  = Color.parseColor("#E3F2FD");
    private static final int COLOR_SELECTED_LETTER = Color.parseColor("#007BFF");
    private static final int COLOR_DEFAULT_LETTER  = Color.parseColor("#E3F2FD");
    private static final int COLOR_LETTER_TEXT_SELECTED = Color.WHITE;
    private static final int COLOR_LETTER_TEXT_DEFAULT  = Color.parseColor("#007BFF");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        pbQuizProgress = findViewById(R.id.pbQuizProgress);
        tvQuizTitle    = findViewById(R.id.tvQuizTitle);
        tvProgress     = findViewById(R.id.tvProgress);
        tvNomorSoal    = findViewById(R.id.tvNomorSoal);
        tvQuestion     = findViewById(R.id.tvQuestion);
        tvScoreCount   = findViewById(R.id.tvScoreCount);
        btnOption1     = findViewById(R.id.btnOption1);
        btnOption2     = findViewById(R.id.btnOption2);
        btnOption3     = findViewById(R.id.btnOption3);
        btnOption4     = findViewById(R.id.btnOption4);
        tvOption1      = findViewById(R.id.tvOption1);
        tvOption2      = findViewById(R.id.tvOption2);
        tvOption3      = findViewById(R.id.tvOption3);
        tvOption4      = findViewById(R.id.tvOption4);
        letterA        = findViewById(R.id.letterA);
        letterB        = findViewById(R.id.letterB);
        letterC        = findViewById(R.id.letterC);
        letterD        = findViewById(R.id.letterD);
        btnNext        = findViewById(R.id.btnNext);

        kategoriTerpilih = getIntent().getStringExtra("KATEGORI_QUIZ");
        if (kategoriTerpilih == null) kategoriTerpilih = "Coding";

        tvQuizTitle.setText("Quiz " + kategoriTerpilih);

        setupQuestions();
        loadQuestion();

        btnOption1.setOnClickListener(v -> selectOption(0));
        btnOption2.setOnClickListener(v -> selectOption(1));
        btnOption3.setOnClickListener(v -> selectOption(2));
        btnOption4.setOnClickListener(v -> selectOption(3));

        btnNext.setOnClickListener(v -> {
            if (selectedOption == -1) {
                Toast.makeText(this, "Pilih salah satu jawaban dulu!", Toast.LENGTH_SHORT).show();
                return;
            }

            Question q = questionList.get(currentQuestionIndex);
            if (selectedOption == q.getCorrectAnswer()) score++;

            tvScoreCount.setText(String.valueOf(score));

            if (currentQuestionIndex < questionList.size() - 1) {
                currentQuestionIndex++;
                loadQuestion();
            } else {
                int nilai = (score * 100) / questionList.size();
                simpanHasilQuiz(nilai);
                bukaHasilQuiz(nilai);
            }
        });
    }

    // ── Load soal ────────────────────────────────────────────────────────────

    private void loadQuestion() {
        selectedOption = -1;
        resetAllOptions();

        Question q    = questionList.get(currentQuestionIndex);
        int total     = questionList.size();
        int no        = currentQuestionIndex + 1;
        int progress  = (no * 100) / total;

        // Progress bar atas
        pbQuizProgress.setProgress(progress);

        // Header info
        tvProgress.setText("Soal " + no + " dari " + total);
        tvNomorSoal.setText("Pertanyaan " + no);

        // Soal & opsi
        tvQuestion.setText(q.getQuestionText());
        tvOption1.setText(q.getOption1());
        tvOption2.setText(q.getOption2());
        tvOption3.setText(q.getOption3());
        tvOption4.setText(q.getOption4());

        // Tombol next
        boolean isLast = currentQuestionIndex == total - 1;
        btnNext.setText(isLast ? "Selesai ✓" : "Selanjutnya →");
        btnNext.setBackgroundTintList(android.content.res.ColorStateList.valueOf(
                Color.parseColor(isLast ? "#FFCC80" : "#A3D9B1")));
        btnNext.setTextColor(Color.parseColor(isLast ? "#E65100" : "#1B5E20"));
    }

    private void selectOption(int idx) {
        selectedOption = idx;
        resetAllOptions();

        CardView  selectedCard   = getCardAt(idx);
        TextView  selectedLetter = getLetterAt(idx);

        if (selectedCard != null) selectedCard.setCardBackgroundColor(COLOR_SELECTED_CARD);
        if (selectedLetter != null) {
            selectedLetter.setBackgroundColor(Color.TRANSPARENT);
            android.graphics.drawable.GradientDrawable circle = new android.graphics.drawable.GradientDrawable();
            circle.setShape(android.graphics.drawable.GradientDrawable.OVAL);
            circle.setColor(COLOR_SELECTED_LETTER);
            selectedLetter.setBackground(circle);
            selectedLetter.setTextColor(COLOR_LETTER_TEXT_SELECTED);
        }
    }

    private void resetAllOptions() {
        CardView[]  cards   = {btnOption1, btnOption2, btnOption3, btnOption4};
        TextView[]  letters = {letterA, letterB, letterC, letterD};

        for (CardView card : cards) {
            if (card != null) card.setCardBackgroundColor(COLOR_DEFAULT_CARD);
        }
        for (TextView letter : letters) {
            if (letter != null) {
                android.graphics.drawable.GradientDrawable circle = new android.graphics.drawable.GradientDrawable();
                circle.setShape(android.graphics.drawable.GradientDrawable.OVAL);
                circle.setColor(COLOR_DEFAULT_LETTER);
                letter.setBackground(circle);
                letter.setTextColor(COLOR_LETTER_TEXT_DEFAULT);
            }
        }
    }

    private CardView getCardAt(int idx) {
        switch (idx) {
            case 0: return btnOption1;
            case 1: return btnOption2;
            case 2: return btnOption3;
            case 3: return btnOption4;
            default: return null;
        }
    }

    private TextView getLetterAt(int idx) {
        switch (idx) {
            case 0: return letterA;
            case 1: return letterB;
            case 2: return letterC;
            case 3: return letterD;
            default: return null;
        }
    }

    // ── Simpan & buka hasil ──────────────────────────────────────────────────

    private void simpanHasilQuiz(int nilai) {
        SharedPreferences prefs = getSharedPreferences(DashboardActivity.getPrefName(this), Context.MODE_PRIVATE);
        String tanggal = new SimpleDateFormat("dd MMM yyyy", new Locale("id", "ID")).format(new Date());

        int skorLama = prefs.getInt(KEY_SKOR_PREFIX + kategoriTerpilih, -1);
        if (nilai > skorLama) {
            prefs.edit()
                    .putInt(KEY_SKOR_PREFIX + kategoriTerpilih, nilai)
                    .putString(KEY_TANGGAL_PREFIX + kategoriTerpilih, tanggal)
                    .apply();
        }

        int totalQuiz = prefs.getInt(KEY_TOTAL_QUIZ, 0);
        prefs.edit().putInt(KEY_TOTAL_QUIZ, totalQuiz + 1).apply();

        String materiTerkait = prefs.getString(DashboardActivity.KEY_DEADLINE_MATERI, "");
        if ((kategoriTerpilih.equals("Coding")  && materiTerkait.contains("Android")) ||
            (kategoriTerpilih.equals("UIUX")    && materiTerkait.contains("UI"))      ||
            (kategoriTerpilih.equals("Design")  && materiTerkait.contains("Desain"))) {
            prefs.edit()
                    .remove(DashboardActivity.KEY_DEADLINE_TS)
                    .remove(DashboardActivity.KEY_DEADLINE_MATERI)
                    .apply();
        }
    }

    private void bukaHasilQuiz(int nilai) {
        Intent intent = new Intent(this, HasilQuizActivity.class);
        intent.putExtra("NILAI",    nilai);
        intent.putExtra("BENAR",    score);
        intent.putExtra("TOTAL",    questionList.size());
        intent.putExtra("KATEGORI", kategoriTerpilih);
        NavHelper.navigateForward(this, intent);
        finish();
    }

    // ── Daftar soal ──────────────────────────────────────────────────────────

    private void setupQuestions() {
        questionList = new ArrayList<>();

        if (kategoriTerpilih.equals("Coding")) {
            questionList.add(new Question("Apa fungsi metode onCreate() dalam Activity Android?","Menghancurkan Activity","Menginisialisasi Activity saat pertama kali dibuat","Menyimpan data secara otomatis","Menghentikan semua proses background",1));
            questionList.add(new Question("Manakah cara yang benar untuk berpindah Activity di Android?","moveToActivity(DetailActivity.class)","Activity.go(DetailActivity.class)","startActivity(new Intent(this, DetailActivity.class))","open(new DetailActivity())",2));
            questionList.add(new Question("Apa kegunaan file AndroidManifest.xml?","Menyimpan gambar dan icon aplikasi","Mendaftarkan Activity, permission, dan konfigurasi aplikasi","Mengatur tampilan layout halaman utama","Menyimpan database lokal aplikasi",1));
            questionList.add(new Question("Atribut XML apa yang digunakan agar View mengisi lebar penuh layar?","android:layout_width=\"fill\"","android:width=\"full\"","android:layout_width=\"match_parent\"","android:layout_width=\"100dp\"",2));
            questionList.add(new Question("Apa perbedaan utama antara putExtra() dan putString() di Intent?","putExtra() untuk String, putString() untuk Integer","putString() adalah metode Intent, putExtra() bukan","putExtra() adalah metode Intent untuk semua tipe; putString() tidak ada di Intent","Keduanya sama persis",2));
            questionList.add(new Question("SharedPreferences digunakan untuk menyimpan data jenis apa?","File gambar berukuran besar","Data kompleks seperti daftar objek","Data sederhana berupa pasangan key-value","Koneksi ke database server",2));
            questionList.add(new Question("Lifecycle method mana yang dipanggil ketika Activity kembali ke foreground setelah onPause()?","onCreate()","onRestart() → onStart() → onResume()","onResume() langsung","onStop()",1));
            questionList.add(new Question("Apa fungsi RecyclerView di Android?","Memutar video secara otomatis","Menampilkan daftar data yang bisa di-scroll secara efisien","Mengatur koneksi internet","Menyimpan data ke database Room",1));
            questionList.add(new Question("Pada layout XML, apa arti wrap_content pada layout_height?","Tinggi view mengikuti ukuran layar penuh","Tinggi view diatur secara manual dalam dp","Tinggi view menyesuaikan ukuran konten di dalamnya","View tidak memiliki tinggi",2));
            questionList.add(new Question("Dependency Room Database ditambahkan di file mana?","AndroidManifest.xml","activity_main.xml","strings.xml","build.gradle (app level)",3));
        }
        else if (kategoriTerpilih.equals("UIUX")) {
            questionList.add(new Question("Apa perbedaan utama antara UI dan UX?","UI dan UX adalah hal yang sama","UI fokus pada tampilan visual; UX fokus pada pengalaman keseluruhan","UX hanya tentang warna dan font","UI adalah back-end, UX adalah front-end",1));
            questionList.add(new Question("Apa yang dimaksud dengan Wireframe dalam proses desain?","Desain final berwarna yang siap dipresentasikan","Kode HTML untuk membuat tampilan web","Sketsa kasar tampilan aplikasi tanpa detail visual","Animasi transisi antar halaman",2));
            questionList.add(new Question("Prinsip desain 'Proximity' mengacu pada?","Penggunaan warna yang berdekatan dalam color wheel","Mengelompokkan elemen yang saling berhubungan agar berdekatan","Membuat semua elemen berukuran sama","Menggunakan font yang mirip satu sama lain",1));
            questionList.add(new Question("Minimum ukuran touch target yang direkomendasikan Material Design adalah?","24 x 24 dp","32 x 32 dp","48 x 48 dp","64 x 64 dp",2));
            questionList.add(new Question("Apa yang dimaksud dengan 'User Flow'?","Animasi halus saat user scroll halaman","Alur perjalanan pengguna dalam aplikasi dari awal hingga tujuan","Jumlah pengguna yang mengunjungi aplikasi per hari","Kecepatan loading halaman aplikasi",1));
            questionList.add(new Question("Manakah pernyataan yang benar tentang whitespace dalam desain UI?","Whitespace adalah pemborosan ruang layar","Whitespace harus diisi dengan konten atau iklan","Whitespace membantu tampilan lebih bersih dan meningkatkan keterbacaan","Whitespace hanya digunakan di desain cetak",2));
            questionList.add(new Question("Snackbar pada Material Design digunakan untuk?","Menampilkan menu navigasi utama","Menampilkan notifikasi singkat di bagian bawah layar","Menampilkan dialog konfirmasi yang memblok layar","Membuat loading spinner di tengah layar",1));
            questionList.add(new Question("Kontras rasio minimum yang disarankan WCAG untuk teks normal adalah?","1:1","2:1","3:1","4.5:1",3));
            questionList.add(new Question("Tools desain UI/UX berbasis web yang paling populer saat ini adalah?","Microsoft Word","Adobe Premiere","Figma","VLC Media Player",2));
            questionList.add(new Question("Apa yang dimaksud dengan 'Prototype' dalam desain UX?","Versi final aplikasi yang sudah di-publish","Simulasi interaktif desain sebelum development","Database yang menyimpan preferensi pengguna","Dokumentasi teknis untuk developer",1));
        }
        else if (kategoriTerpilih.equals("Design")) {
            questionList.add(new Question("Model warna RGB digunakan untuk media apa?","Cetak (printing) seperti buku dan brosur","Layar digital seperti monitor dan smartphone","Lukisan cat minyak","Desain untuk kain dan tekstil",1));
            questionList.add(new Question("Apa kepanjangan dari CMYK?","Color, Mix, Yellow, Key","Cyan, Magenta, Yellow, Key (Black)","Creative, Modern, Young, Kinetic","Contrast, Margin, Yield, Kerning",1));
            questionList.add(new Question("Format gambar mana yang mendukung background transparan?","JPEG","BMP","PNG","TIFF",2));
            questionList.add(new Question("Apa perbedaan desain Raster dan Vektor?","Raster menggunakan matematika, Vektor menggunakan piksel","Raster berbasis piksel (bisa pecah saat diperbesar); Vektor berbasis persamaan matematika","Raster dan Vektor adalah hal yang sama","Vektor hanya untuk foto, Raster untuk ilustrasi",1));
            questionList.add(new Question("Software Adobe Illustrator digunakan terutama untuk?","Editing video dan motion graphics","Manajemen foto dan katalog","Desain vektor seperti logo dan ilustrasi","Desain layout buku dan majalah",2));
            questionList.add(new Question("Font jenis Sans-Serif adalah font yang?","Memiliki kait di ujung huruf","Hanya tersedia dalam huruf kapital","Tidak memiliki kait di ujung huruf","Hanya bisa untuk judul",2));
            questionList.add(new Question("Prinsip desain 'Kontras' bertujuan untuk?","Membuat semua elemen terlihat serupa","Menciptakan perbedaan mencolok untuk fokus dan hierarki","Mengurangi jumlah warna dalam desain","Membuat teks menjadi lebih kecil",1));
            questionList.add(new Question("Format file WebP memiliki keunggulan apa dibanding PNG?","WebP mendukung lebih banyak warna","WebP bisa diputar sebagai video","Ukuran file WebP lebih kecil dengan kualitas sebanding","WebP lebih lama diproses oleh browser",2));
            questionList.add(new Question("Apa yang dimaksud dengan Kerning dalam tipografi?","Jarak antar baris teks","Ukuran font dalam satuan point","Penyesuaian jarak antar dua karakter tertentu","Ketebalan garis pada huruf",2));
            questionList.add(new Question("Ukuran teks yang tepat untuk body text di aplikasi mobile adalah?","8–10sp","14–16sp","24–28sp","32–40sp",1));
        }
    }

    @Override
    public void onBackPressed() {
        NavHelper.navigateBack(this);
    }

    // ── Inner class ──────────────────────────────────────────────────────────

    static class Question {
        private final String questionText;
        private final String option1, option2, option3, option4;
        private final int correctAnswer;

        public Question(String q, String a, String b, String c, String d, int correct) {
            this.questionText  = q;
            this.option1 = a; this.option2 = b;
            this.option3 = c; this.option4 = d;
            this.correctAnswer = correct;
        }

        public String getQuestionText() { return questionText; }
        public String getOption1()      { return option1; }
        public String getOption2()      { return option2; }
        public String getOption3()      { return option3; }
        public String getOption4()      { return option4; }
        public int    getCorrectAnswer(){ return correctAnswer; }
    }
}
