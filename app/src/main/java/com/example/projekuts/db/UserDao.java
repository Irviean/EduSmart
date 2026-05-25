package com.example.projekuts.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {

    // Daftar akun baru
    @Insert
    void register(User user);

    // Login: cari user berdasarkan email + password
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    User login(String email, String password);

    // Cek apakah email sudah terdaftar (untuk cegah duplikat)
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User cariByEmail(String email);
}
