package com.example.opusreader

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
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
        this.addTripCrowdSourceSection(trip)
    }

    private fun addTripInfoSectionTitles() {
        val boardingDateTitleTv = this.mView?.findViewById<TextView>(R.id.tripBoardingDateTv)
        val validityFromDateTitleTv = this.mView?.findViewById<TextView>(R.id.tripValidityFromDateTv)
        boardingDateTitleTv?.text = getString(R.string.trip_boarding_date_title)
        validityFromDateTitleTv?.text = getString(R.string.trip_validity_from_date_title)
    }

    private fun addTripInfoSectionValues(trip: Trip) {
        val line = CardContentConverter.getLineById(trip.operatorId, trip.lineId)
        val operator = CardContentConverter.getOperatorById(trip.operatorId)
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

    private fun addTripCrowdSourceSection(trip: Trip) {
        val tripCrowdSourceDivider = this.mView?.findViewById<View>(R.id.tripCrowdSourceDivider)
        tripCrowdSourceDivider?.visibility = View.GONE
        val tripCrowdSourceSpinner = this.mView?.findViewById<Spinner>(R.id.tripCrowdSourceSpinner)
        tripCrowdSourceSpinner?.visibility = View.GONE
        val tripCrowdSourceSwitch = this.mView?.findViewById<SwitchCompat>(R.id.tripCrowdSourceSwitch)
        tripCrowdSourceSwitch?.visibility = View.GONE
        val tripCrowdSourceConfirmButton = this.mView?.findViewById<Button>(R.id.tripCrowdSourceConfirmButton)
        tripCrowdSourceConfirmButton?.visibility = View.GONE

        val tripLayout = this.mView?.findViewById<ConstraintLayout>(R.id.tripLayout)
        tripLayout?.setOnClickListener(TripLayoutListener(trip, this.requireContext()))
    }

    private fun calendarToStringWithTime(cal: Calendar): String {
        return SimpleDateFormat(
            getString(R.string.calendar_with_time_pattern),
            Locale(getString(R.string.calendar_language), getString(R.string.calendar_country))
        ).format(cal.time)
    }


    class TripLayoutListener(private val trip: Trip, private val context: Context) : View.OnClickListener {
        private var isShowing: Boolean = false
        private var hasAddedOptions: Boolean = false
        private var firestoreSource: Source = Source.DEFAULT

        override fun onClick(view: View) {
            if (isShowing) {
                hideTripCrowdSourceSection(view)
            } else {
                showTripCrowdSourceSection(view)
            }

            isShowing = !isShowing
        }

        private fun showTripCrowdSourceSection(view: View) {
            val tripCrowdSourceDivider = view.findViewById<View>(R.id.tripCrowdSourceDivider)
            val tripCrowdSourceSpinner = view.findViewById<Spinner>(R.id.tripCrowdSourceSpinner)
            val tripCrowdSourceSwitch = view.findViewById<SwitchCompat>(R.id.tripCrowdSourceSwitch)
            val tripCrowdSourceConfirmButton = view.findViewById<Button>(R.id.tripCrowdSourceConfirmButton)
            tripCrowdSourceDivider?.visibility = View.VISIBLE
            tripCrowdSourceSpinner?.visibility = View.VISIBLE
            tripCrowdSourceSwitch?.visibility = View.VISIBLE
            tripCrowdSourceConfirmButton?.visibility = View.VISIBLE

            if (!hasAddedOptions) {
                tripCrowdSourceSwitch.setOnCheckedChangeListener { _, isChecked ->
                    addOptionsToCrowdSourceSpinner(view, !isChecked)
                }
                addOptionsToCrowdSourceSpinner(view, true)
            }
        }

        private fun hideTripCrowdSourceSection(view: View) {
            val tripCrowdSourceDivider = view.findViewById<View>(R.id.tripCrowdSourceDivider)
            val tripCrowdSourceSpinner = view.findViewById<Spinner>(R.id.tripCrowdSourceSpinner)
            val tripCrowdSourceSwitch = view.findViewById<SwitchCompat>(R.id.tripCrowdSourceSwitch)
            val tripCrowdSourceConfirmButton = view.findViewById<Button>(R.id.tripCrowdSourceConfirmButton)
            tripCrowdSourceDivider?.visibility = View.GONE
            tripCrowdSourceSpinner?.visibility = View.GONE
            tripCrowdSourceSwitch?.visibility = View.GONE
            tripCrowdSourceConfirmButton?.visibility = View.GONE
        }

        private fun addOptionsToCrowdSourceSpinner(view: View, filterKnownLines: Boolean) {
            val db = Firebase.firestore
            val document = db.collection("operators").document(this.trip.operatorId.toString())

            val options = arrayListOf(
                LineFirestore(
                    "?",
                    "",
                    this.context.getString(R.string.line_missing_from_proposition),
                    "#000000",
                    "#ffffff"
                ),
            )

            val crowdSourceSpinner = view.findViewById<Spinner>(R.id.tripCrowdSourceSpinner)
            crowdSourceSpinner?.adapter = LineCrowdSrcAdapter(this.context, options)
            crowdSourceSpinner.onItemSelectedListener = SpinnerSelectListener(view, options)

            try {
                document.get(firestoreSource).addOnSuccessListener { documentSnapshot ->
                    firestoreSource = Source.CACHE
                    val operator = documentSnapshot.toObject(OperatorFirestore::class.java)
                    operator?.lines?.forEach { line ->
                        if (line.idOnCard == trip.lineId.toString()) {
                            crowdSourceSpinner.setSelection(options.size)
                            options.add(line)
                        } else if (!filterKnownLines || line.idOnCard == "") {
                            options.add(line)
                        }
                    }
                }
            } catch (_: Error) { }
        }

        private class SpinnerSelectListener(
            private val fragmentView: View,
            private val options: ArrayList<LineFirestore>
        ): AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) return

                val lineIdTv = this.fragmentView.findViewById<TextView>(R.id.tripLineIdTv)
                val name = this.fragmentView.findViewById<TextView>(R.id.tripLineNameTv)
                lineIdTv?.text = options[position].id
                name?.text = options[position].name

                val textColor = Color.parseColor(options[position].textColor)
                lineIdTv?.setTextColor(textColor)
                val background = Color.parseColor(options[position].color)
                val tripColorLayout = this.fragmentView.findViewById<LinearLayout>(R.id.tripColorLayout)
                lineIdTv?.setBackgroundColor(background)
                tripColorLayout?.setBackgroundColor(background)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}