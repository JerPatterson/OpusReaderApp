package com.transition.ora

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val ARG_CARD = "card"


class CardActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        enableBackButton()

        val card = Gson().fromJson(intent.getStringExtra(ARG_CARD), Card::class.java)
        if (card != null) {
            this.addCardInfoSection(card)
            this.addValiditySection(card)
            this.addFareInfoSection(card)
            this.addTripInfoSection(card)
        }
    }

    private fun addCardInfoSection(card: Card) {
        this.addCardInfoSectionTitles(card.type)
        this.addCardInfoSectionValues(card.id, card.expiryDate)
    }

    private fun addCardInfoSectionTitles(cardType: CardType) {
        val cardIdTitleTv = findViewById<TextView>(R.id.cardIdTv)
        val cardExpirationDateTitleTv = findViewById<TextView>(R.id.cardExpiryDateTv)
        cardIdTitleTv.text = getString(R.string.card_id_title)
        cardExpirationDateTitleTv.text = getString(R.string.card_expiration_date_title)

        val cardTitleTv = findViewById<TextView>(R.id.cardSectionTitleTv)
        cardTitleTv.text = when (cardType) {
            CardType.Opus -> getString(R.string.opus_card_name)
            CardType.Occasional -> getString(R.string.occasional_card_name)
        }

        val cardImageView = findViewById<ImageView>(R.id.cardImageView)
        when (cardType) {
            CardType.Opus -> cardImageView.setImageResource(R.drawable.opus)
            CardType.Occasional -> cardImageView.setImageResource(R.drawable.occasionnelle)
        }
    }

    private fun addCardInfoSectionValues(id: ULong, date: Calendar?) {
        val identifier = findViewById<TextView>(R.id.cardIdValueTv)
        val expiryDate = findViewById<TextView>(R.id.cardExpiryDateValueTv)

        identifier.text = id.toString()
        expiryDate.text = if (date != null) this.calendarToString(date) else getString(R.string.no_expiry_date)
    }

    private fun addValiditySection(card: Card) {
        if (this.addValidityInfoSectionValues(card)) {
            this.addValidityInfoSectionTitles()
        }
        else {
            hideValidityInfoSectionTitles()
        }
    }

    private fun addValidityInfoSectionTitles() {
        val validitySectionTitleTv = findViewById<TextView>(R.id.validitySectionTitleTv)
        validitySectionTitleTv.visibility = View.VISIBLE
        validitySectionTitleTv.text = getString(R.string.validity_section_title)
    }

    private fun hideValidityInfoSectionTitles() {
        val validitySectionTitleTv = findViewById<TextView>(R.id.validitySectionTitleTv)
        validitySectionTitleTv.visibility = View.GONE
    }

    private fun addValidityInfoSectionValues(card: Card): Boolean {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = supportFragmentManager.findFragmentById(R.id.validityFragment)
        if (fragment != null) fragmentTransaction.hide(fragment).commit()
        if (card.getTrips().isEmpty()) return false

        supportFragmentManager.beginTransaction()
            .add(R.id.validityFragment, ValidityFragment.newInstance(card)).commit()

        return true
    }

    private fun addFareInfoSection(card: Card) {
        if (this.addFareInfoSectionValues(card.id, card.getFares())) this.addFareInfoSectionTitles()
    }

    private fun addFareInfoSectionTitles() {
        val fareSectionTitleTv = findViewById<TextView>(R.id.fareSectionTitleTv)
        fareSectionTitleTv.text = getString(R.string.fare_section_title)
    }

    private fun addFareInfoSectionValues(id: ULong, fares: List<Fare>): Boolean {
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        var fragment = supportFragmentManager.findFragmentById(R.id.fourthFareFragment)
        if (fragment != null) fragmentTransaction.hide(fragment).commit()
        fragmentTransaction = supportFragmentManager.beginTransaction()
        fragment = supportFragmentManager.findFragmentById(R.id.thirdFareFragment)
        if (fragment != null) fragmentTransaction.hide(fragment).commit()
        fragmentTransaction = supportFragmentManager.beginTransaction()
        fragment = supportFragmentManager.findFragmentById(R.id.secondFareFragment)
        if (fragment != null) fragmentTransaction.hide(fragment).commit()
        fragmentTransaction = supportFragmentManager.beginTransaction()
        fragment = supportFragmentManager.findFragmentById(R.id.firstFareFragment)
        if (fragment != null) fragmentTransaction.hide(fragment).commit()

        var hasFare = false
        for ((i, fare) in fares.withIndex()) {
            hasFare = true
            when (i + 1) {
                1 -> supportFragmentManager.beginTransaction()
                    .add(R.id.firstFareFragment, FareFragment.newInstance(id, fare)).commit()
                2 -> supportFragmentManager.beginTransaction()
                    .add(R.id.secondFareFragment, FareFragment.newInstance(id, fare)).commit()
                3 -> supportFragmentManager.beginTransaction()
                    .add(R.id.thirdFareFragment, FareFragment.newInstance(id, fare)).commit()
                4 -> supportFragmentManager.beginTransaction()
                    .add(R.id.fourthFareFragment, FareFragment.newInstance(id, fare)).commit()
            }
        }

        return hasFare
    }


    private fun addTripInfoSection(card: Card) {
        if (this.addTripInfoSectionValues(card.id, card.getTrips()))
            this.addTripInfoSectionTitles()
    }

    private fun addTripInfoSectionTitles() {
        val tripSectionTitleTv = findViewById<TextView>(R.id.tripSectionTitleTv)
        tripSectionTitleTv.text = getString(R.string.trip_section_title)
    }

    private fun addTripInfoSectionValues(id: ULong, trips: List<Trip>): Boolean {
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        var fragment = supportFragmentManager.findFragmentById(R.id.firstTripFragment)
        if (fragment != null) fragmentTransaction.hide(fragment).commit()
        fragmentTransaction = supportFragmentManager.beginTransaction()
        fragment = supportFragmentManager.findFragmentById(R.id.secondTripFragment)
        if (fragment != null) fragmentTransaction.hide(fragment).commit()
        fragmentTransaction = supportFragmentManager.beginTransaction()
        fragment = supportFragmentManager.findFragmentById(R.id.thirdTripFragment)
        if (fragment != null) fragmentTransaction.hide(fragment).commit()

        var hasTrip = false
        for ((i, trip) in trips.withIndex()) {
            hasTrip = true
            when (i + 1) {
                1 -> supportFragmentManager.beginTransaction()
                    .add(R.id.firstTripFragment, TripFragment.newInstance(id, trip)).commit()
                2 -> supportFragmentManager.beginTransaction()
                    .add(R.id.secondTripFragment, TripFragment.newInstance(id, trip)).commit()
                3 -> supportFragmentManager.beginTransaction()
                    .add(R.id.thirdTripFragment, TripFragment.newInstance(id, trip)).commit()
            }
        }

        return hasTrip
    }


    private fun calendarToString(cal: Calendar): String {
        return SimpleDateFormat(
            getString(R.string.calendar_pattern),
            Locale(getString(R.string.calendar_language), getString(R.string.calendar_country))
        ).format(cal.time)
    }

    private fun enableBackButton() {
        val backButton: ImageView = findViewById(R.id.cardActivityBack)
        backButton.setOnClickListener(HistoryBackListener(this))
    }

    class HistoryBackListener(
        private val activity: CardActivity,
    ) : View.OnClickListener {
        override fun onClick(view: View) {
            activity.finish()
        }
    }
}