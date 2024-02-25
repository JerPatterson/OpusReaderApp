package com.example.opusreader

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar

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
        this.addTripInfoSectionTitles(trip)
        this.addTripInfoSectionValues(trip)
    }

    private fun addTripInfoSectionTitles(trip: Trip) {
        val title = this.mView?.findViewById<TextView>(R.id.tripLineTv)
        val boardingDate = this.mView?.findViewById<TextView>(R.id.tripBoardingDateTv)
        val validityFromDate = this.mView?.findViewById<TextView>(R.id.tripValidityFromDateTv)

        title?.text = IdConverter.getLineById(trip.operatorId, trip.lineId).name
        boardingDate?.text = "Embarquement le"
        validityFromDate?.text = "Valide à partir du"
    }

    private fun addTripInfoSectionValues(trip: Trip) {
        val boardingDate = this.mView?.findViewById<TextView>(R.id.tripBoardingDateValueTv)
        val validityFromDate = this.mView?.findViewById<TextView>(R.id.tripValidityFromDateValueTv)

        boardingDate?.text = calendarToStringWithTime(trip.useDate)
        validityFromDate?.text = calendarToStringWithTime(trip.firstUseDate)
    }

    private fun calendarToStringWithTime(cal: Calendar): String {
        return SimpleDateFormat("dd MMMM YYYY à HH:MM").format(cal.time)
    }
}