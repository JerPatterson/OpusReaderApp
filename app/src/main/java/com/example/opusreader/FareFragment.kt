package com.example.opusreader

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.Gson

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
        this.addFareInfoSectionTitles(fare.typeId)
    }

    private fun addFareInfoSectionTitles(typeId: UInt) {
        val title = this.mView?.findViewById<TextView>(R.id.fareTypeValueTv)
        val buyingDate = this.mView?.findViewById<TextView>(R.id.fareBuyingDateTv)
        val firstUseDate = this.mView?.findViewById<TextView>(R.id.firstUseDateTv)
        val validityUntilDate = this.mView?.findViewById<TextView>(R.id.validityUntilDateTv)

        title?.text = "Inconnu ($typeId)"
        buyingDate?.text = "Acheté le"
        firstUseDate?.text = "Passage utilisé le"
        validityUntilDate?.text = "Valide jusqu'au"
    }
}