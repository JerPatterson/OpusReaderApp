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
private const val ARG_FARE = "fare"


/**
 * A simple [Fragment] subclass.
 * Use the [FareFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FareFragment : Fragment() {
    private var mView: View? = null
    private var id: ULong? = null
    private var fare: Fare? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            this.id = it.getString(ARG_ID)?.toULong()
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
        fun newInstance(id: ULong, fare: Fare) =
            FareFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ID, id.toString())
                    putString(ARG_FARE, Gson().toJson(fare))
                }
            }
    }

    private fun addFareInfoSection(fare: Fare) {
        val fareProduct = CardContentConverter.getFareProductById(requireContext(), fare.operatorId, fare.typeId)
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
        val operator = CardContentConverter.getOperatorById(fare.operatorId)
        addFareInfoSectionImages(operator)
        addFareDescriptionSection(fare, fareProduct)
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

    private fun addFareDescriptionSection(fare: Fare, fareProduct: FareProduct) {
        val transferInfoDivider = this.mView?.findViewById<View>(R.id.fareDescriptionDivider)
        transferInfoDivider?.visibility = View.GONE
        val transferInfoIcon = this.mView?.findViewById<ImageView>(R.id.fareDescriptionImageView)
        transferInfoIcon?.visibility = View.GONE
        val transferInfoTitle = this.mView?.findViewById<TextView>(R.id.fareDescriptionTitle)
        transferInfoTitle?.visibility = View.GONE
        val transferInfoTv = this.mView?.findViewById<TextView>(R.id.fareDescriptionTv)
        transferInfoTv?.text = getString(fareProduct.descriptionStringId)
        transferInfoTv?.visibility = View.GONE

        val fareCrowdSourceDivider = this.mView?.findViewById<View>(R.id.fareCrowdSourceDivider)
        fareCrowdSourceDivider?.visibility = View.GONE
        val fareCrowdSourceIcon = this.mView?.findViewById<View>(R.id.fareCrowdSourceImageView)
        fareCrowdSourceIcon?.visibility = View.GONE
        val fareCrowdSourceTitle = this.mView?.findViewById<TextView>(R.id.fareCrowdSourceTitle)
        fareCrowdSourceTitle?.visibility = View.GONE
        val fareCrowdSourceDescription = this.mView?.findViewById<TextView>(R.id.fareCrowdSourceDescription)
        fareCrowdSourceDescription?.visibility = View.GONE
        val fareCrowdSourceSpinner = this.mView?.findViewById<Spinner>(R.id.fareCrowdSourceSpinner)
        fareCrowdSourceSpinner?.visibility = View.GONE
        val fareCrowdSourceSwitch = this.mView?.findViewById<SwitchCompat>(R.id.fareCrowdSourceSwitch)
        fareCrowdSourceSwitch?.visibility = View.GONE
        val fareCrowdSourceConfirmButton = this.mView?.findViewById<Button>(R.id.fareCrowdSourceConfirmButton)
        fareCrowdSourceConfirmButton?.visibility = View.GONE

        val fareLayout = this.mView?.findViewById<ConstraintLayout>(R.id.fareLayout)
        fareLayout?.setOnClickListener(this.id?.let { FareLayoutListener(it, fare, fareProduct, this.requireContext()) })
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


    class FareLayoutListener(
        private val id: ULong,
        private val fare: Fare,
        private val fareProduct: FareProduct,
        private val context: Context
    ) : View.OnClickListener {
        private var isShowing: Boolean = false
        private var hasAddedOptions: Boolean = false
        private var firestoreSource: Source = Source.DEFAULT

        override fun onClick(view: View) {
            if (isShowing) {
                hideFareTransferInfoSection(view)
                hideFareCrowdSourceSection(view)
            } else {
                showFareTransferInfoSection(view)
                showFareCrowdSourceSection(view)
            }

            isShowing = !isShowing
        }

        private fun showFareTransferInfoSection(view: View) {
            val transferInfoDivider = view.findViewById<View>(R.id.fareDescriptionDivider)
            val transferInfoIcon = view.findViewById<ImageView>(R.id.fareDescriptionImageView)
            val transferInfoTitle = view.findViewById<TextView>(R.id.fareDescriptionTitle)
            val transferInfoTv = view.findViewById<TextView>(R.id.fareDescriptionTv)
            transferInfoDivider?.visibility = View.VISIBLE
            transferInfoIcon?.visibility = View.VISIBLE
            transferInfoTitle?.visibility = View.VISIBLE
            transferInfoTv?.visibility = View.VISIBLE
        }

        private fun hideFareTransferInfoSection(view: View) {
            val transferInfoDivider = view.findViewById<View>(R.id.fareDescriptionDivider)
            val transferInfoIcon = view.findViewById<ImageView>(R.id.fareDescriptionImageView)
            val transferInfoTitle = view.findViewById<TextView>(R.id.fareDescriptionTitle)
            val transferInfoTv = view.findViewById<TextView>(R.id.fareDescriptionTv)
            transferInfoDivider?.visibility = View.GONE
            transferInfoIcon?.visibility = View.GONE
            transferInfoTitle?.visibility = View.GONE
            transferInfoTv?.visibility = View.GONE
        }

        private fun showFareCrowdSourceSection(view: View) {
            val fareCrowdSourceDivider = view.findViewById<View>(R.id.fareCrowdSourceDivider)
            val fareCrowdSourceIcon = view.findViewById<ImageView>(R.id.fareCrowdSourceImageView)
            val fareCrowdSourceTitle = view.findViewById<TextView>(R.id.fareCrowdSourceTitle)
            val fareCrowdSourceDescription = view.findViewById<TextView>(R.id.fareCrowdSourceDescription)
            val fareCrowdSourceSpinner = view.findViewById<Spinner>(R.id.fareCrowdSourceSpinner)
            val fareCrowdSourceSwitch = view.findViewById<SwitchCompat>(R.id.fareCrowdSourceSwitch)
            val fareCrowdSourceConfirmButton = view.findViewById<Button>(R.id.fareCrowdSourceConfirmButton)
            fareCrowdSourceDivider?.visibility = View.VISIBLE
            fareCrowdSourceIcon?.visibility = View.VISIBLE
            fareCrowdSourceTitle?.visibility = View.VISIBLE
            fareCrowdSourceDescription?.visibility = View.VISIBLE
            fareCrowdSourceSpinner?.visibility = View.VISIBLE
            fareCrowdSourceSwitch?.visibility = View.VISIBLE
            fareCrowdSourceConfirmButton?.visibility = View.VISIBLE

            if (!hasAddedOptions) {
                fareCrowdSourceSwitch.setOnCheckedChangeListener { _, isChecked ->
                    addOptionsToCrowdSourceSpinner(view, !isChecked)
                }
                addOptionsToCrowdSourceSpinner(view, true)
            }

            enableCrowdSourceConfirmButton(view)
        }

        private fun hideFareCrowdSourceSection(view: View) {
            val fareCrowdSourceDivider = view.findViewById<View>(R.id.fareCrowdSourceDivider)
            val fareCrowdSourceIcon = view.findViewById<ImageView>(R.id.fareCrowdSourceImageView)
            val fareCrowdSourceTitle = view.findViewById<TextView>(R.id.fareCrowdSourceTitle)
            val fareCrowdSourceDescription = view.findViewById<TextView>(R.id.fareCrowdSourceDescription)
            val fareCrowdSourceSpinner = view.findViewById<Spinner>(R.id.fareCrowdSourceSpinner)
            val fareCrowdSourceSwitch = view.findViewById<SwitchCompat>(R.id.fareCrowdSourceSwitch)
            val fareCrowdSourceConfirmButton = view.findViewById<Button>(R.id.fareCrowdSourceConfirmButton)
            fareCrowdSourceDivider?.visibility = View.GONE
            fareCrowdSourceIcon?.visibility = View.GONE
            fareCrowdSourceTitle?.visibility = View.GONE
            fareCrowdSourceDescription?.visibility = View.GONE
            fareCrowdSourceSpinner?.visibility = View.GONE
            fareCrowdSourceSwitch?.visibility = View.GONE
            fareCrowdSourceConfirmButton?.visibility = View.GONE
        }

        private fun addOptionsToCrowdSourceSpinner(view: View, filterKnownFares: Boolean) {
            val db = Firebase.firestore
            val document = db.collection("operators").document(this.fare.operatorId.toString())

            val options = arrayListOf<FareFirestore>()
            if (!fareProduct.name.startsWith("Unknown")) {
                options.add(
                    FareFirestore(
                        null,
                        fareProduct.name,
                        null
                    )
                )
            }
            options.add(
                FareFirestore(
                    null,
                    this.context.getString(R.string.fare_missing_option_name),
                    null
                )
            )

            val crowdSourceSpinner = view.findViewById<Spinner>(R.id.fareCrowdSourceSpinner)
            crowdSourceSpinner?.adapter = FareCrowdSrcAdapter(this.context, options)
            crowdSourceSpinner.onItemSelectedListener = SpinnerSelectListener(view, options)

            try {
                document.get(firestoreSource).addOnSuccessListener { documentSnapshot ->
                    firestoreSource = Source.CACHE
                    val operator = documentSnapshot.toObject(OperatorFirestore::class.java)
                    operator?.fares?.forEach { fare ->
                        if (fare.name != fareProduct.name && !filterKnownFares
                            || (fare.idOnCard?.size ?: 0) < (fare.nbOfVariations?.toInt() ?: 0)
                        ) {
                            options.add(fare)
                        }
                    }
                }
            } catch (_: Error) { }
        }

        private class SpinnerSelectListener(
            private val fragmentView: View,
            private val options: ArrayList<FareFirestore>
        ): AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) return

                val name = this.fragmentView.findViewById<TextView>(R.id.fareTypeValueTv)
                name?.text = options[position].name
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        private fun enableCrowdSourceConfirmButton(view: View) {
            val confirmButton: Button = view.findViewById(R.id.fareCrowdSourceConfirmButton)
            confirmButton.setOnClickListener(CrowdSourceConfirmListener(view, this.id, this.fare))
        }

        class CrowdSourceConfirmListener(
            private val parent: View,
            private val id: ULong,
            private val fare: Fare,
        ) : View.OnClickListener {
            override fun onClick(view: View) {
                val fareCrowdSourceSpinner =
                    parent.findViewById<Spinner>(R.id.fareCrowdSourceSpinner)
                val selectedFareName = (fareCrowdSourceSpinner.selectedItem as FareFirestore).name

                if (selectedFareName == parent.context.getString(R.string.fare_missing_option_name)) {
                    val linearInputLayout = LinearLayout(view.context)
                    linearInputLayout.orientation = LinearLayout.VERTICAL
                    val fareNameInputLayout = TextInputLayout(linearInputLayout.context)
                    val fareNameInput = TextInputEditText(linearInputLayout.context)

                    val padding =
                        view.context.resources.getDimension(R.dimen.fare_missing_input_padding)
                            .roundToInt()
                    fareNameInputLayout.setPadding(padding, 0, padding, 0)
                    fareNameInputLayout.hint = view.context.getString(R.string.fare_name_input_hint)
                    fareNameInputLayout.addView(fareNameInput)

                    linearInputLayout.addView(fareNameInputLayout)

                    val builder = AlertDialog.Builder(view.context)
                    builder.setTitle(view.context.getString(R.string.fare_missing_alert_title))
                        .setMessage(view.context.getString(R.string.fare_missing_alert_message))
                        .setView(linearInputLayout)
                        .setPositiveButton(view.context.getString(R.string.submit)) { _, _ ->
                            this.completeCrowdSourceEvent(
                                view,
                                fareNameInput.text.toString()
                            )
                        }
                        .setNegativeButton(view.context.getString(R.string.cancel)) { _, _ -> }

                    val dialog = builder.create()
                    dialog.show()

                } else if (selectedFareName != null) {
                    this.completeCrowdSourceEvent(view, selectedFareName)
                }
            }

            private fun completeCrowdSourceEvent(
                view: View,
                selectedFareName: String
            ) {
                val fareCrowdSourceSpinner =
                    parent.findViewById<Spinner>(R.id.fareCrowdSourceSpinner)

                try {
                    val db = Firebase.firestore
                    val document = db.collection("operators")
                        .document(fare.operatorId.toString())
                        .collection("fare-propositions")
                        .document(id.toString() + "_" + fare.typeId)

                    val idOnCard = ((fareCrowdSourceSpinner.selectedItem as FareFirestore).idOnCard ?: listOf()) + fare.typeId.toString()
                    val data = hashMapOf(
                        "idOnCard" to idOnCard,
                        "name" to selectedFareName,
                        "nbOfVariations" to ((fareCrowdSourceSpinner.selectedItem as FareFirestore)
                            .nbOfVariations?.toInt() ?: 0).coerceAtLeast(idOnCard.size),
                    )
                    document.set(data)

                    val localDb = CardDatabase.getInstance(view.context)
                    CoroutineScope(Dispatchers.IO).launch {
                        localDb.daoProposition.insertStoredProposition(
                            CardPropositionEntity(
                                fare.operatorId.toString(),
                                fare.typeId.toString(),
                                "fare",
                                "",
                                selectedFareName,
                                "",
                                "",
                            )
                        )
                    }

                    val builder = AlertDialog.Builder(view.context)
                    builder.setTitle(R.string.proposition_alert_title)
                        .setMessage(R.string.proposition_alert_message)
                        .setPositiveButton(R.string.accept) { _, _ ->
                            parent.findViewById<ConstraintLayout>(R.id.fareLayout).callOnClick()
                        }
                    val dialog = builder.create()
                    dialog.show()

                } catch (_: Error) {
                    val builder = AlertDialog.Builder(view.context)
                    builder.setTitle(R.string.proposition_error_title)
                        .setMessage(R.string.proposition_error_message)
                        .setPositiveButton(R.string.accept) { _, _ ->
                            parent.findViewById<ConstraintLayout>(R.id.fareLayout).callOnClick()
                        }
                    val dialog = builder.create()
                    dialog.show()
                }
            }
        }
    }
}