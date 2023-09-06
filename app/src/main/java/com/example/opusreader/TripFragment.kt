package com.example.opusreader

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

private const val ARG_LINE_SHORT_NAME = "lineLongName"
private const val ARG_LINE_LONG_NAME = "lineLongName"
private const val ARG_OPERATOR_NAME = "operatorName"
private const val ARG_BOARDING_DATE = "boardingDate"
private const val ARG_IS_TRANSFER = "isTransfer"

/**
 * A simple [Fragment] subclass.
 * Use the [TripFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TripFragment : Fragment() {
    private var operatorName: String? = null
    private var lineShortName: String? = null
    private var lineLongName: String? = null
    private var boardingDate: String? = null
    private var isTransfer: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            this.operatorName = it.getString(ARG_OPERATOR_NAME)
            this.lineShortName = it.getString(ARG_LINE_SHORT_NAME)
            this.lineLongName = it.getString(ARG_LINE_LONG_NAME)
            this.boardingDate = it.getString(ARG_BOARDING_DATE)
            this.isTransfer = it.getBoolean(ARG_IS_TRANSFER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trip, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(trip: Trip) =
            TripFragment().apply {
                arguments = Bundle().apply {
                    // TODO
                    putString(ARG_OPERATOR_NAME, "")
                    putString(ARG_LINE_SHORT_NAME, "")
                    putString(ARG_LINE_LONG_NAME, "")
                    putString(ARG_BOARDING_DATE, "")
                    putBoolean(ARG_IS_TRANSFER, false)
                }
            }
    }
}