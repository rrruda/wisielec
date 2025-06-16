package com.example.wisielec

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Rejestracja : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rejestracja)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.emailedittext)
        val passwordEditText = findViewById<EditText>(R.id.passwordedittext)
        val registerButton = findViewById<Button>(R.id.zarejestrujbutton2)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Rejestracja zakończona sukcesem", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, EkranGlowny::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Błąd: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Wpisz email i hasło", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
