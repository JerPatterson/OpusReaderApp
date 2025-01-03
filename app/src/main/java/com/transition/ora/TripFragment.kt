package com.transition.ora

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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

private const val ARG_ID = "id"
private const val ARG_TRIP = "trip"


/**
 * A simple [Fragment] subclass.
 * Use the [TripFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TripFragment : Fragment() {
    private var mView: View? = null
    private var id: ULong? = null
    private var trip: Trip? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            this.id = it.getString(ARG_ID)?.toULong()
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
        fun newInstance(id: ULong, trip: Trip) =
            TripFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ID, id.toString())
                    putString(ARG_TRIP, Gson().toJson(trip))
                }
            }
    }

    private fun addTripInfoSection(trip: Trip) {
        this.addTripInfoSectionTitles()
        this.addTripInfoSectionValues(trip)
    }

    private fun addTripInfoSectionTitles() {
        val boardingDateTitleTv = this.mView?.findViewById<TextView>(R.id.tripBoardingDateTv)
        val validityFromDateTitleTv = this.mView?.findViewById<TextView>(R.id.tripValidityFromDateTv)
        boardingDateTitleTv?.text = getString(R.string.trip_boarding_date_title)
        validityFromDateTitleTv?.text = getString(R.string.trip_validity_from_date_title)
    }

    private fun addTripInfoSectionValues(trip: Trip) {
        val line = CardContentConverter.getLineById(requireContext(), trip.operatorId, trip.lineId)
        val operator = CardContentConverter.getOperatorById(trip.operatorId)
        addTripLine(line)
        addTripDates(trip)
        addTripInfoSectionImages(operator, line)
        addTripCrowdSourceSection(trip, line)
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

    private fun addTripCrowdSourceSection(trip: Trip, line: Line) {
        val tripCrowdSourceDivider = this.mView?.findViewById<View>(R.id.tripCrowdSourceDivider)
        tripCrowdSourceDivider?.visibility = View.GONE
        val tripCrowdSourceIcon = this.mView?.findViewById<View>(R.id.tripCrowdSourceImageView)
        tripCrowdSourceIcon?.visibility = View.GONE
        val tripCrowdSourceTitle = this.mView?.findViewById<TextView>(R.id.tripCrowdSourceTitle)
        tripCrowdSourceTitle?.visibility = View.GONE
        val tripCrowdSourceDescription = this.mView?.findViewById<TextView>(R.id.tripCrowdSourceDescription)
        tripCrowdSourceDescription?.visibility = View.GONE
        val tripCrowdSourceSpinner = this.mView?.findViewById<Spinner>(R.id.tripCrowdSourceSpinner)
        tripCrowdSourceSpinner?.visibility = View.GONE
        val tripCrowdSourceSwitch = this.mView?.findViewById<SwitchCompat>(R.id.tripCrowdSourceSwitch)
        tripCrowdSourceSwitch?.visibility = View.GONE
        val tripCrowdSourceConfirmButton = this.mView?.findViewById<Button>(R.id.tripCrowdSourceConfirmButton)
        tripCrowdSourceConfirmButton?.visibility = View.GONE

        val tripLayout = this.mView?.findViewById<ConstraintLayout>(R.id.tripLayout)
        tripLayout?.setOnClickListener(this.id?.let { TripLayoutListener(it, trip, line, this.requireContext()) })
    }

    private fun calendarToStringWithTime(cal: Calendar): String {
        return SimpleDateFormat(
            getString(R.string.calendar_with_time_pattern),
            Locale(getString(R.string.calendar_language), getString(R.string.calendar_country))
        ).format(cal.time)
    }


    class TripLayoutListener(
        private val id: ULong,
        private val trip: Trip,
        private val line: Line,
        private val context: Context
    ) : View.OnClickListener {
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
            val tripCrowdSourceIcon = view.findViewById<ImageView>(R.id.tripCrowdSourceImageView)
            val tripCrowdSourceTitle = view.findViewById<TextView>(R.id.tripCrowdSourceTitle)
            val tripCrowdSourceDescription = view.findViewById<TextView>(R.id.tripCrowdSourceDescription)
            val tripCrowdSourceSpinner = view.findViewById<Spinner>(R.id.tripCrowdSourceSpinner)
            val tripCrowdSourceSwitch = view.findViewById<SwitchCompat>(R.id.tripCrowdSourceSwitch)
            val tripCrowdSourceConfirmButton = view.findViewById<Button>(R.id.tripCrowdSourceConfirmButton)
            tripCrowdSourceDivider?.visibility = View.VISIBLE
            tripCrowdSourceIcon?.visibility = View.VISIBLE
            tripCrowdSourceTitle?.visibility = View.VISIBLE
            tripCrowdSourceDescription?.visibility = View.VISIBLE
            tripCrowdSourceSpinner?.visibility = View.VISIBLE
            tripCrowdSourceSwitch?.visibility = View.VISIBLE
            tripCrowdSourceConfirmButton?.visibility = View.VISIBLE

            if (!hasAddedOptions) {
                tripCrowdSourceSwitch.setOnCheckedChangeListener { _, isChecked ->
                    addOptionsToCrowdSourceSpinner(view, !isChecked)
                }
                addOptionsToCrowdSourceSpinner(view, true)
            }

            enableCrowdSourceConfirmButton(view)
        }

        private fun hideTripCrowdSourceSection(view: View) {
            val tripCrowdSourceDivider = view.findViewById<View>(R.id.tripCrowdSourceDivider)
            val tripCrowdSourceIcon = view.findViewById<ImageView>(R.id.tripCrowdSourceImageView)
            val tripCrowdSourceTitle = view.findViewById<TextView>(R.id.tripCrowdSourceTitle)
            val tripCrowdSourceDescription = view.findViewById<TextView>(R.id.tripCrowdSourceDescription)
            val tripCrowdSourceSpinner = view.findViewById<Spinner>(R.id.tripCrowdSourceSpinner)
            val tripCrowdSourceSwitch = view.findViewById<SwitchCompat>(R.id.tripCrowdSourceSwitch)
            val tripCrowdSourceConfirmButton = view.findViewById<Button>(R.id.tripCrowdSourceConfirmButton)
            tripCrowdSourceDivider?.visibility = View.GONE
            tripCrowdSourceIcon?.visibility = View.GONE
            tripCrowdSourceTitle?.visibility = View.GONE
            tripCrowdSourceDescription?.visibility = View.GONE
            tripCrowdSourceSpinner?.visibility = View.GONE
            tripCrowdSourceSwitch?.visibility = View.GONE
            tripCrowdSourceConfirmButton?.visibility = View.GONE
        }

        private fun addOptionsToCrowdSourceSpinner(view: View, filterKnownLines: Boolean) {
            val db = Firebase.firestore
            val document = db.collection("operators").document(this.trip.operatorId.toString())

            val options = arrayListOf<LineFirestore>()
            if (line.id != "?") {
                options.add(
                    LineFirestore(
                        line.id,
                        trip.lineId.toString(),
                        line.name,
                        line.color,
                        line.textColor
                    )
                )
            }
            options.add(
                LineFirestore(
                    "?",
                    "",
                    this.context.getString(R.string.line_missing_option_name),
                    "#000000",
                    "#ffffff",
                )
            )

            val crowdSourceSpinner = view.findViewById<Spinner>(R.id.tripCrowdSourceSpinner)
            crowdSourceSpinner?.adapter = LineCrowdSrcAdapter(this.context, options)
            crowdSourceSpinner.onItemSelectedListener = SpinnerSelectListener(view, options)

            try {
                document.get(firestoreSource).addOnSuccessListener { documentSnapshot ->
                    firestoreSource = Source.CACHE
                    val operator = documentSnapshot.toObject(OperatorFirestore::class.java)
                    operator?.lines?.forEach { line ->
                        if (line.idOnCard != trip.lineId.toString()
                            && !filterKnownLines || line.idOnCard == "") {
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

        private fun enableCrowdSourceConfirmButton(view: View) {
            val confirmButton: Button = view.findViewById(R.id.tripCrowdSourceConfirmButton)
            confirmButton.setOnClickListener(CrowdSourceConfirmListener(view, this.id, this.trip))
        }

        class CrowdSourceConfirmListener(
            private val parent: View,
            private val id: ULong,
            private val trip: Trip,
        ) : View.OnClickListener {
            override fun onClick(view: View) {
                val tripCrowdSourceSpinner = parent.findViewById<Spinner>(R.id.tripCrowdSourceSpinner)
                val selectedLineId = (tripCrowdSourceSpinner.selectedItem as LineFirestore).id
                val selectedLineName = (tripCrowdSourceSpinner.selectedItem as LineFirestore).name

                if (selectedLineId == "?") {
                    val linearInputLayout = LinearLayout(view.context)
                    linearInputLayout.orientation = LinearLayout.VERTICAL
                    val lineIdInputLayout = TextInputLayout(linearInputLayout.context)
                    val lineNameInputLayout = TextInputLayout(linearInputLayout.context)
                    val lineIdInput = TextInputEditText(linearInputLayout.context)
                    val lineNameInput = TextInputEditText(linearInputLayout.context)

                    val padding = view.context.resources.getDimension(R.dimen.line_missing_input_padding).roundToInt()
                    lineIdInputLayout.setPadding(padding, 0, padding, 0)
                    lineIdInputLayout.hint = view.context.getString(R.string.line_id_input_hint)
                    lineIdInputLayout.addView(lineIdInput)
                    lineNameInputLayout.setPadding(padding, padding / 2, padding, 0)
                    lineNameInputLayout.hint = view.context.getString(R.string.line_name_input_hint)
                    lineNameInputLayout.addView(lineNameInput)

                    linearInputLayout.addView(lineIdInputLayout)
                    linearInputLayout.addView(lineNameInputLayout)

                    val builder = AlertDialog.Builder(view.context)
                    builder.setTitle(view.context.getString(R.string.line_missing_alert_title))
                        .setMessage(view.context.getString(R.string.line_missing_alert_message))
                        .setView(linearInputLayout)
                        .setPositiveButton(view.context.getString(R.string.submit)) { _, _ ->
                            this.completeCrowdSourceEvent(
                                view,
                                lineIdInput.text.toString(),
                                lineNameInput.text.toString()
                            )
                        }
                        .setNegativeButton(view.context.getString(R.string.cancel)) { _, _ -> }

                    val dialog = builder.create()
                    dialog.show()

                } else if (selectedLineId != null && selectedLineName != null) {
                    this.completeCrowdSourceEvent(view, selectedLineId, selectedLineName)
                }
            }

            private fun completeCrowdSourceEvent(
                view: View,
                selectedLineId: String,
                selectedLineName: String
            ) {
                val tripCrowdSourceSpinner = parent.findViewById<Spinner>(R.id.tripCrowdSourceSpinner)

                try {
                    val db = Firebase.firestore
                    val document = db.collection("operators")
                        .document(trip.operatorId.toString())
                        .collection("line-propositions")
                        .document(id.toString() + "_" + trip.lineId)

                    val data = hashMapOf(
                        "id" to selectedLineId,
                        "idOnCard" to trip.lineId.toString(),
                        "name" to selectedLineName,
                        "color" to (tripCrowdSourceSpinner.selectedItem as LineFirestore).color,
                        "textColor" to (tripCrowdSourceSpinner.selectedItem as LineFirestore).textColor,
                    )
                    document.set(data)

                    val localDb = CardDatabase.getInstance(view.context)
                    CoroutineScope(Dispatchers.IO).launch {
                        localDb.daoProposition.insertStoredProposition(
                            CardPropositionEntity(
                                trip.operatorId.toString(),
                                trip.lineId.toString(),
                                "line",
                                selectedLineId,
                                selectedLineName,
                                (tripCrowdSourceSpinner.selectedItem as LineFirestore).color ?: "#000000",
                                (tripCrowdSourceSpinner.selectedItem as LineFirestore).textColor ?: "#ffffff",
                            )
                        )
                    }

                    val builder = AlertDialog.Builder(view.context)
                    builder.setTitle(R.string.proposition_alert_title)
                        .setMessage(R.string.proposition_alert_message)
                        .setPositiveButton(R.string.accept) { _, _ ->
                            parent.findViewById<ConstraintLayout>(R.id.tripLayout).callOnClick()
                        }
                    val dialog = builder.create()
                    dialog.show()

                } catch(_: Error) {
                    val builder = AlertDialog.Builder(view.context)
                    builder.setTitle(R.string.proposition_error_title)
                        .setMessage(R.string.proposition_error_message)
                        .setPositiveButton(R.string.accept) { _, _ ->
                            parent.findViewById<ConstraintLayout>(R.id.tripLayout).callOnClick()
                        }
                    val dialog = builder.create()
                    dialog.show()
                }
            }
        }
    }
}