package com.example.wisielec

class Slowa {
    val bazaSlow: Map<String, Map<String, List<String>>> = mapOf(
        "Zwierzęta" to mapOf(
            "Łatwy" to listOf("kot", "pies", "koń", "mysz", "kura"),
            "Trudny" to listOf("jeżozwierz", "szympans", "aligator", "pantera", "kangur")
        ),
        "Owoce" to mapOf(
            "Łatwy" to listOf("jabłko", "gruszka", "śliwka", "banan", "wiśnia"),
            "Trudny" to listOf("granat", "liczi", "ananas", "kumkwat", "mango")
        ),
        "Warzywa" to mapOf(
            "Łatwy" to listOf("marchew", "burak", "seler", "ogórek", "pomidor"),
            "Trudny" to listOf("karczoch", "brokuł", "papryka", "cebula", "batat")
        ),
        "Części ciała" to mapOf(
            "Łatwy" to listOf("ręka", "noga", "oko", "ucho", "nos"),
            "Trudny" to listOf("krtań", "przepona", "łopatka", "obojczyk", "kolano")
        ),
        "Państwa" to mapOf(
            "Łatwy" to listOf("Polska", "Niemcy", "Włochy", "Hiszpania", "Czechy"),
            "Trudny" to listOf("Egipt", "Kazachstan", "Mozambik", "Indonezja", "Andora")
        )
    )

    fun pobierzSlowo(kategoria: String, poziom: String): String? {
        return bazaSlow[kategoria]?.get(poziom)?.random()
    }
}
