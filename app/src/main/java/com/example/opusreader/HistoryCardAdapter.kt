package com.example.opusreader

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
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

        holder.itemView.setOnClickListener(HistoryItemListener(historyList[position], holder))
        holder.deleteItemIcon.setOnClickListener(HistoryItemDeleteListener(historyList[position], position, this))
    }

    private fun removeItem(position: Int) {
        historyList.removeAt(position)
        notifyItemRemoved(position)
    }


    private fun getImageResource(type: CardType): Int {
        return when (type) {
            CardType.Opus -> R.drawable.opus
            CardType.Occasional -> R.drawable.occasionnelle
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
            }
        }

        fun calendarToStringWithTime(cal: Calendar): String {
            return SimpleDateFormat(
                getString(itemView.context, R.string.calendar_with_time_pattern),
                Locale(getString(itemView.context, R.string.calendar_language),
                    getString(itemView.context, R.string.calendar_country))
            ).format(cal.time)
        }
    }

    class HistoryItemListener(
        private val card: Card,
        private val holder: MyViewHolder,
    ) : View.OnClickListener {
        private var isShowing: Boolean = false
        private var isInitialized: Boolean = false

        override fun onClick(view: View) {
            val scanListRecyclerView: RecyclerView = view.findViewById(R.id.historyScanList)

            if (!isInitialized) {
                scanListRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
                scanListRecyclerView.adapter = HistoryScanAdapter(getScanListItems(card, holder.itemView.context))
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
                            Calendar.getInstance().also { calendar ->
                                calendar.timeInMillis = cardEntity.scanDate.toLong()
                            },
                            Calendar.getInstance().also { calendar ->
                                calendar.timeInMillis = cardEntity.expiryDate.toLong()
                            },
                            gson.fromJson(cardEntity.fares, ArrayList<Fare>()::class.java),
                            gson.fromJson(cardEntity.trips, ArrayList<Trip>()::class.java)
                        ))
                    } else if (cardEntity.type == CardType.Occasional.name) {
                        cards.add(Card(
                            cardEntity.id.toULong(),
                            CardType.Occasional,
                            Calendar.getInstance().also { calendar ->
                                calendar.timeInMillis = cardEntity.scanDate.toLong()
                            },
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
        private val position: Int,
        private val adapter: HistoryCardAdapter
    ): View.OnClickListener {
        override fun onClick(view: View) {
            CoroutineScope(Dispatchers.IO).launch {
                CardDatabase.getInstance(view.context).dao.deleteStoredCard(card.getCardEntity().id)
            }
            adapter.removeItem(position)
        }
    }
}