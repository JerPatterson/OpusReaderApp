package com.transition.ora.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.transition.ora.R
import com.transition.ora.database.CardDatabase
import com.transition.ora.database.entities.CardPropositionEntity
import com.transition.ora.enums.CardType
import com.transition.ora.services.CardContentConverter
import com.transition.ora.types.Card
import com.transition.ora.types.Fare
import com.transition.ora.types.Line
import com.transition.ora.types.Trip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val ARG_CARD = "card"


/**
 * A simple [Fragment] subclass.
 * Use the [TripFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@SuppressLint("ClickableViewAccessibility")
class ValidityFragment: Fragment() {
    private var mView: View? = null
    private var card: Card? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            this.card = Gson().fromJson(it.getString(ARG_CARD), Card::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_validity, container, false)

        val cardValue = this.card
        if (cardValue != null) {
            this.addValidityInfoSection(cardValue)
        }

        return mView
    }

    companion object {
        @JvmStatic
        fun newInstance(card: Card) =
            ValidityFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CARD, Gson().toJson(card))
                }
            }
    }

    private fun getDefaultValidityMinutes(fare: Fare?): Int {
        return if (fare?.operatorId == 5u || fare?.operatorId == 16u) 90 else 120
    }

    private fun addValidityInfoSection(card: Card) {
        this.addValidityInfoSectionTitles()
        this.addValidityInfoSectionValues(card)
    }

    private fun addValidityInfoSectionTitles() {
        val validityStartingTitleTv = this.mView?.findViewById<TextView>(R.id.validityStartingTitleTv)
        val validityEndingTitleTv = this.mView?.findViewById<TextView>(R.id.validityEndingTitleTv)
        validityStartingTitleTv?.text = getString(R.string.validity_starting_on)
        validityEndingTitleTv?.text = getString(R.string.validity_ending_on)
    }

    private fun addValidityInfoSectionValues(card: Card) {
        when (card.type) {
            CardType.Occasional -> addOccasionalCardValidityInfoSectionValues(card)
            CardType.Opus -> addOpusCardValidityInfoSectionValues(card)
        }
    }

    private fun addOccasionalCardValidityInfoSectionValues(card: Card) {
        val validityStartingValueTv = this.mView?.findViewById<TextView>(R.id.validityStartingValueTv)
        val validityEndingValueTv = this.mView?.findViewById<TextView>(R.id.validityEndingValueTv)

        var progress = 100
        val now = Calendar.getInstance()

        val fare = card.getFares()[0]
        val validityFromDateValue = fare.validityFromDate
        val validityUntilDateValue = fare.validityUntilDate
        if (validityFromDateValue == null || validityUntilDateValue == null) {
            val trips = card.getTrips()
            val usableFromDate = trips[trips.size - 1].firstUseDate
            val usableUntilDate = usableFromDate.clone() as Calendar
            usableUntilDate.add(Calendar.MINUTE, getDefaultValidityMinutes(fare))

            validityStartingValueTv?.text = calendarToStringWithTimeWithoutYear(usableFromDate)
            validityEndingValueTv?.text = calendarToStringWithTimeWithoutYear(usableUntilDate)

            if (usableUntilDate.timeInMillis > now.timeInMillis) {
                progress = ((now.timeInMillis - usableFromDate.timeInMillis) * 100 /
                        (usableUntilDate.timeInMillis - usableFromDate.timeInMillis)).toInt()
            }

            addOccasionalCardTrips(card, progress, usableFromDate, usableUntilDate)
        } else {
            validityStartingValueTv?.text = calendarToStringWithTimeWithoutYear(validityFromDateValue)
            validityEndingValueTv?.text = calendarToStringWithTimeWithoutYear(validityUntilDateValue)

            if (validityUntilDateValue.timeInMillis > now.timeInMillis) {
                progress = ((now.timeInMillis - validityFromDateValue.timeInMillis) * 100 /
                        (validityUntilDateValue.timeInMillis - validityFromDateValue.timeInMillis)).toInt()
            }

            addOccasionalCardTrips(card, progress, validityFromDateValue, validityUntilDateValue)
        }
    }

    private fun addOccasionalCardTrips(card: Card, progress: Int, startDate: Calendar, endDate: Calendar) {
        val validitySeekBar = this.mView?.findViewById<SeekBar>(R.id.validitySeekBar)
        validitySeekBar?.progress = progress
        validitySeekBar?.setOnTouchListener(OnTouchListener())

        val validityHigherLabelLine = this.mView?.findViewById<View>(R.id.validityHigherLabelLine)
        val validityHigherLineIdTv = this.mView?.findViewById<TextView>(R.id.validityHigherLineIdTv)
        val validityHigherModeImage = this.mView?.findViewById<ImageView>(R.id.validityHigherModeImageView)
        val validityMiddleLabelLine = this.mView?.findViewById<View>(R.id.validityMiddleLabelLine)
        val validityMiddleLineIdTv = this.mView?.findViewById<TextView>(R.id.validityMiddleLineIdTv)
        val validityMiddleModeImage = this.mView?.findViewById<ImageView>(R.id.validityMiddleModeImageView)
        val validityLowerLabelLine = this.mView?.findViewById<View>(R.id.validityLowerLabelLine)
        val validityLowerLineIdTv = this.mView?.findViewById<TextView>(R.id.validityLowerLineIdTv)
        val validityLowerModeImage = this.mView?.findViewById<ImageView>(R.id.validityLowerModeImageView)

        hideCardScanEvent(validityLowerLabelLine, validityLowerLineIdTv, validityLowerModeImage)
        hideCardScanEvent(validityMiddleLabelLine, validityMiddleLineIdTv, validityMiddleModeImage)
        hideCardScanEvent(validityHigherLabelLine, validityHigherLineIdTv, validityHigherModeImage)

        val trips = card.getTrips()
        for ((i, trip) in trips.withIndex().reversed()) {
            val line = CardContentConverter.getLineById(requireContext(), trip.zoneId, trip.operatorId, trip.lineId)
            if (i == 0 && trips.size == 1 || i == 1) {
                val useProgress = (trip.useDate.timeInMillis - startDate.timeInMillis).toFloat() / (endDate.timeInMillis - startDate.timeInMillis).toFloat()
                if (useProgress < 0) continue
                listenForLineProposition(trip, validityHigherLabelLine, validityHigherLineIdTv)
                addCardScanEvent(line, validityHigherLabelLine, validityHigherLineIdTv, validityHigherModeImage)
            } else {
                val useProgress = (trip.useDate.timeInMillis - startDate.timeInMillis).toFloat() / (endDate.timeInMillis - startDate.timeInMillis).toFloat()
                if (useProgress < 0) continue
                listenForLineProposition(trip, validityMiddleLabelLine, validityMiddleLineIdTv)
                addCardScanEvent(line, validityMiddleLabelLine, validityMiddleLineIdTv, validityMiddleModeImage)
            }
        }

        validitySeekBar?.doOnLayout {
            val dpWidthSeekBar = validitySeekBar.measuredWidth - (validitySeekBar.paddingLeft + validitySeekBar.paddingRight)
            for ((i, trip) in trips.withIndex().reversed()) {
                if (i == 0 && trips.size == 1 || i == 1) {
                    val useProgress = (trip.useDate.timeInMillis - startDate.timeInMillis).toFloat() / (endDate.timeInMillis - startDate.timeInMillis).toFloat()
                    if (useProgress < 0) continue
                    moveCardScanEvent(dpWidthSeekBar * useProgress, validityHigherLabelLine, validityHigherLineIdTv, validityHigherModeImage)
                } else {
                    val useProgress = (trip.useDate.timeInMillis - startDate.timeInMillis).toFloat() / (endDate.timeInMillis - startDate.timeInMillis).toFloat()
                    if (useProgress < 0) continue
                    moveCardScanEvent(dpWidthSeekBar * useProgress, validityMiddleLabelLine, validityMiddleLineIdTv, validityMiddleModeImage)
                }
            }
        }
    }

    private fun addOpusCardValidityInfoSectionValues(card: Card) {
        val validityStartingValueTv = this.mView?.findViewById<TextView>(R.id.validityStartingValueTv)
        val validityEndingValueTv = this.mView?.findViewById<TextView>(R.id.validityEndingValueTv)

        var progress = 100
        val now = Calendar.getInstance()

        val trips = card.getTrips()
        val fares = card.getFares()

        val mostRecentTrip = trips[trips.size - 1]
        val mostRecentFare = fares.find { fare -> fare.fareIndex == mostRecentTrip.fareIndex }
        val validityFromDateValue = mostRecentFare?.validityFromDate
        val validityUntilDateValue = mostRecentFare?.validityUntilDate

        if (validityFromDateValue == null || validityUntilDateValue == null
            || (validityUntilDateValue.timeInMillis < mostRecentTrip.useDate.timeInMillis)) {
            val usableFromDate = mostRecentTrip.firstUseDate
            val usableUntilDate = usableFromDate.clone() as Calendar
            usableUntilDate.add(Calendar.MINUTE, getDefaultValidityMinutes(mostRecentFare))

            validityStartingValueTv?.text = calendarToStringWithTimeWithoutYear(usableFromDate)
            validityEndingValueTv?.text = calendarToStringWithTimeWithoutYear(usableUntilDate)

            if (usableUntilDate.timeInMillis > now.timeInMillis) {
                progress = ((now.timeInMillis - usableFromDate.timeInMillis) * 100 /
                        (usableUntilDate.timeInMillis - usableFromDate.timeInMillis)).toInt()
            }

            addOpusCardTrips(card, progress, usableFromDate, usableUntilDate)
        } else {
            validityStartingValueTv?.text = calendarToStringWithTimeWithoutYear(validityFromDateValue)
            validityEndingValueTv?.text = calendarToStringWithTimeWithoutYear(validityUntilDateValue)

            if (validityUntilDateValue.timeInMillis > now.timeInMillis) {
                progress = ((now.timeInMillis - validityFromDateValue.timeInMillis) * 100 /
                        (validityUntilDateValue.timeInMillis - validityFromDateValue.timeInMillis)).toInt()
            }

            addOpusCardTrips(card, progress, validityFromDateValue, validityUntilDateValue)
        }
    }

    private fun addOpusCardTrips(card: Card, progress: Int, startDate: Calendar, endDate: Calendar) {
        val validitySeekBar = this.mView?.findViewById<SeekBar>(R.id.validitySeekBar)
        validitySeekBar?.progress = progress
        validitySeekBar?.setOnTouchListener(OnTouchListener())

        val validityHigherLabelLine = this.mView?.findViewById<View>(R.id.validityHigherLabelLine)
        val validityHigherLineIdTv = this.mView?.findViewById<TextView>(R.id.validityHigherLineIdTv)
        val validityHigherModeImage = this.mView?.findViewById<ImageView>(R.id.validityHigherModeImageView)
        val validityMiddleLabelLine = this.mView?.findViewById<View>(R.id.validityMiddleLabelLine)
        val validityMiddleLineIdTv = this.mView?.findViewById<TextView>(R.id.validityMiddleLineIdTv)
        val validityMiddleModeImage = this.mView?.findViewById<ImageView>(R.id.validityMiddleModeImageView)
        val validityLowerLabelLine = this.mView?.findViewById<View>(R.id.validityLowerLabelLine)
        val validityLowerLineIdTv = this.mView?.findViewById<TextView>(R.id.validityLowerLineIdTv)
        val validityLowerModeImage = this.mView?.findViewById<ImageView>(R.id.validityLowerModeImageView)

        hideCardScanEvent(validityLowerLabelLine, validityLowerLineIdTv, validityLowerModeImage)
        hideCardScanEvent(validityMiddleLabelLine, validityMiddleLineIdTv, validityMiddleModeImage)
        hideCardScanEvent(validityHigherLabelLine, validityHigherLineIdTv, validityHigherModeImage)

        val trips = card.getTrips()
        for ((i, trip) in trips.withIndex().reversed()) {
            val line = CardContentConverter.getLineById(requireContext(), trip.zoneId, trip.operatorId, trip.lineId)
            if ((i == 0 && trips.size == 1) || (i == 1 && trips.size == 2) || i == 2) {
                val useProgress = (trip.useDate.timeInMillis - startDate.timeInMillis).toFloat() / (endDate.timeInMillis - startDate.timeInMillis).toFloat()
                if (useProgress < 0) continue
                listenForLineProposition(trip, validityHigherLabelLine, validityHigherLineIdTv)
                addCardScanEvent(line, validityHigherLabelLine, validityHigherLineIdTv, validityHigherModeImage)
            } else if ((i == 0 && trips.size == 2) || i == 1) {
                val useProgress = (trip.useDate.timeInMillis - startDate.timeInMillis).toFloat() / (endDate.timeInMillis - startDate.timeInMillis).toFloat()
                if (useProgress < 0) continue
                listenForLineProposition(trip, validityMiddleLabelLine, validityMiddleLineIdTv)
                addCardScanEvent(line, validityMiddleLabelLine, validityMiddleLineIdTv, validityMiddleModeImage)
            } else if (i == 0) {
                val useProgress = (trip.useDate.timeInMillis - startDate.timeInMillis).toFloat() / (endDate.timeInMillis - startDate.timeInMillis).toFloat()
                if (useProgress < 0) continue
                listenForLineProposition(trip, validityLowerLabelLine, validityLowerLineIdTv)
                addCardScanEvent(line, validityLowerLabelLine, validityLowerLineIdTv, validityLowerModeImage)
            }
        }

        validitySeekBar?.doOnLayout {
            val dpWidthSeekBar = validitySeekBar.measuredWidth - (validitySeekBar.paddingLeft + validitySeekBar.paddingRight)
            for ((i, trip) in trips.withIndex().reversed()) {
                if ((i == 0 && trips.size == 1) || (i == 1 && trips.size == 2) || i == 2) {
                    val useProgress = (trip.useDate.timeInMillis - startDate.timeInMillis).toFloat() / (endDate.timeInMillis - startDate.timeInMillis).toFloat()
                    if (useProgress < 0) continue
                    moveCardScanEvent(dpWidthSeekBar * useProgress, validityHigherLabelLine, validityHigherLineIdTv, validityHigherModeImage)
                } else if ((i == 0 && trips.size == 2) || i == 1) {
                    val useProgress = (trip.useDate.timeInMillis - startDate.timeInMillis).toFloat() / (endDate.timeInMillis - startDate.timeInMillis).toFloat()
                    if (useProgress < 0) continue
                    moveCardScanEvent(dpWidthSeekBar * useProgress, validityMiddleLabelLine, validityMiddleLineIdTv, validityMiddleModeImage)
                } else if (i == 0) {
                    val useProgress = (trip.useDate.timeInMillis - startDate.timeInMillis).toFloat() / (endDate.timeInMillis - startDate.timeInMillis).toFloat()
                    if (useProgress < 0) continue
                    moveCardScanEvent(dpWidthSeekBar * useProgress, validityLowerLabelLine, validityLowerLineIdTv, validityLowerModeImage)
                }
            }
        }
    }

    private fun addCardScanEvent(line: Line, validityLabelLine: View?, validityLineIdTv: TextView?, validityModeImage: ImageView?) {
        validityLabelLine?.visibility = View.VISIBLE
        validityLineIdTv?.visibility = View.VISIBLE
        validityModeImage?.visibility = View.VISIBLE
        validityLineIdTv?.text = line.id
        validityLineIdTv?.setTextColor(Color.parseColor(line.textColor))
        validityLineIdTv?.setBackgroundColor(Color.parseColor(line.color))
        validityModeImage?.setImageResource(line.icon)
    }

    private fun listenForLineProposition(trip: Trip, validityLabelLine: View?, validityLineIdTv: TextView?) {
        var liveProposition: LiveData<CardPropositionEntity?>? = null
        val db = CardDatabase.getInstance(requireContext())
        val job = CoroutineScope(Dispatchers.IO).launch {
            liveProposition = db.daoProposition.getLiveStoredPropositionById(
                trip.operatorId.toString(),
                trip.lineId.toString(),
                "line"
            )
        }

        runBlocking {
            job.join()
        }

        liveProposition?.observe(viewLifecycleOwner) { proposition ->
            if (proposition != null) {
                validityLabelLine?.visibility = View.VISIBLE
                validityLineIdTv?.visibility = View.VISIBLE
                validityLineIdTv?.text = proposition.id
                validityLineIdTv?.setTextColor(Color.parseColor(proposition.textColor))
                validityLineIdTv?.setBackgroundColor(Color.parseColor(proposition.color))
            }
        }
    }

    private fun hideCardScanEvent(validityLabelLine: View?, validityLineIdTv: TextView?, validityModeImage: ImageView?) {
        validityLabelLine?.visibility = View.GONE
        validityLineIdTv?.visibility = View.GONE
        validityModeImage?.visibility = View.GONE
    }

    private fun moveCardScanEvent(translation: Float, validityLabelLine: View?, validityLineIdTv: TextView?, validityModeImage: ImageView?) {
        validityLabelLine?.translationX = translation
        validityLineIdTv?.translationX = translation
        validityModeImage?.translationX = translation
    }

    private fun calendarToStringWithTimeWithoutYear(cal: Calendar): String {
        val locale = Locale.Builder()
            .setLanguage(getString(R.string.calendar_language))
            .setRegion(getString(R.string.calendar_country))
            .build()

        return SimpleDateFormat(
            getString(R.string.calendar_with_time_without_year_pattern), locale
        ).format(cal.time)
    }

    class OnTouchListener: View.OnTouchListener {
        override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
            return true
        }
    }
}