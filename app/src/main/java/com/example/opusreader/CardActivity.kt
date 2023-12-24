package com.example.opusreader

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import java.util.Calendar

class CardActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        val gson = Gson()
        val card = gson.fromJson(intent.getStringExtra("card"), Card::class.java)
        if (card != null) {
            this.addCardInfoSection(card)
        }
    }

    private fun addCardInfoSection(card: Card) {
        this.addCardInfoSectionTitles(card.type)
        val identifier = findViewById<TextView>(R.id.idValueTv)
        val expiryDate = findViewById<TextView>(R.id.expiryDateValueTv)

        identifier.text = card.id.toString()
        val calendar = card.expiryDate
        expiryDate.text = if (calendar != null) this.calendarToString(calendar) else "Aucune"
    }

    private fun addCardInfoSectionTitles(cardType: CardType) {
        val type = findViewById<TextView>(R.id.cardSectionTitleTv)
        val identifier = findViewById<TextView>(R.id.idTv)
        val expirationDate = findViewById<TextView>(R.id.expiryDateTv)

        type.text = if (cardType == CardType.Opus) "CARTE OPUS" else "CARTE OCCASIONNELLE"
        identifier.text = "Identifiant"
        expirationDate.text = "Date d'expiration"
    }

    private fun calendarToString(cal: Calendar): String {
        return String.format(
            "%04d-%02d-%02d",
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH).inc(),
            cal.get(Calendar.DATE)
        )
    }
}