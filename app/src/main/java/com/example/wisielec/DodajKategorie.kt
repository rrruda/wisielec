package com.example.wisielec

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class DodajKategorie : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dodajkategorie)
        supportActionBar?.hide()

        db = FirebaseFirestore.getInstance()
        userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            Toast.makeText(this, "Musisz być zalogowany!", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        val input = findViewById<EditText>(R.id.kategoriaEditText)
        val dodajBtn = findViewById<Button>(R.id.dodajKategorieButton)
        val pokazBtn = findViewById<Button>(R.id.pokazKategorieButton)

        dodajBtn.setOnClickListener {
            val nowa = input.text.toString().trim()

            if (nowa.isEmpty()) {
                Toast.makeText(this, "Wpisz nazwę kategorii!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val mapa = hashMapOf(nowa to mapOf("Łatwy" to emptyList<String>(), "Trudny" to emptyList<String>()))

            db.collection("kategorie").document(userId!!)
                .set(mapa, SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(this, "Dodano kategorię: $nowa", Toast.LENGTH_SHORT).show()
                    input.text.clear()

                    // Powrót do ekranu głównego po krótkim czasie
                    input.postDelayed({
                        val intent = Intent(this, EkranGlowny::class.java)
                        startActivity(intent)
                        finish()
                    }, 1500) // 1,5 sekundy opóźnienia
                }
                .addOnFailureListener { e ->
                    Log.e("DodajKategorie", "Błąd dodawania", e)
                    Toast.makeText(this, "Błąd dodawania: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }

        pokazBtn.setOnClickListener {
            db.collection("kategorie").document(userId!!).get()
                .addOnSuccessListener { doc ->
                    val kategorie = doc.data?.keys?.joinToString("\n") ?: "Brak kategorii"
                    AlertDialog.Builder(this)
                        .setTitle("Kategorie")
                        .setMessage(kategorie)
                        .setPositiveButton("OK", null)
                        .show()
                }
                .addOnFailureListener { e ->
                    Log.e("DodajKategorie", "Błąd pobierania", e)
                    Toast.makeText(this, "Błąd podczas pobierania: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}
