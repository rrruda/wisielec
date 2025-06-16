package com.example.wisielec

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class Gra : AppCompatActivity() {

    private lateinit var slowo: String
    private lateinit var poziom: String
    private lateinit var kategorieList: List<String>

    private lateinit var hasloTextView: TextView
    private lateinit var probyTextView: TextView
    private lateinit var wisielecImage: ImageView
    private lateinit var restartButton: Button
    private lateinit var podpowiedzButton: Button
    private lateinit var kategoriaPoziomTextView: TextView

    private val uzyteLitery = mutableSetOf<Char>()
    private val przyciskiMap = mutableMapOf<Char, Button>()
    private var bledy = 0
    private val maksBledy = 6

    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gra)
        supportActionBar?.hide()

        // UI
        hasloTextView = findViewById(R.id.hasloTextView)
        probyTextView = findViewById(R.id.probyTextView)
        wisielecImage = findViewById(R.id.wisielecImage)
        restartButton = findViewById(R.id.restartButton)
        podpowiedzButton = findViewById(R.id.podpowiedzButton)
        kategoriaPoziomTextView = findViewById(R.id.kategoriaPoziomTextView)

        // Dane z poprzedniego ekranu
        poziom = intent.getStringExtra("poziom") ?: ""
        kategorieList = intent.getStringArrayListExtra("kategorie") ?: listOf()

        startNowejGry()

        restartButton.setOnClickListener { startNowejGry() }

        podpowiedzButton.setOnClickListener { dajPodpowiedz() }
    }

    private fun startNowejGry() {
        bledy = 0
        uzyteLitery.clear()
        ustawObrazek(1)
        probyTextView.text = "Pozostało prób: ${maksBledy - bledy}"

        if (userId == null) {
            Toast.makeText(this, "Błąd: brak użytkownika", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val wybranaKategoria = kategorieList.random()
        kategoriaPoziomTextView.text = "Poziom: $poziom\nKategoria: $wybranaKategoria"

        // Pobierz słowo z Firestore
        db.collection("kategorie").document(userId!!).get()
            .addOnSuccessListener { doc ->
                val slowa = (doc.get("$wybranaKategoria.$poziom") as? List<*>)?.filterIsInstance<String>() ?: emptyList()

                slowo = if (slowa.isNotEmpty()) {
                    slowa.random().uppercase()
                } else {
                    // fallback na lokalne słowa
                    Slowa().pobierzSlowo(wybranaKategoria, poziom)?.uppercase() ?: ""
                }

                if (slowo.isEmpty()) {
                    pokazAlert("Błąd", "Brak słów do gry w tej kategorii. Wróć i wybierz inną.")
                    return@addOnSuccessListener
                }

                aktualizujHaslo()
                generujKlawiature()
            }
            .addOnFailureListener {
                pokazAlert("Błąd", "Nie udało się pobrać danych. Spróbuj ponownie.")
            }
    }

    private fun generujKlawiature() {
        val qwerty = listOf(
            "WEĘRTYUIOÓP",
            "AĄSŚDFGHJKLŁ",
            "ZŻŹCĆBNŃM"
        )

        val rowIds = listOf(R.id.row1, R.id.row2, R.id.row3)
        przyciskiMap.clear()

        for (i in qwerty.indices) {
            val rowLayout = findViewById<LinearLayout>(rowIds[i])
            rowLayout.removeAllViews()

            for (char in qwerty[i]) {
                val button = Button(this).apply {
                    text = char.toString()
                    textSize = 14f
                    isAllCaps = false
                    setBackgroundColor(Color.parseColor("#EDE4D5"))
                    setTextColor(Color.parseColor("#4E342E"))
                    setPadding(0, 12, 0, 12)
                    setOnClickListener { obsluzKlikniecieLitery(char, this) }
                }

                val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                params.setMargins(4, 4, 4, 4)
                rowLayout.addView(button, params)

                przyciskiMap[char] = button
            }
        }
    }

    private fun obsluzKlikniecieLitery(litera: Char, button: Button) {
        if (uzyteLitery.contains(litera)) return

        uzyteLitery.add(litera)
        button.isEnabled = false
        button.alpha = 0.3f

        if (slowo.contains(litera)) {
            aktualizujHaslo()
            if (!hasloTextView.text.contains("_")) {
                pokazAlert("WYGRAŁEŚ!", "Gratulacje! Odgadłeś słowo: $slowo")
            }
        } else {
            bledy++
            probyTextView.text = "Pozostało prób: ${maksBledy - bledy}"
            ustawObrazek(bledy + 1)
            if (bledy >= maksBledy) {
                hasloTextView.text = slowo.toCharArray().joinToString(" ")
                pokazAlert("PRZEGRAŁEŚ!", "Słowo to: $slowo")
            }
        }
    }

    private fun aktualizujHaslo() {
        val pokaz = slowo.map { if (uzyteLitery.contains(it)) "$it " else "_ " }.joinToString("")
        hasloTextView.text = pokaz.trim()
    }

    private fun dajPodpowiedz() {
        val nieodkryte = slowo.filter { !uzyteLitery.contains(it) }
        if (nieodkryte.isNotEmpty()) {
            val litera = nieodkryte.first()
            uzyteLitery.add(litera)
            przyciskiMap[litera]?.apply {
                isEnabled = false
                alpha = 0.3f
            }

            aktualizujHaslo()
            bledy++
            ustawObrazek(bledy + 1)
            probyTextView.text = "Pozostało prób: ${maksBledy - bledy}"

            if (!hasloTextView.text.contains("_")) {
                pokazAlert("WYGRAŁEŚ!", "Gratulacje! Odgadłeś słowo: $slowo")
            } else if (bledy >= maksBledy) {
                hasloTextView.text = slowo.toCharArray().joinToString(" ")
                pokazAlert("PRZEGRAŁEŚ!", "Słowo to: $slowo")
            }
        }
    }

    private fun ustawObrazek(nr: Int) {
        if (nr in 1..7) {
            val resId = resources.getIdentifier("wisielec$nr", "drawable", packageName)
            wisielecImage.setImageResource(resId)
        }
    }

    private fun pokazAlert(tytul: String, wiadomosc: String) {
        AlertDialog.Builder(this)
            .setTitle(tytul)
            .setMessage(wiadomosc)
            .setCancelable(false)
            .setPositiveButton("Zagraj ponownie") { _, _ -> startNowejGry() }
            .setNegativeButton("Wyjdź") { _, _ -> finish() }
            .show()
    }
}
