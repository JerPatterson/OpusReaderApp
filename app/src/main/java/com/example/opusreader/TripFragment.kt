package com.example.opusreader

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val ARG_TRIP = "trip"

/**
 * A simple [Fragment] subclass.
 * Use the [TripFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TripFragment : Fragment() {
    private var mView: View? = null
    private var trip: Trip? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            this.trip = Gson().fromJson(it.getString(ARG_TRIP), Trip::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_trip, container, false)

        val tripValue = this.trip
        if (tripValue != null) {
            this.addTripInfoSection(tripValue)
        }

        return mView
    }

    companion object {
        @JvmStatic
        fun newInstance(trip: Trip) =
            TripFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TRIP, Gson().toJson(trip))
                }
            }
    }

    private fun addTripInfoSection(trip: Trip) {
        this.addTripInfoSectionTitles()
        this.addTripInfoSectionValues(trip)
    }

    private fun addTripInfoSectionTitles() {
        val boardingDateTitleTv = this.mView?.findViewById<TextView>(R.id.tripBoardingDateTv)
        val validityFromDateTitleTv = this.mView?.findViewById<TextView>(R.id.tripValidityFromDateTv)
        boardingDateTitleTv?.text = getString(R.string.trip_boarding_date_title)
        validityFromDateTitleTv?.text = getString(R.string.trip_validity_from_date_title)
    }

    private fun addTripInfoSectionValues(trip: Trip) {
        val line = IdConverter.getLineById(trip.operatorId, trip.lineId)
        val operator = IdConverter.getOperatorById(trip.operatorId)
        addTripLine(line)
        addTripDates(trip)
        addTripInfoSectionImages(operator, line)
    }

    private fun addTripLine(line: Line) {
        val lineIdTv = this.mView?.findViewById<TextView>(R.id.tripLineIdTv)
        val name = this.mView?.findViewById<TextView>(R.id.tripLineNameTv)
        lineIdTv?.text = line.id
        name?.text = line.name

        val textColor = Color.parseColor(line.textColor)
        lineIdTv?.setTextColor(textColor)
        val background = Color.parseColor(line.color)
        val tripColorLayout = this.mView?.findViewById<LinearLayout>(R.id.tripColorLayout)
        lineIdTv?.setBackgroundColor(background)
        tripColorLayout?.setBackgroundColor(background)
    }

    private fun addTripDates(trip: Trip) {
        val boardingDate = this.mView?.findViewById<TextView>(R.id.tripBoardingDateValueTv)
        val validityFromDate = this.mView?.findViewById<TextView>(R.id.tripValidityFromDateValueTv)
        boardingDate?.text = calendarToStringWithTime(trip.useDate)
        validityFromDate?.text = calendarToStringWithTime(trip.firstUseDate)
    }

    private fun addTripInfoSectionImages(operator: Operator, line: Line) {
        val modeImageView = this.mView?.findViewById<ImageView>(R.id.modeImageView)
        val operatorImageView = this.mView?.findViewById<ImageView>(R.id.operatorImageView)
        modeImageView?.setImageResource(line.icon)
        operatorImageView?.setImageResource(operator.imageId)
    }

    private fun calendarToStringWithTime(cal: Calendar): String {
        return SimpleDateFormat(
            getString(R.string.calendar_with_time_pattern),
            Locale(getString(R.string.calendar_language), getString(R.string.calendar_country))
        ).format(cal.time)
    }
}