package com.transition.ora

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import com.transition.ora.adapters.TypeCrowdSrcAdapter
import com.transition.ora.database.CardDatabase
import com.transition.ora.database.entities.CardPropositionEntity
import com.transition.ora.enums.CardType
import com.transition.ora.enums.CardTypeVariant
import com.transition.ora.fragments.FareFragment
import com.transition.ora.fragments.TripFragment
import com.transition.ora.fragments.ValidityFragment
import com.transition.ora.services.CardContentConverter
import com.transition.ora.services.NotificationScheduler
import com.transition.ora.types.Card
import com.transition.ora.types.Fare
import com.transition.ora.types.Trip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val ARG_CARD = "card"


class CardActivity : AppCompatActivity() {
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences("permission", MODE_PRIVATE)
    }

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

            this.scheduleFareNotification(card)
        }
    }

    private fun addCardInfoSection(card: Card) {
        this.addCardInfoSectionTitles(card.type)
        this.addCardRegisteredInfo(card.type, card.expiryDate, card.birthDate)
        this.addCardTypeVariantInfo(card.id, card.typeVariant)
        this.addCardInfoSectionValues(card.id, card.expiryDate, card.birthDate)

        val overlayLayout = findViewById<FrameLayout>(R.id.overlay)
        overlayLayout.setOnClickListener(OverlayListener(this))
        overlayLayout.visibility = View.GONE
    }

    private fun addCardRegisteredInfo(cardType: CardType, expiryDate: Calendar?, birthDate: Calendar?) {
        val cardStatusButton = findViewById<CardView>(R.id.cardStatusButtonLayout)
        cardStatusButton.setOnClickListener(RegisteredCardListener(this))

        if (cardType == CardType.Occasional) {
            cardStatusButton.visibility = View.GONE
        } else if (cardType == CardType.Opus) {
            val now = Calendar.getInstance()
            val cardStatusTv = findViewById<TextView>(R.id.cardStatusTv)
            val cardStatusInfoTitleTv = findViewById<TextView>(R.id.cardStatusInfoTitleTv)
            val cardStatusDescriptionTv = findViewById<TextView>(R.id.cardStatusDescriptionTv)

            if (birthDate != null && expiryDate != null && expiryDate.timeInMillis >= now.timeInMillis) {
                cardStatusTv.text = getString(R.string.registered_card_title)
                cardStatusInfoTitleTv.text = getString(R.string.registered_card_info)
                cardStatusDescriptionTv.text = getString(R.string.registered_card_description)
                cardStatusButton.visibility = View.VISIBLE
            } else if (expiryDate != null && expiryDate.timeInMillis < now.timeInMillis) {
                cardStatusTv.text = getString(R.string.expired_card_title)
                cardStatusInfoTitleTv.text = getString(R.string.expired_card_info)
                cardStatusDescriptionTv.text = getString(R.string.expired_card_description)
                cardStatusButton.visibility = View.VISIBLE
            } else {
                cardStatusButton.visibility = View.GONE
            }
        }
    }

    private fun addCardTypeVariantInfo(cardId: ULong, cardTypeVariantId: UInt?) {
        if (cardTypeVariantId == null || cardTypeVariantId == 0u) {
            val cardTypeVariantButton = findViewById<CardView>(R.id.cardTypeButtonLayout)
            cardTypeVariantButton.visibility = View.GONE
            return
        }

        val cardTypeVariant = CardContentConverter.getCardTypeVariantById(this, cardTypeVariantId)
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
                addSpecificCardTypeVariantInfo(R.string.unknown_card, R.string.unknown_card_info)
            }
        }

        enableCrowdSourceConfirmButton(cardId, cardTypeVariantId)
    }

    private fun addSpecificCardTypeVariantInfo(buttonText: Int, descriptionText: Int) {
        val cardVariantTypeTv = findViewById<TextView>(R.id.cardTypeValueTv)
        val cardVariantTypeDescriptionTv = findViewById<TextView>(R.id.cardTypeDescriptionTv)
        cardVariantTypeTv.text = getString(buttonText)
        cardVariantTypeDescriptionTv.text = getString(descriptionText)

        val cardVariantTypeButton = findViewById<CardView>(R.id.cardTypeButtonLayout)
        cardVariantTypeButton.visibility = View.VISIBLE
        cardVariantTypeButton.setOnClickListener(CardTypeVariantListener(this))

        val cardTypeVariantCrowdSourceSwitch = findViewById<SwitchCompat>(R.id.cardTypeCrowdSourceSwitch)
        cardTypeVariantCrowdSourceSwitch.setOnCheckedChangeListener { _, isChecked ->
            addOptionsToCrowdSourceSpinner(!isChecked)
        }
        addOptionsToCrowdSourceSpinner(true)
    }

    private fun addOptionsToCrowdSourceSpinner(filterKnownTypes: Boolean) {
        val cardTypeVariantValueTv = findViewById<TextView>(R.id.cardTypeValueTv)
        val cardTypeVariantName = cardTypeVariantValueTv.text.toString()

        val possibleOptions = arrayListOf<String>()
        if (!filterKnownTypes) possibleOptions.add(getString(R.string.standard_card))
        possibleOptions.add(getString(R.string.all_modes_AB_card))
        possibleOptions.add(getString(R.string.all_modes_ABC_card))
        possibleOptions.add(getString(R.string.all_modes_ABCD_card))
        possibleOptions.add(getString(R.string.bus_out_territory_card))

        val options = arrayListOf(cardTypeVariantName)
        possibleOptions.forEach { option ->
            if (cardTypeVariantName != option) options.add(option)
        }

        val crowdSourceSpinner = findViewById<Spinner>(R.id.cardTypeCrowdSourceSpinner)
        crowdSourceSpinner?.adapter = TypeCrowdSrcAdapter(this, options)
        crowdSourceSpinner.onItemSelectedListener = SpinnerSelectListener(this, options)
    }

    private fun enableCrowdSourceConfirmButton(cardId: ULong, cardTypeVariantId: UInt) {
        val confirmButton: Button = findViewById(R.id.cardTypeCrowdSourceConfirmButton)
        confirmButton.setOnClickListener(CrowdSourceConfirmListener(this, cardId, cardTypeVariantId))
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
        if (this.addFareInfoSectionValues(card.id, card.getFares())) {
            this.addFareInfoSectionTitles()
        } else {
            this.hideFareInfoSectionTitles()
        }
    }

    private fun addFareInfoSectionTitles() {
        val fareSectionTitleTv = findViewById<TextView>(R.id.fareSectionTitleTv)
        fareSectionTitleTv.visibility = View.VISIBLE
    }

    private fun hideFareInfoSectionTitles() {
        val fareSectionTitleTv = findViewById<TextView>(R.id.fareSectionTitleTv)
        fareSectionTitleTv.visibility = View.GONE
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

    private fun scheduleFareNotification(card: Card) {
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    NotificationScheduler().scheduleNotification(card, this)
                    requestExactAlarmPermission()
                }
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermission = android.Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(this, notificationPermission) != PackageManager.PERMISSION_GRANTED) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.enable_notification_title)
                    .setMessage(R.string.enable_notification_message)
                    .setNeutralButton(R.string.accept) { _, _ ->
                        requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                    }
                val dialog = builder.create()
                dialog.show()
                return
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (shouldRequestExactAlarmPermission()) {
                requestExactAlarmPermission()
            }
        }

        NotificationScheduler().scheduleNotification(card, this)
    }

    private fun shouldRequestExactAlarmPermission(): Boolean {
        return !sharedPreferences.getBoolean("exact_alarm_permission_requested", false)
    }

    private fun requestExactAlarmPermission() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.enable_alert_title)
                    .setMessage(R.string.enable_alert_message)
                    .setNeutralButton(R.string.accept) { _, _ ->
                        val settingsIntent = Intent(
                            Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                            Uri.parse("package:com.transition.ora")
                        )
                        startActivity(settingsIntent)
                    }
                val dialog = builder.create()
                dialog.show()

                sharedPreferences.edit().putBoolean("exact_alarm_permission_requested", true).apply()
            }
        }
    }


    private fun addTripInfoSection(card: Card) {
        if (this.addTripInfoSectionValues(card.id, card.getTrips(false))) {
            this.addTripInfoSectionTitles()
        } else {
             this.hideTripInfoSectionTitles()
        }
    }

    private fun addTripInfoSectionTitles() {
        val tripSectionTitleTv = findViewById<TextView>(R.id.tripSectionTitleTv)
        tripSectionTitleTv.visibility = View.VISIBLE
    }

    private fun hideTripInfoSectionTitles() {
        val tripSectionTitleTv = findViewById<TextView>(R.id.tripSectionTitleTv)
        tripSectionTitleTv.visibility = View.GONE
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
        val locale = Locale.Builder()
            .setLanguage(getString(R.string.calendar_language))
            .setRegion(getString(R.string.calendar_country))
            .build()

        return SimpleDateFormat(
            getString(R.string.calendar_pattern), locale
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

    private class SpinnerSelectListener(
        private val activity: CardActivity,
        private val options: ArrayList<String>
    ): AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (position == 0) return

            val cardTypeValueTv = this.activity.findViewById<TextView>(R.id.cardTypeValueTv)
            cardTypeValueTv?.text = options[position]
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    class CrowdSourceConfirmListener(
        private val activity: CardActivity,
        private val id: ULong,
        private val cardTypeVariantId: UInt
    ) : View.OnClickListener {
        override fun onClick(view: View) {
            val cardTypeCrowdSourceSpinner = activity.findViewById<Spinner>(R.id.cardTypeCrowdSourceSpinner)
            val selectedCardType = cardTypeCrowdSourceSpinner.selectedItem as String

            try {
                val db = Firebase.firestore
                val document = db.collection(view.context.getString(R.string.proposition_collection))
                    .document(CardType.Opus.name)
                    .collection("type-propositions")
                    .document(id.toString())

                val data = hashMapOf(
                    "idOnCard" to cardTypeVariantId.toString(),
                    "name" to selectedCardType
                )
                document.set(data)

                val localDb = CardDatabase.getInstance(view.context)
                CoroutineScope(Dispatchers.IO).launch {
                    localDb.daoProposition.insertStoredProposition(
                        CardPropositionEntity(
                            CardType.Opus.name,
                            cardTypeVariantId.toString(),
                            "type",
                            "",
                            selectedCardType,
                            "",
                            "",
                        )
                    )
                }

                val builder = AlertDialog.Builder(view.context)
                builder.setTitle(R.string.proposition_alert_title)
                    .setMessage(R.string.proposition_alert_message)
                    .setPositiveButton(R.string.accept) { _, _ ->
                        activity.findViewById<ConstraintLayout>(R.id.tripLayout).callOnClick()
                    }
                val dialog = builder.create()
                dialog.show()

            } catch(_: Error) {
                val builder = AlertDialog.Builder(view.context)
                builder.setTitle(R.string.proposition_error_title)
                    .setMessage(R.string.proposition_error_message)
                    .setPositiveButton(R.string.accept) { _, _ ->
                        activity.findViewById<ConstraintLayout>(R.id.tripLayout).callOnClick()
                    }
                val dialog = builder.create()
                dialog.show()
            }
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