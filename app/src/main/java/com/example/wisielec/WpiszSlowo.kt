package com.example.wisielec

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class WpiszSlowo : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var kategoria: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wpiszslowo)
        supportActionBar?.hide()

        kategoria = intent.getStringExtra("kategoria") ?: return

        val kategoriaText = findViewById<TextView>(R.id.kategoriaNazwa)
        val slowoEdit = findViewById<EditText>(R.id.slowoEditText)
        val dodajBtn = findViewById<Button>(R.id.dodajSlowoButton)
        val pokazBtn = findViewById<Button>(R.id.pokazSlowaButton)
        val latwy = findViewById<RadioButton>(R.id.latwyRadio)
        val trudny = findViewById<RadioButton>(R.id.trudnyRadio)

        kategoriaText.text = "Dodajesz do: $kategoria"

        dodajBtn.setOnClickListener {
            val slowo = slowoEdit.text.toString().trim()
            val poziom = when {
                latwy.isChecked -> "Łatwy"
                trudny.isChecked -> "Trudny"
                else -> {
                    Toast.makeText(this, "Wybierz poziom!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            if (slowo.isEmpty()) {
                Toast.makeText(this, "Wpisz słowo!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val path = "kategorie/$userId/$kategoria.$poziom"
            val ref = db.collection("kategorie").document(userId!!)
            ref.update("$kategoria.$poziom", com.google.firebase.firestore.FieldValue.arrayUnion(slowo))
                .addOnSuccessListener {
                    Toast.makeText(this, "Dodano słowo!", Toast.LENGTH_SHORT).show()
                    slowoEdit.text.clear()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Błąd dodawania słowa", Toast.LENGTH_SHORT).show()
                }
        }

        pokazBtn.setOnClickListener {
            db.collection("kategorie").document(userId!!).get()
                .addOnSuccessListener { doc ->
                    val listaLatwy = (doc.get("$kategoria.Łatwy") as? List<*>)?.joinToString(", ") ?: ""
                    val listaTrudny = (doc.get("$kategoria.Trudny") as? List<*>)?.joinToString(", ") ?: ""
                    val wynik = "Łatwy:\n$listaLatwy\n\nTrudny:\n$listaTrudny"
                    AlertDialog.Builder(this)
                        .setTitle("Słowa w kategorii $kategoria")
                        .setMessage(wynik)
                        .setPositiveButton("OK", null)
                        .show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Błąd pobierania słów", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
