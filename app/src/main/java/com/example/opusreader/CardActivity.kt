package com.example.opusreader

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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
            this.addFareInfoSection(card)
        }
    }

    private fun addCardInfoSection(card: Card) {
        this.addCardInfoSectionTitles(card.type)
        this.addCardInfoSectionValues(card.id, card.expiryDate)
    }

    private fun addCardInfoSectionTitles(cardType: CardType) {
        val title = findViewById<TextView>(R.id.cardSectionTitleTv)
        val identifier = findViewById<TextView>(R.id.idTv)
        val expirationDate = findViewById<TextView>(R.id.expiryDateTv)

        title.text = if (cardType == CardType.Opus) "CARTE OPUS" else "CARTE OCCASIONNELLE"
        identifier.text = "Identifiant"
        expirationDate.text = "Date d'expiration"
    }

    private fun addCardInfoSectionValues(id: ULong, date: Calendar?) {
        val identifier = findViewById<TextView>(R.id.idValueTv)
        val expiryDate = findViewById<TextView>(R.id.expiryDateValueTv)

        identifier.text = id.toString()
        expiryDate.text = if (date != null) this.calendarToString(date) else "Aucune"
    }

    private fun addFareInfoSection(card: Card) {
        this.addFareInfoSectionTitles()
        this.addFareInfoSectionValues(card.fares)
    }

    private fun addFareInfoSectionTitles() {
        val title = findViewById<TextView>(R.id.fareSectionTitleTv)
        title.text = "TITRES DE LA CARTE"
    }

    private fun addFareInfoSectionValues(fares: ArrayList<Fare>) {
        for ((i, fare) in fares.withIndex()) {
            when (i + 1) {
                1 -> supportFragmentManager.beginTransaction()
                    .add(R.id.firstFareFragment, FareFragment.newInstance(fare)).commit()
                2 -> supportFragmentManager.beginTransaction()
                    .add(R.id.secondFareFragment, FareFragment.newInstance(fare)).commit()
                3 -> supportFragmentManager.beginTransaction()
                    .add(R.id.thirdFareFragment, FareFragment.newInstance(fare)).commit()
                4 -> supportFragmentManager.beginTransaction()
                    .add(R.id.fourthFareFragment, FareFragment.newInstance(fare)).commit()
            }
        }
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