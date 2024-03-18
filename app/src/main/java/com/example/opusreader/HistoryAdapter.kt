package com.example.opusreader

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
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
}