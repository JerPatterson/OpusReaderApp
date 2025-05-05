package com.transition.ora

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
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
        this.addCardRegisteredInfo(card.type, card.birthDate)
        this.addCardTypeVariantInfo(card.typeVariant)
        this.addCardInfoSectionValues(card.id, card.expiryDate, card.birthDate)

        val overlayLayout = findViewById<FrameLayout>(R.id.overlay)
        overlayLayout.setOnClickListener(OverlayListener(this))
        overlayLayout.visibility = View.GONE
    }

    private fun addCardRegisteredInfo(cardType: CardType, birthDate: Calendar?) {
        val registeredCardButton = findViewById<CardView>(R.id.registeredButtonLayout)
        if (cardType == CardType.Occasional || birthDate == null) {
            registeredCardButton.visibility = View.GONE
        } else {
            registeredCardButton.visibility = View.VISIBLE
            registeredCardButton.setOnClickListener(RegisteredCardListener(this))
        }
    }

    private fun addCardTypeVariantInfo(cardTypeVariantId: UInt?) {
        val cardTypeVariant = CardContentConverter.getCardTypeVariantById(cardTypeVariantId)
        when (cardTypeVariant) {
            CardTypeVariant.Standard,
            CardTypeVariant.StandardReduced -> {
                addSpecificCardTypeVariantInfo(R.string.standard_card, R.string.standard_card_info)
            }
            CardTypeVariant.AllModesAB,
            CardTypeVariant.AllModesABReduced -> {
                addSpecificCardTypeVariantInfo(R.string.all_modes_AB_card, R.string.all_modes_AB_card_info)
            }
            CardTypeVariant.AllModesABC,
            CardTypeVariant.AllModesABCReduced -> {
                addSpecificCardTypeVariantInfo(R.string.all_modes_ABC_card, R.string.all_modes_ABC_card_info)
            }
            CardTypeVariant.AllModesABCD,
            CardTypeVariant.AllModesABCDReduced -> {
                addSpecificCardTypeVariantInfo(R.string.all_modes_ABCD_card, R.string.all_modes_ABCD_card_info)
            }
            CardTypeVariant.BusOutOfTerritory,
            CardTypeVariant.BusOutOfTerritoryReduced -> {
                addSpecificCardTypeVariantInfo(R.string.bus_out_territory_card, R.string.bus_out_territory_card_info)
            }

            else -> {
                val cardVariantTypeButton = findViewById<CardView>(R.id.cardTypeButtonLayout)
                cardVariantTypeButton.visibility = View.GONE
            }
        }
    }

    private fun addSpecificCardTypeVariantInfo(buttonText: Int, descriptionText: Int) {
        val cardVariantTypeTv = findViewById<TextView>(R.id.cardTypeValueTv)
        val cardVariantTypeDescriptionTv = findViewById<TextView>(R.id.cardTypeDescriptionTv)
        cardVariantTypeTv.text = getString(buttonText)
        cardVariantTypeDescriptionTv.text = getString(descriptionText)

        val cardVariantTypeButton = findViewById<CardView>(R.id.cardTypeButtonLayout)
        cardVariantTypeButton.visibility = View.VISIBLE
        cardVariantTypeButton.setOnClickListener(CardTypeVariantListener(this))
    }

    private fun addCardInfoSectionTitles(cardType: CardType) {
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

    private fun addCardInfoSectionValues(id: ULong, expiryDate: Calendar?, birthDate: Calendar?) {
        val identifier = findViewById<TextView>(R.id.cardIdValueTv)
        val expiry = findViewById<TextView>(R.id.cardExpiryDateValueTv)
        val birth = findViewById<TextView>(R.id.cardBirthDateValueTv)

        identifier.text = id.toString()
        expiry.text = if (expiryDate != null) this.calendarToString(expiryDate) else getString(R.string.no_expiry_date)
        birth.text = if (birthDate != null) this.calendarToString(birthDate) else getString(R.string.no_birth_date)
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

    class RegisteredCardListener(
        private val activity: CardActivity,
    ) : View.OnClickListener {
        override fun onClick(view: View) {
            val overlayLayout = activity.findViewById<FrameLayout>(R.id.overlay)
            val cardTypeVariantLayout = activity.findViewById<CardView>(R.id.cardTypeLayout)
            val registeredCardLayout = activity.findViewById<CardView>(R.id.registeredLayout)

            overlayLayout.visibility = View.VISIBLE
            cardTypeVariantLayout.visibility = View.GONE
            registeredCardLayout.visibility = View.VISIBLE
        }
    }

    class CardTypeVariantListener(
        private val activity: CardActivity,
    ) : View.OnClickListener {
        override fun onClick(view: View) {
            val overlayLayout = activity.findViewById<FrameLayout>(R.id.overlay)
            val cardTypeVariantLayout = activity.findViewById<CardView>(R.id.cardTypeLayout)
            val registeredCardLayout = activity.findViewById<CardView>(R.id.registeredLayout)

            overlayLayout.visibility = View.VISIBLE
            cardTypeVariantLayout.visibility = View.VISIBLE
            registeredCardLayout.visibility = View.GONE
        }
    }

    class OverlayListener(
        private val activity: CardActivity,
    ) : View.OnClickListener {
        override fun onClick(view: View) {
            val overlayLayout = activity.findViewById<FrameLayout>(R.id.overlay)
            val cardTypeVariantLayout = activity.findViewById<CardView>(R.id.cardTypeLayout)
            val registeredCardLayout = activity.findViewById<CardView>(R.id.registeredLayout)

            overlayLayout.visibility = View.GONE
            cardTypeVariantLayout.visibility = View.GONE
            registeredCardLayout.visibility = View.GONE
        }
    }
}