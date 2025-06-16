package com.example.wisielec

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ZalogujZarejestruj : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zalogujzarejestruj)
        supportActionBar?.hide()

        val loginButton = findViewById<Button>(R.id.zalogujbutton)
        val registerButton = findViewById<Button>(R.id.zarejestrujbutton)

        loginButton.setOnClickListener {
            val intent = Intent(this, Logowanie::class.java)
            startActivity(intent)
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, Rejestracja::class.java)
            startActivity(intent)
        }
    }
}
