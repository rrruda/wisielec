package com.example.wisielec

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PoziomKategoria : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var layout: LinearLayout
    private val checkBoxes = mutableListOf<Pair<String, CheckBox>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.poziomkategoria)
        supportActionBar?.hide()

        val grajButton = findViewById<Button>(R.id.grajButton)
        val latwy = findViewById<RadioButton>(R.id.latwyButton)
        val trudny = findViewById<RadioButton>(R.id.trudnyButton)
        layout = findViewById(R.id.kategorieLayout)
        layout.removeAllViews()

        // Wbudowane kategorie
        val wbudowane = listOf("Zwierzęta", "Owoce", "Warzywa", "Części ciała", "Państwa")
        for (nazwa in wbudowane) {
            dodajCheckbox(nazwa)
        }

        // Kategorie z Firebase
        if (userId != null) {
            db.collection("kategorie").document(userId).get()
                .addOnSuccessListener { doc ->
                    doc.data?.keys?.forEach { nazwa ->
                        if (!wbudowane.contains(nazwa)) {
                            dodajCheckbox(nazwa)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("PoziomKategoria", "Błąd pobierania kategorii: ${e.message}")
                    Toast.makeText(this, "Błąd pobierania kategorii", Toast.LENGTH_SHORT).show()
                }
        }

        grajButton.setOnClickListener {
            val poziom = when {
                latwy.isChecked -> "Łatwy"
                trudny.isChecked -> "Trudny"
                else -> ""
            }

            val wybraneKategorie = checkBoxes.filter { it.second.isChecked }.map { it.first }

            if (poziom.isEmpty() || wybraneKategorie.isEmpty()) {
                Toast.makeText(this, "Wybierz poziom i kategorię!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, Gra::class.java).apply {
                putExtra("poziom", poziom)
                putStringArrayListExtra("kategorie", ArrayList(wybraneKategorie))
            }
            startActivity(intent)
        }
    }

    private fun dodajCheckbox(nazwa: String) {
        val checkBox = CheckBox(this)
        checkBox.text = nazwa
        checkBox.textSize = 16f
        layout.addView(checkBox)
        checkBoxes.add(nazwa to checkBox)
    }
}
