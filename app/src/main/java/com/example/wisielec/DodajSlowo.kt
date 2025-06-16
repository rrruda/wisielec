package com.example.wisielec

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DodajSlowo : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dodajslowo)
        supportActionBar?.hide()

        val layout = findViewById<LinearLayout>(R.id.kategorieContainer)

        if (userId == null) {
            Toast.makeText(this, "Brak użytkownika", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("kategorie").document(userId).get()
            .addOnSuccessListener { doc ->
                val kategorie = doc.data?.keys ?: emptySet()
                for (nazwa in kategorie) {
                    val btn = TextView(this).apply {
                        text = nazwa
                        textSize = 18f
                        setTextColor(Color.WHITE)
                        setBackgroundColor(Color.parseColor("#5D4037"))
                        setPadding(24, 24, 24, 24)
                        textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                        setOnClickListener {
                            // Przejście do WpiszSlowo z wybraną kategorią
                            val intent = Intent(this@DodajSlowo, WpiszSlowo::class.java)
                            intent.putExtra("kategoria", nazwa)
                            startActivity(intent)
                        }
                    }

                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.setMargins(0, 16, 0, 0)
                    layout.addView(btn, params)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Błąd podczas wczytywania kategorii", Toast.LENGTH_SHORT).show()
            }
    }
}
