package com.example.projekuts.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "nama")
    public String nama;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "password")
    public String password; // disimpan sebagai plain text (bisa di-hash kalau mau lebih aman)

    // Constructor
    public User(String nama, String email, String password) {
        this.nama     = nama;
        this.email    = email;
        this.password = password;
    }
}
