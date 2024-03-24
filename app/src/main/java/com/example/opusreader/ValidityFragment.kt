package com.example.opusreader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson


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
        val validityStatusTitleTv = this.mView?.findViewById<TextView>(R.id.validityTimeRemainingTitleTv)
        validityStartingTitleTv?.text = getString(R.string.validity_starting_on)
        validityEndingTitleTv?.text = getString(R.string.validity_ending_on)
        validityStatusTitleTv?.text = getString(R.string.validity_still_valid_for)
    }

    private fun addValidityInfoSectionValues(card: Card) {

    }
}