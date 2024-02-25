package com.example.opusreader

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import java.util.Calendar

private const val ARG_CARD = "card"

class CardActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        val card = Gson().fromJson(intent.getStringExtra(ARG_CARD), Card::class.java)
        if (card != null) {
            this.addCardInfoSection(card)
            this.addFareInfoSection(card)
            this.addTripInfoSection(card)
        }
    }

    private fun addCardInfoSection(card: Card) {
        this.addCardInfoSectionTitles(card.type)
        this.addCardInfoSectionValues(card.id, card.expiryDate)
    }

    private fun addCardInfoSectionTitles(cardType: CardType) {
        val title = findViewById<TextView>(R.id.cardSectionTitleTv)
        val image = findViewById<ImageView>(R.id.cardImageView)
        val identifier = findViewById<TextView>(R.id.cardIdTv)
        val expirationDate = findViewById<TextView>(R.id.cardExpiryDateTv)

        title.text = when (cardType) {
            CardType.Opus -> "CARTE OPUS"
            CardType.Occasional -> "CARTE OCCASIONNELLE"
        }

        when (cardType) {
            CardType.Opus -> image.setImageResource(R.drawable.opus)
            CardType.Occasional -> image.setImageResource(R.drawable.occasionnelle)
        }

        identifier.text = "Identifiant"
        expirationDate.text = "Date d'expiration"
    }

    private fun addCardInfoSectionValues(id: ULong, date: Calendar?) {
        val identifier = findViewById<TextView>(R.id.cardIdValueTv)
        val expiryDate = findViewById<TextView>(R.id.cardExpiryDateValueTv)

        identifier.text = id.toString()
        expiryDate.text = if (date != null) this.calendarToString(date) else "Aucune"
    }

    private fun addFareInfoSection(card: Card) {
        this.addFareInfoSectionTitles()
        this.addFareInfoSectionValues(card.fares)
    }

    private fun addFareInfoSectionTitles() {
        val title = findViewById<TextView>(R.id.fareSectionTitleTv)
        title.text = "Titres de la carte"
    }

    private fun addFareInfoSectionValues(fares: ArrayList<Fare>) {
        if (fares.size < 4 || fares[3].typeId == 0u) {
            var fragmentTransaction = supportFragmentManager.beginTransaction()
            var fragment = supportFragmentManager.findFragmentById(R.id.fourthFareFragment)
            if (fragment != null) fragmentTransaction.hide(fragment)
            fragmentTransaction.commit()

            if (fares.size < 3 || fares[2].typeId == 0u) {
                fragmentTransaction = supportFragmentManager.beginTransaction()
                fragment = supportFragmentManager.findFragmentById(R.id.thirdFareFragment)
                if (fragment != null) fragmentTransaction.hide(fragment)
                fragmentTransaction.commit()

                if (fares.size < 2 || fares[1].typeId == 0u) {
                    fragmentTransaction = supportFragmentManager.beginTransaction()
                    fragment = supportFragmentManager.findFragmentById(R.id.secondFareFragment)
                    if (fragment != null) fragmentTransaction.hide(fragment)
                    fragmentTransaction.commit()
                }
            }
        }

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


    private fun addTripInfoSection(card: Card) {
        this.addTripInfoSectionTitles()
        this.addTripInfoSectionValues(card.trips)
    }

    private fun addTripInfoSectionTitles() {
        val title = findViewById<TextView>(R.id.tripSectionTitleTv)
        title.text = "Dernières utilisations"
    }

    private fun addTripInfoSectionValues(trips: ArrayList<Trip>) {
        if (trips.size < 3 || trips[2].operatorId == 0u) {
            var fragmentTransaction = supportFragmentManager.beginTransaction()
            var fragment = supportFragmentManager.findFragmentById(R.id.thirdTripFragment)
            if (fragment != null) fragmentTransaction.hide(fragment)
            fragmentTransaction.commit()

            if (trips.size < 2 || trips[1].operatorId == 0u) {
                fragmentTransaction = supportFragmentManager.beginTransaction()
                fragment = supportFragmentManager.findFragmentById(R.id.secondTripFragment)
                if (fragment != null) fragmentTransaction.hide(fragment)
                fragmentTransaction.commit()
            }
        }

        for ((i, trip) in trips.withIndex()) {
            when (i + 1) {
                1 -> supportFragmentManager.beginTransaction()
                    .add(R.id.firstTripFragment, TripFragment.newInstance(trip)).commit()
                2 -> supportFragmentManager.beginTransaction()
                    .add(R.id.secondTripFragment, TripFragment.newInstance(trip)).commit()
                3 -> supportFragmentManager.beginTransaction()
                    .add(R.id.thirdTripFragment, TripFragment.newInstance(trip)).commit()
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