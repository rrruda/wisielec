package com.example.wisielec

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Ustawienia : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ustawienia)
        supportActionBar?.hide()

        val wylogujButton = findViewById<Button>(R.id.wylogujButton)
        val dodajKategorieButton = findViewById<Button>(R.id.dodajKategorieButton)
        val dodajSlowoButton = findViewById<Button>(R.id.dodajSlowoButton)

        wylogujButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "Wylogowano pomyślnie", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, ZalogujZarejestruj::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        dodajKategorieButton.setOnClickListener {
            val intent = Intent(this, DodajKategorie::class.java)
            startActivity(intent)
        }

        dodajSlowoButton.setOnClickListener {
            val intent = Intent(this, DodajSlowo::class.java)
            startActivity(intent)
        }
    }
}
