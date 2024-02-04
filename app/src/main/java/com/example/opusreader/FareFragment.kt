package com.example.opusreader

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.Gson
import java.util.Calendar

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
        val title = this.mView?.findViewById<TextView>(R.id.fareTypeValueTv)
        val buyingDate = this.mView?.findViewById<TextView>(R.id.fareBuyingDateTv)
        val firstUseDate = this.mView?.findViewById<TextView>(R.id.validityFromDateTv)
        val validityUntilDate = this.mView?.findViewById<TextView>(R.id.validityUntilDateTv)
        val ticketCount = this.mView?.findViewById<TextView>(R.id.ticketCountTv)

        title?.text = when (fare.typeId) {
            3316801u -> "2 passages, Bus"
            3316865u -> "10 passages, Bus"
            3314625u -> "2 passages, Tous modes A"
            3314689u -> "1 passage, Tous modes AB"
            3314753u -> "2 passages, Tous modes AB"
            3312577u -> "10 passages, Tous modes AB"
            3321601u -> "3 jours, Tous modes AB"
            3310337u -> "1 passage, Tous modes ABC"
            3305921u -> "Soirée illimité"
            else -> "Inconnu (${fare.typeId})"
        }

        buyingDate?.text = "Acheté le"
        if (fare.validityFromDate != null) firstUseDate?.text = "Valide à partir du"
        if (fare.validityUntilDate != null) validityUntilDate?.text = "Valide jusqu'au"
        if (fare.ticketCount != null)  ticketCount?.text = "Billets restants"
    }

    private fun addFareInfoSectionValues(fare: Fare) {
        val ticketCount = this.mView?.findViewById<TextView>(R.id.ticketCountValueTv)
        val buyingDate = this.mView?.findViewById<TextView>(R.id.fareBuyingDateValueTv)
        val validityFromDate = this.mView?.findViewById<TextView>(R.id.validityFromDateValueTv)
        val validityUntilDate = this.mView?.findViewById<TextView>(R.id.validityUntilDateValueTv)

        if (fare.ticketCount != null) ticketCount?.text = fare.ticketCount.toString()

        if (fare.buyingDateHasMinutes) {
            buyingDate?.text = this.calendarToStringWithTime(fare.buyingDate)
        } else {
            buyingDate?.text = this.calendarToString(fare.buyingDate)
        }

        val fromDate = fare.validityFromDate
        if (fromDate != null) {
            validityFromDate?.text = this.calendarToString(fromDate)
        }

        val untilDate = fare.validityUntilDate
        if (untilDate != null) {
            validityUntilDate?.text = this.calendarToString(untilDate)
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

    private fun calendarToStringWithTime(cal: Calendar): String {
        return String.format(
            "%04d-%02d-%02d à %02d:%02d",
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH).inc(),
            cal.get(Calendar.DATE),
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE)
        )
    }
}