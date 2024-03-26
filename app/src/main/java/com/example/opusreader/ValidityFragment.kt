package com.example.opusreader

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


private const val ARG_CARD = "card"

/**
 * A simple [Fragment] subclass.
 * Use the [TripFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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

        val fare = card.fares[0]
        val validityFromDateValue = fare.validityFromDate
        val validityUntilDateValue = fare.validityUntilDate
        if (validityFromDateValue == null || validityUntilDateValue == null) {
            var usableFromDate = card.trips[0].firstUseDate
            for (trip in card.trips) {
                if (usableFromDate.time < trip.firstUseDate.time) usableFromDate = trip.firstUseDate
            }
            validityStartingValueTv?.text = calendarToStringWithTimeWithoutYear(usableFromDate)
            val usableUntilDate = usableFromDate.clone() as Calendar
            usableUntilDate.add(Calendar.HOUR_OF_DAY, 2)
            validityEndingValueTv?.text = calendarToStringWithTimeWithoutYear(usableUntilDate)

            if (usableUntilDate.time > now.time) {
                progress = ((now.timeInMillis - usableFromDate.timeInMillis) /
                        (usableUntilDate.timeInMillis - usableFromDate.timeInMillis) * 100).toInt()
            }
            addOccasionalCardTrips(card, progress, usableFromDate, usableUntilDate)
        } else {
            validityStartingValueTv?.text = calendarToStringWithTimeWithoutYear(validityFromDateValue)
            validityEndingValueTv?.text = calendarToStringWithTimeWithoutYear(validityUntilDateValue)
            if (validityUntilDateValue.time > now.time) {
                progress = ((now.timeInMillis - validityFromDateValue.timeInMillis) /
                        (validityUntilDateValue.timeInMillis - validityFromDateValue.timeInMillis) * 100).toInt()
            }
            addOccasionalCardTrips(card, progress, validityFromDateValue, validityUntilDateValue)
        }
    }

    private fun addOccasionalCardTrips(card: Card, progress: Int, startDate: Calendar, endDate: Calendar) {
        val displayMetrics = context?.resources?.displayMetrics ?: return
        val dpWidthSeekBar = displayMetrics.widthPixels - (displayMetrics.widthPixels * 0.35F)
        val validitySeekBar = this.mView?.findViewById<SeekBar>(R.id.validitySeekBar)
        validitySeekBar?.progress = progress

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
        val sortedTrips = card.trips.sortedBy { Trip -> Trip.useDate }
        for ((i, trip) in sortedTrips.withIndex()) {
            val line = CardContentConverter.getLineById(trip.operatorId, trip.lineId)
            val hasToBeHidden = (trip.operatorId == 0u || (endDate.timeInMillis - startDate.timeInMillis) <= 0
                    || (i < sortedTrips.size - 1 && trip.useDate.time == sortedTrips[i + 1].useDate.time))
            when (i + 1) {
                1 -> {
                    if (hasToBeHidden) {
                        hideCardScanEvent(validityMiddleLabelLine, validityMiddleLineIdTv, validityMiddleModeImage)
                    } else {
                        val useProgress = (trip.useDate.timeInMillis - startDate.timeInMillis).toFloat() / (endDate.timeInMillis - startDate.timeInMillis).toFloat()
                        addCardScanEvent(line, validityMiddleLabelLine, validityMiddleLineIdTv, validityMiddleModeImage)
                        moveCardScanEvent(dpWidthSeekBar * useProgress, validityMiddleLabelLine, validityMiddleLineIdTv, validityMiddleModeImage)
                    }
                }
                2 -> {
                    if (hasToBeHidden) {
                        hideCardScanEvent(validityHigherLabelLine, validityHigherLineIdTv, validityHigherModeImage)
                    } else {
                        val useProgress = (trip.useDate.timeInMillis - startDate.timeInMillis).toFloat() / (endDate.timeInMillis - startDate.timeInMillis).toFloat()
                        addCardScanEvent(line, validityHigherLabelLine, validityHigherLineIdTv, validityHigherModeImage)
                        moveCardScanEvent(dpWidthSeekBar * useProgress, validityHigherLabelLine, validityHigherLineIdTv, validityHigherModeImage)
                    }
                }
            }
        }
    }

    private fun addOpusCardValidityInfoSectionValues(card: Card) {
        val validityStartingValueTv = this.mView?.findViewById<TextView>(R.id.validityStartingValueTv)
        val validityEndingValueTv = this.mView?.findViewById<TextView>(R.id.validityEndingValueTv)

        var progress = 100
        val now = Calendar.getInstance()

        val mostRecentTrip = card.trips.sortedBy { Trip -> Trip.useDate }[0]
        val mostRecentUnlimitedFare = card.fares.filter { fare -> fare.validityUntilDate != null }
            .sortedBy { Fare -> Fare.validityUntilDate }.last()
        val validityFromDateValue = mostRecentUnlimitedFare.validityFromDate
        val validityUntilDateValue = mostRecentUnlimitedFare.validityUntilDate
        if (validityFromDateValue == null || validityUntilDateValue == null
            || (validityUntilDateValue.timeInMillis < mostRecentTrip.useDate.timeInMillis)) {
            var usableFromDate = card.trips[0].firstUseDate
            for (trip in card.trips) {
                if (usableFromDate.time < trip.firstUseDate.time) usableFromDate = trip.firstUseDate
            }
            validityStartingValueTv?.text = calendarToStringWithTimeWithoutYear(usableFromDate)
            val usableUntilDate = usableFromDate.clone() as Calendar
            usableUntilDate.add(Calendar.HOUR_OF_DAY, 2)
            validityEndingValueTv?.text = calendarToStringWithTimeWithoutYear(usableUntilDate)

            if (usableUntilDate.time > now.time) {
                progress = ((now.timeInMillis - usableFromDate.timeInMillis) /
                        (usableUntilDate.timeInMillis - usableFromDate.timeInMillis) * 100).toInt()
            }
            addOpusCardTrips(card, progress, usableFromDate, usableUntilDate)
        } else {
            validityStartingValueTv?.text = calendarToStringWithTimeWithoutYear(validityFromDateValue)
            validityEndingValueTv?.text = calendarToStringWithTimeWithoutYear(validityUntilDateValue)
            if (validityUntilDateValue.time > now.time) {
                progress = ((now.timeInMillis - validityFromDateValue.timeInMillis) /
                        (validityUntilDateValue.timeInMillis - validityFromDateValue.timeInMillis) * 100).toInt()
            }
            addOpusCardTrips(card, progress, validityFromDateValue, validityUntilDateValue)
        }
    }

    private fun addOpusCardTrips(card: Card, progress: Int, startDate: Calendar, endDate: Calendar) {
        val displayMetrics = context?.resources?.displayMetrics ?: return
        val dpWidthSeekBar = displayMetrics.widthPixels - (displayMetrics.widthPixels * 0.35F)
        val validitySeekBar = this.mView?.findViewById<SeekBar>(R.id.validitySeekBar)
        validitySeekBar?.progress = progress

        val validityHigherLabelLine = this.mView?.findViewById<View>(R.id.validityHigherLabelLine)
        val validityHigherLineIdTv = this.mView?.findViewById<TextView>(R.id.validityHigherLineIdTv)
        val validityHigherModeImage = this.mView?.findViewById<ImageView>(R.id.validityHigherModeImageView)
        val validityMiddleLabelLine = this.mView?.findViewById<View>(R.id.validityMiddleLabelLine)
        val validityMiddleLineIdTv = this.mView?.findViewById<TextView>(R.id.validityMiddleLineIdTv)
        val validityMiddleModeImage = this.mView?.findViewById<ImageView>(R.id.validityMiddleModeImageView)
        val validityLowerLabelLine = this.mView?.findViewById<View>(R.id.validityLowerLabelLine)
        val validityLowerLineIdTv = this.mView?.findViewById<TextView>(R.id.validityLowerLineIdTv)
        val validityLowerModeImage = this.mView?.findViewById<ImageView>(R.id.validityLowerModeImageView)

        for ((i, trip) in card.trips.sortedBy { Trip -> Trip.useDate }.withIndex()) {
            val line = CardContentConverter.getLineById(trip.operatorId, trip.lineId)
            val hasToBeHidden = (trip.operatorId == 0u || (endDate.timeInMillis - startDate.timeInMillis) <= 0
                    || (trip.useDate.timeInMillis - startDate.timeInMillis) < 0)
            when (i + 1) {
                1 -> {
                    if (hasToBeHidden) {
                        hideCardScanEvent(validityLowerLabelLine, validityLowerLineIdTv, validityLowerModeImage)
                    } else {
                        val useProgress = (trip.useDate.timeInMillis - startDate.timeInMillis).toFloat() / (endDate.timeInMillis - startDate.timeInMillis).toFloat()
                        addCardScanEvent(line, validityLowerLabelLine, validityLowerLineIdTv, validityLowerModeImage)
                        moveCardScanEvent(dpWidthSeekBar * useProgress, validityLowerLabelLine, validityLowerLineIdTv, validityLowerModeImage)
                    }
                }
                2 -> {
                    if (hasToBeHidden) {
                        hideCardScanEvent(validityMiddleLabelLine, validityMiddleLineIdTv, validityMiddleModeImage)
                    } else {
                        val useProgress = (trip.useDate.timeInMillis - startDate.timeInMillis).toFloat() / (endDate.timeInMillis - startDate.timeInMillis).toFloat()
                        addCardScanEvent(line, validityMiddleLabelLine, validityMiddleLineIdTv, validityMiddleModeImage)
                        moveCardScanEvent(dpWidthSeekBar * useProgress, validityMiddleLabelLine, validityMiddleLineIdTv, validityMiddleModeImage)
                    }
                }
                3 -> {
                    if (hasToBeHidden) {
                        hideCardScanEvent(validityHigherLabelLine, validityHigherLineIdTv, validityHigherModeImage)
                    } else {
                        val useProgress = (trip.useDate.timeInMillis - startDate.timeInMillis).toFloat() / (endDate.timeInMillis - startDate.timeInMillis).toFloat()
                        addCardScanEvent(line, validityHigherLabelLine, validityHigherLineIdTv, validityHigherModeImage)
                        moveCardScanEvent(dpWidthSeekBar * useProgress, validityHigherLabelLine, validityHigherLineIdTv, validityHigherModeImage)
                    }
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
        return SimpleDateFormat(
            getString(R.string.calendar_with_time_pattern_without_year_pattern),
            Locale(getString(R.string.calendar_language), getString(R.string.calendar_country))
        ).format(cal.time)
    }
}