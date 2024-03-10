package com.example.opusreader

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
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
        val fareProduct = IdConverter.getFareProductById(fare.typeId)
        this.addFareInfoSectionTitles(fare, fareProduct)
        this.addFareInfoSectionValues(fare, fareProduct)
    }

    private fun addFareInfoSectionTitles(fare: Fare, fareProduct: FareProduct) {
        val fareTypeTitleTv = this.mView?.findViewById<TextView>(R.id.fareTypeValueTv)
        fareTypeTitleTv?.text = fareProduct.name

        val buyingDateTitleTv = this.mView?.findViewById<TextView>(R.id.fareBuyingDateTv)
        buyingDateTitleTv?.text = getString(R.string.fare_buying_date_title)

        if (fare.validityFromDate != null) {
            val firstUseDateTitleTv = this.mView?.findViewById<TextView>(R.id.validityFromDateTv)
            firstUseDateTitleTv?.text = getString(R.string.fare_first_use_date_title)
        }

        if (fare.ticketCount != null)  {
            val ticketCountTitleTv = this.mView?.findViewById<TextView>(R.id.ticketCountTv)
            ticketCountTitleTv?.text = getString(R.string.fare_ticket_count_title)
        }
    }

    private fun addFareInfoSectionValues(fare: Fare, fareProduct: FareProduct) {
        addBuyingDate(fare)
        if (fare.ticketCount != null) addTicketCount(fare)
        val fromDate = fare.validityFromDate
        val untilDate = fare.validityUntilDate
        if (fromDate != null && untilDate != null) {
            addValidityInterval(fromDate, untilDate)
        }
        val operator = IdConverter.getOperatorById(fare.operatorId)
        addFareInfoSectionImages(operator)
        addFareDescriptionSection(fareProduct)
    }

    private fun addBuyingDate(fare: Fare) {
        val buyingDateTv = this.mView?.findViewById<TextView>(R.id.fareBuyingDateValueTv)
        if (fare.buyingDateHasMinutes) {
            buyingDateTv?.text = this.calendarToStringWithTime(fare.buyingDate)
        } else {
            buyingDateTv?.text = this.calendarToString(fare.buyingDate)
        }
    }

    private fun addTicketCount(fare: Fare) {
        val ticketCountTv = this.mView?.findViewById<TextView>(R.id.ticketCountValueTv)
        ticketCountTv?.text = fare.ticketCount.toString()

        val fareValidityColorLayout = this.mView?.findViewById<LinearLayout>(R.id.fareValidityColorLayout)
        if (fare.ticketCount == 0u) {
            fareValidityColorLayout?.setBackgroundColor(Color.parseColor("#ff0317"))
        } else {
            fareValidityColorLayout?.setBackgroundColor(Color.parseColor("#108016"))
        }
    }

    private fun addValidityInterval(fromDate: Calendar, untilDate: Calendar) {
        val validityFromDateTv = this.mView?.findViewById<TextView>(R.id.validityFromDateValueTv)
        validityFromDateTv?.text = calendarToStringInterval(fromDate, untilDate)

        val fareValidityColorLayout = this.mView?.findViewById<LinearLayout>(R.id.fareValidityColorLayout)
        if (untilDate.time < Calendar.getInstance().time) {
            fareValidityColorLayout?.setBackgroundColor(Color.parseColor("#ff0317"))
        } else {
            fareValidityColorLayout?.setBackgroundColor(Color.parseColor("#108016"))
        }
    }

    private fun addFareInfoSectionImages(operator: Operator) {
        val operatorImageView = this.mView?.findViewById<ImageView>(R.id.operatorImageView)
        operatorImageView?.setImageResource(operator.imageId)
    }

    private fun addFareDescriptionSection(fareProduct: FareProduct) {
        val transferInfoDivider = this.mView?.findViewById<View>(R.id.fareDescriptionDivider)
        transferInfoDivider?.visibility = View.GONE
        val transferInfoTv = this.mView?.findViewById<TextView>(R.id.fareDescriptionTv)
        transferInfoTv?.text = getString(fareProduct.descriptionStringId)
        transferInfoTv?.visibility = View.GONE

        val fareLayout = this.mView?.findViewById<ConstraintLayout>(R.id.fareLayout)
        fareLayout?.setOnClickListener(FareLayoutListener())
    }

    private fun calendarToString(cal: Calendar): String {
        return SimpleDateFormat(
            getString(R.string.calendar_pattern),
            Locale(getString(R.string.calendar_language), getString(R.string.calendar_country))
        ).format(cal.time)
    }

    private fun calendarToStringWithTime(cal: Calendar): String {
        return SimpleDateFormat(
            getString(R.string.calendar_with_time_pattern),
            Locale(getString(R.string.calendar_language), getString(R.string.calendar_country))
        ).format(cal.time)
    }

    private fun calendarToStringWithoutYear(cal: Calendar): String {
        return SimpleDateFormat(
            getString(R.string.calendar_without_year_pattern),
            Locale(getString(R.string.calendar_language), getString(R.string.calendar_country))
        ).format(cal.time)
    }

    private fun calendarToStringInterval(fromCal: Calendar, untilCal: Calendar): String {
        return "${calendarToStringWithoutYear(fromCal)} ${getString(R.string.calendar_interval_linking_word)} ${calendarToString(untilCal)}"
    }


    class FareLayoutListener : View.OnClickListener {
        private var isShowing: Boolean = false

        override fun onClick(view: View) {
            if (isShowing) {
                hideFareTransferInfoSection(view)
            } else {
                showFareTransferInfoSection(view)
            }

            isShowing = !isShowing
        }

        private fun showFareTransferInfoSection(view: View) {
            val transferInfoDivider = view.findViewById<View>(R.id.fareDescriptionDivider)
            val transferInfoTv = view.findViewById<TextView>(R.id.fareDescriptionTv)
            transferInfoDivider?.visibility = View.VISIBLE
            transferInfoTv?.visibility = View.VISIBLE
        }

        private fun hideFareTransferInfoSection(view: View) {
            val transferInfoDivider = view.findViewById<View>(R.id.fareDescriptionDivider)
            val transferInfoTv = view.findViewById<TextView>(R.id.fareDescriptionTv)
            transferInfoDivider?.visibility = View.GONE
            transferInfoTv?.visibility = View.GONE
        }
    }
}