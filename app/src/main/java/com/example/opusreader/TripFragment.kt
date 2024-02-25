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
        val boardingDate = this.mView?.findViewById<TextView>(R.id.tripBoardingDateTv)
        val validityFromDate = this.mView?.findViewById<TextView>(R.id.tripValidityFromDateTv)

        boardingDate?.text = "Embarquement le"
        validityFromDate?.text = "Valide à partir du"
    }

    private fun addTripInfoSectionValues(trip: Trip) {
        val line = IdConverter.getLineById(trip.operatorId, trip.lineId)
        val operator = IdConverter.getOperatorById(trip.operatorId)

        val id = this.mView?.findViewById<TextView>(R.id.tripLineIdTv)
        val name = this.mView?.findViewById<TextView>(R.id.tripLineNameTv)
        id?.text = line.id
        name?.text = line.name

        val color = this.mView?.findViewById<LinearLayout>(R.id.tripColorLayout)
        val textColor = Color.parseColor(line.textColor)
        val background = Color.parseColor(line.color)
        id?.setTextColor(textColor)
        id?.setBackgroundColor(background)
        color?.setBackgroundColor(background)

        val boardingDate = this.mView?.findViewById<TextView>(R.id.tripBoardingDateValueTv)
        val validityFromDate = this.mView?.findViewById<TextView>(R.id.tripValidityFromDateValueTv)
        boardingDate?.text = calendarToStringWithTime(trip.useDate)
        validityFromDate?.text = calendarToStringWithTime(trip.firstUseDate)

        addTripInfoSectionImages(operator, line)
    }

    private fun addTripInfoSectionImages(operator: Operator, line: Line) {
        val modeImage = this.mView?.findViewById<ImageView>(R.id.modeImageView)
        val operatorImage = this.mView?.findViewById<ImageView>(R.id.operatorImageView)

        modeImage?.setImageResource(line.icon)
        operatorImage?.setImageResource(operator.imageId)
    }

    private fun calendarToStringWithTime(cal: Calendar): String {
        return SimpleDateFormat("dd MMMM yyyy à HH:MM", Locale.CANADA_FRENCH).format(cal.time)
    }
}