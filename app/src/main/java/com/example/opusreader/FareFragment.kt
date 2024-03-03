package com.example.opusreader

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val ARG_FARE = "fare"

/**
 * A simple [Fragment] subclass.
 * Use the [FareFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FareFragment : Fragment() {
    private var mView: View? = null
    private var fare: Fare? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            this.fare = Gson().fromJson(it.getString(ARG_FARE), Fare::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_fare, container, false)

        val fareValue = this.fare
        if (fareValue != null) {
            this.addFareInfoSection(fareValue)
        }

        return mView
    }

    companion object {
        @JvmStatic
        fun newInstance(fare: Fare) =
            FareFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_FARE, Gson().toJson(fare))
                }
            }
    }

    private fun addFareInfoSection(fare: Fare) {
        this.addFareInfoSectionTitles(fare)
        this.addFareInfoSectionValues(fare)
    }

    private fun addFareInfoSectionTitles(fare: Fare) {
        val fareTypeTitleTv = this.mView?.findViewById<TextView>(R.id.fareTypeValueTv)
        fareTypeTitleTv?.text = IdConverter.getFareProductById(fare.typeId).name

        val buyingDateTitleTv = this.mView?.findViewById<TextView>(R.id.fareBuyingDateTv)
        buyingDateTitleTv?.text = "Acheté le"


        if (fare.validityFromDate != null) {
            val firstUseDateTitleTv = this.mView?.findViewById<TextView>(R.id.validityFromDateTv)
            firstUseDateTitleTv?.text = "Valide du"
        }

        if (fare.ticketCount != null)  {
            val ticketCountValueTv = this.mView?.findViewById<TextView>(R.id.ticketCountTv)
            ticketCountValueTv?.text = "Passages\nrestants"
        }
    }

    private fun addFareInfoSectionValues(fare: Fare) {
        if (fare.ticketCount != null) {
            val ticketCountTv = this.mView?.findViewById<TextView>(R.id.ticketCountValueTv)
            ticketCountTv?.text = fare.ticketCount.toString()

            val fareValidityColorLayout = this.mView?.findViewById<LinearLayout>(R.id.fareValidityColorLayout)
            if (fare.ticketCount == 0u) {
                fareValidityColorLayout?.setBackgroundColor(Color.parseColor("#ff0317"))
            } else {
                fareValidityColorLayout?.setBackgroundColor(Color.parseColor("#108016"))
            }
        }

        val buyingDateTv = this.mView?.findViewById<TextView>(R.id.fareBuyingDateValueTv)
        if (fare.buyingDateHasMinutes) {
            buyingDateTv?.text = this.calendarToStringWithTime(fare.buyingDate)
        } else {
            buyingDateTv?.text = this.calendarToString(fare.buyingDate)
        }

        val fromDate = fare.validityFromDate
        val untilDate = fare.validityUntilDate
        if (fromDate != null && untilDate != null) {
            val validityFromDateTv = this.mView?.findViewById<TextView>(R.id.validityFromDateValueTv)
            validityFromDateTv?.text = "${this.calendarToStringWithoutYear(fromDate)} au ${this.calendarToString(untilDate)}"

            val fareValidityColorLayout = this.mView?.findViewById<LinearLayout>(R.id.fareValidityColorLayout)
            if (untilDate.time < Calendar.getInstance().time) {
                fareValidityColorLayout?.setBackgroundColor(Color.parseColor("#ff0317"))
            } else {
                fareValidityColorLayout?.setBackgroundColor(Color.parseColor("#108016"))
            }
        }
    }

    private fun calendarToString(cal: Calendar): String {
        return SimpleDateFormat("dd MMMM yyyy", Locale.CANADA_FRENCH).format(cal.time)
    }
    private fun calendarToStringWithoutYear(cal: Calendar): String {
        return SimpleDateFormat("dd MMMM", Locale.CANADA_FRENCH).format(cal.time)
    }

    private fun calendarToStringWithTime(cal: Calendar): String {
        return SimpleDateFormat("dd MMMM yyyy à HH:mm", Locale.CANADA_FRENCH).format(cal.time)
    }
}