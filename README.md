# EduSmart: Mobile Learning Management System (LMS)

EduSmart adalah sebuah platform *Mobile Learning Management System* (LMS) berbasis Android Native yang dirancang untuk menjadi solusi pembelajaran digital yang praktis, responsif, dan stabil. Proyek ini dikembangkan menggunakan arsitektur koding yang bersih (*clean code*), berorientasi pada kenyamanan visual pengguna (*user-centric design*), serta memiliki ketahanan penuh terhadap distorsi visual yang kerap terjadi pada perangkat Android modern.

---

## 🚀 Fitur Utama

* **Splash Screen & Modern Bento Dashboard:** Tampilan awal yang minimalis dipadukan dengan konsep *Bento-Box Grid Layout* yang membagi fungsionalitas menu secara simetris, lengkap dengan sapaan pengguna dinamis (*Dynamic Greeting Header*).
* **Pusat Materi Digital Terstruktur:** Repositori materi perkuliahan Android yang dikemas dalam komponen kartu interaktif, dioptimalkan khusus untuk kenyamanan membaca dalam durasi lama (*Readability Focus*).
* **Sistem Evaluasi Kuis Interaktif:** Fitur ujian mandiri dengan alur bertingkat berdasarkan kategori (Coding, UI/UX, Desain Grafis) dilengkapi dengan perhitungan skor dinamis dan umpan balik instan.
* **Manajemen Profil & Persistensi Data:** Formulir pembaruan identitas (Nama Lengkap & NIM) yang tervalidasi dan tersinkronisasi secara *real-time* ke halaman utama menggunakan penyimpanan lokal.

---

## 🛠️ Spesifikasi Teknis & Arsitektur

Aplikasi ini dibangun secara *native* dengan mengoptimalkan komponen internal Android SDK untuk menjamin performa yang ringan dan stabil:

* **Bahasa Pemrograman:** Java (Android SDK Native)
* **IDE:** Android Studio
* **Penyimpanan Data Lokal:** `SharedPreferences` (digunakan untuk memelihara persistensi sesi data profil pengguna secara persisten agar tidak hilang saat aplikasi ditutup).
* **Manajemen UI & Tata Letak:** XML Layout (`LinearLayout`, `ConstraintLayout`, `CardView`, `Material Components`).
* **Logika Navigasi:** `Intent` & *Activity Lifecycle Management* (`onCreate`, `onDestroy`) untuk menjamin perpindahan halaman yang mulus tanpa *memory leak*.

## 📂 Struktur Direktori Proyek

```text
EduSmart/
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/projekuts/
│   │   │   │   ├── MainActivity.java (Splash Screen)
│   │   │   │   ├── DashboardActivity.java (Menu Utama & Logic Load Data)
│   │   │   │   ├── MateriActivity.java (Pusat Materi)
│   │   │   │   ├── DetailMateriActivity.java (Artikel Bacaan)
│   │   │   │   ├── QuizCategoryActivity.java (Seleksi Kategori Kuis)
│   │   │   │   ├── QuizActivity.java (Alur Pertanyaan & Scoring)
│   │   │   │   └── ProfilActivity.java (Edit Data & Session Management)
│   │   │   │
│   │   │   └── res/
│   │   │       ├── layout/ (Berkas Antarmuka XML)
│   │   │       ├── values/
│   │   │       │   ├── colors.xml (Variabel Hex Warna Mengunci)
│   │   │       │   └── themes.xml (Konfigurasi Global Light Theme)
│   │   │       └── drawable/ (Aset Ilustrasi & Background Rounded)
│   │   │
│   │   └── AndroidManifest.xml (Konfigurasi Aplikasi & Alur Launcher)
│   │
│   └── build.gradle (Dependensi Gradle)
└── README.md
