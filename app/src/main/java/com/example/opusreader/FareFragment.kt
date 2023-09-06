package com.example.opusreader

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

private const val ARG_FARE_NAME = "lineLongName"
private const val ARG_OPERATOR_NAME = "operatorName"
private const val ARG_BUYING_DATE = "buyingDate"
private const val ARG_TICKET_COUNT = "ticketCount"
private const val ARG_VALIDITY_FROM_DATE = "buyingDate"
private const val ARG_VALIDITY_UNTIL_DATE = "buyingDate"

/**
 * A simple [Fragment] subclass.
 * Use the [FareFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FareFragment : Fragment() {
    private var fareName: String? = null
    private var operatorName: String? = null
    private var buyingDate: String? = null
    private var ticketCount: String? = null
    private var validityFromDate: String? = null
    private var validityUntilDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            this.fareName = it.getString(ARG_FARE_NAME)
            this.operatorName = it.getString(ARG_OPERATOR_NAME)
            this.buyingDate = it.getString(ARG_BUYING_DATE)
            this.ticketCount = it.getString(ARG_TICKET_COUNT)
            this.validityFromDate = it.getString(ARG_VALIDITY_FROM_DATE)
            this.validityUntilDate = it.getString(ARG_VALIDITY_UNTIL_DATE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fare, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(fare: Fare) =
            FareFragment().apply {
                // TODO
                arguments = Bundle().apply {
                    putString(ARG_FARE_NAME, "")
                    putString(ARG_OPERATOR_NAME, "")
                    putString(ARG_BUYING_DATE, "")
                    putString(ARG_TICKET_COUNT, "")
                    putString(ARG_VALIDITY_FROM_DATE, "")
                    putString(ARG_VALIDITY_UNTIL_DATE, "")
                }
            }
    }
}