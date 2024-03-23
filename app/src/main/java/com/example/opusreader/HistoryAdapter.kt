package com.example.opusreader

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class HistoryAdapter(private val historyList: ArrayList<Card>): RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.history_row, parent, false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.cardIdValueTv.text = historyList[position].id.toString()
        holder.cardTypeValueTv.text = holder.getCardTypeValue(historyList[position].type)
        holder.lastScanTimeValueTv.text = holder.calendarToStringWithTime(historyList[position].scanDate)
        holder.cardImageView.setImageResource(getImageResource(historyList[position].type))

        holder.itemView.setOnClickListener(HistoryItemListener(historyList[position], holder.itemView.context))
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
        private val context: Context,
    ) : View.OnClickListener {
        override fun onClick(view: View) {
            val gson = Gson()
            val intent = Intent(context , CardActivity::class.java)
            intent.putExtra("card", gson.toJson(card))
            context.startActivity(intent)
        }
    }

    class HistoryItemDeleteListener(
        private val card: Card,
        private val position: Int,
        private val adapter: HistoryAdapter
    ): View.OnClickListener {
        override fun onClick(view: View) {
            CoroutineScope(Dispatchers.IO).launch {
                CardDatabase.getInstance(view.context).dao.deleteStoredCardById(card.getCardEntity())
            }
            adapter.removeItem(position)
        }
    }
}