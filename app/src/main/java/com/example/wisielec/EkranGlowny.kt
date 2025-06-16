package com.example.wisielec

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class EkranGlowny : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ekranglowny)
        supportActionBar?.hide()

        val grajButton = findViewById<Button>(R.id.grajbutton)
        val zasadyButton = findViewById<Button>(R.id.zasadybutton)
        val ustawieniaButton = findViewById<Button>(R.id.ustawieniaButton)

        grajButton.setOnClickListener {
            val intent = Intent(this, PoziomKategoria::class.java)
            startActivity(intent)
        }

        zasadyButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Zasady gry")
            builder.setMessage(
                "• Gra polega na odgadywaniu ukrytego słowa – litera po literze.\n" +
                        "• Na początku widzisz tylko puste pola – każda kreska to jedna litera.\n" +
                        "• W każdej turze wybierasz jedną literę z alfabetu.\n" +
                        "• Jeśli litera znajduje się w haśle – zostaje odkryta na właściwych miejscach.\n" +
                        "• Jeśli litery nie ma – tracisz jedną próbę i rysowana jest kolejna część wisielca.\n" +
                        "• Gra kończy się, gdy:\n" +
                        "   – odgadniesz całe słowo – WYGRYWASZ\n" +
                        "   – skończą Ci się próby – PRZEGRYWASZ\n" +
                        "• Masz ograniczoną liczbę pomyłek – 7\n" +
                        "• Nie możesz zgadywać tej samej litery dwa razy.\n" +
                        "• Przycisk 'Podpowiedź' odsłania jedną literę, ale zabiera jedno życie.\n" +
                        "Powodzenia!"
            )
            builder.setPositiveButton("Zamknij") { dialog, _ -> dialog.dismiss() }
            builder.create().show()
        }

        ustawieniaButton.setOnClickListener {
            val intent = Intent(this, Ustawienia::class.java)
            startActivity(intent)
        }
    }
}
