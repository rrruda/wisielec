package com.example.wisielec

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Logowanie : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.logowanie)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.emailedittext)
        val passwordEditText = findViewById<EditText>(R.id.passwordedittext)
        val loginButton = findViewById<Button>(R.id.zalogujbutton2)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Zalogowano pomyślnie", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, EkranGlowny::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Błąd logowania: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Wpisz email i hasło", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
