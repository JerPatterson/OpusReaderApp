package com.transition.ora.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.transition.ora.database.CardDatabase
import com.transition.ora.R
import com.transition.ora.enums.CardType
import com.transition.ora.services.NotificationScheduler
import com.transition.ora.types.Card
import com.transition.ora.types.Fare
import com.transition.ora.types.Trip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class HistoryCardAdapter(
    private val historyList: ArrayList<Card>,
): RecyclerView.Adapter<HistoryCardAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.history_card_row, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onViewDetachedFromWindow(holder: MyViewHolder) {
        holder.scanListRecyclerView.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.cardIdValueTv.text = historyList[position].id.toString()
        holder.cardTypeValueTv.text = holder.getCardTypeValue(historyList[position].type)
        holder.lastScanTimeValueTv.text = holder.calendarToStringWithTime(historyList[position].scanDate)
        holder.cardImageView.setImageResource(getImageResource(historyList[position].type))

        holder.itemView.setOnClickListener(HistoryItemListener(historyList[position], holder, this))
        holder.deleteItemIcon.setOnClickListener(HistoryItemDeleteListener(historyList[position],holder, this))
    }

    private fun removeItem(position: Int) {
        historyList.removeAt(position)
        notifyItemRemoved(position)
    }


    private fun getImageResource(type: CardType): Int {
        return when (type) {
            CardType.Opus -> R.drawable.opus
            CardType.Occasional -> R.drawable.occasionnelle
            CardType.OccasionalRTC -> R.drawable.occasionnelle_rtc
            CardType.OccasionalSTLevis -> R.drawable.occasionnelle_stlevis
        }
    }


    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardImageView: ImageView = itemView.findViewById(R.id.historyCardImageView)
        val cardTypeValueTv: TextView = itemView.findViewById(R.id.historyCardTypeTv)
        val cardIdValueTv: TextView = itemView.findViewById(R.id.historyCardIdTv)
        val lastScanTimeValueTv: TextView = itemView.findViewById(R.id.historyLastScanTimeValueTv)
        val deleteItemIcon: ImageView = itemView.findViewById(R.id.deleteItemHistoryIcon)
        val scanListRecyclerView: RecyclerView = itemView.findViewById(R.id.historyScanList)

        fun getCardTypeValue(type: CardType): String {
            return when (type) {
                CardType.Opus -> getString(itemView.context, R.string.opus_card_name)
                CardType.Occasional -> getString(itemView.context, R.string.occasional_card_name)
                CardType.OccasionalRTC -> getString(itemView.context, R.string.occasional_card_name)
                CardType.OccasionalSTLevis -> getString(itemView.context, R.string.occasional_card_name)
            }
        }

        fun calendarToStringWithTime(cal: Calendar): String {
            val locale = Locale.Builder()
                .setLanguage(getString(itemView.context, R.string.calendar_language))
                .setRegion(getString(itemView.context, R.string.calendar_country))
                .build()

            return SimpleDateFormat(
                getString(itemView.context, R.string.calendar_with_time_pattern), locale
            ).format(cal.time)
        }
    }

    class HistoryItemListener(
        private val card: Card,
        private val holder: MyViewHolder,
        private val adapter: HistoryCardAdapter,
    ) : View.OnClickListener {
        private var isShowing: Boolean = false
        private var isInitialized: Boolean = false

        override fun onClick(view: View) {
            val scanListRecyclerView: RecyclerView = view.findViewById(R.id.historyScanList)

            if (!isInitialized) {
                scanListRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
                scanListRecyclerView.adapter = HistoryScanAdapter(
                    getScanListItems(card, holder.itemView.context),
                    holder,
                    adapter,
                )
                isInitialized = true
            }

            if (isShowing) {
                hideHistoryScanList(scanListRecyclerView)
            } else {
                showHistoryScanList(scanListRecyclerView)
            }

            isShowing = !isShowing
        }

        private fun showHistoryScanList(view: View) {
            view.visibility = View.VISIBLE
        }

        private fun hideHistoryScanList(view: View) {
            view.visibility = View.GONE
        }

        private fun getScanListItems(card: Card, context: Context): ArrayList<Card> {
            val gson = Gson()
            val cards = ArrayList<Card>()
            val job = CoroutineScope(Dispatchers.IO).launch {
                val cardEntities = CardDatabase.getInstance(context).dao.getStoredCardById(card.getCardEntity().id)
                val cardEntityIterator = cardEntities.listIterator()
                while (cardEntityIterator.hasNext()) {
                    val cardEntity = cardEntityIterator.next()
                    if (cardEntity.type == CardType.Opus.name) {
                        cards.add(Card(
                            cardEntity.id.toULong(),
                            CardType.Opus,
                            Calendar.getInstance().apply {
                                timeInMillis = cardEntity.scanDate.toLong()
                            },
                            Calendar.getInstance().apply {
                                timeInMillis = cardEntity.expiryDate.toLong()
                            },
                            cardEntity.birthDate?.let { millis ->
                                Calendar.getInstance().apply { timeInMillis = millis.toLong() }
                            },
                            cardEntity.typeVariant?.toUInt(),
                            gson.fromJson(cardEntity.fares, ArrayList<Fare>()::class.java),
                            gson.fromJson(cardEntity.trips, ArrayList<Trip>()::class.java)
                        ))
                    } else if (cardEntity.type == CardType.Occasional.name
                        || cardEntity.type == CardType.OccasionalRTC.name
                        || cardEntity.type == CardType.OccasionalSTLevis.name) {
                        cards.add(Card(
                            cardEntity.id.toULong(),
                            when (cardEntity.type) {
                                CardType.OccasionalRTC.name -> CardType.OccasionalRTC
                                CardType.OccasionalSTLevis.name -> CardType.OccasionalSTLevis
                                else -> CardType.Occasional
                            },
                            Calendar.getInstance().also { calendar ->
                                calendar.timeInMillis = cardEntity.scanDate.toLong()
                            },
                            Calendar.getInstance().also { calendar ->
                                calendar.timeInMillis = cardEntity.expiryDate.toLong()
                            },
                            null,
                            null,
                            gson.fromJson(cardEntity.fares, ArrayList<Fare>()::class.java),
                            gson.fromJson(cardEntity.trips, ArrayList<Trip>()::class.java)
                        ))
                    }
                }
            }

            runBlocking {
                job.join()
            }

            return cards
        }
    }

    class HistoryItemDeleteListener(
        private val card: Card,
        private val holder: MyViewHolder,
        private val adapter: HistoryCardAdapter,
    ): View.OnClickListener {
        override fun onClick(view: View) {
            val builder = AlertDialog.Builder(view.context)
            builder.setMessage(R.string.delete_confirmation_message)
                .setNegativeButton(R.string.no) { _, _ -> }
                .setPositiveButton(R.string.yes) { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        NotificationScheduler().removeScheduleNotification(card, view.context)
                        CardDatabase.getInstance(view.context).dao.deleteStoredCard(card.getCardEntity().id)
                    }
                    adapter.removeItem(holder.adapterPosition)
                }
            val dialog = builder.create()
            dialog.show()
        }
    }
}