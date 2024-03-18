package com.example.opusreader

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


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
        holder.cardTypeValueTv.setText(getCardTypeValue(historyList[position].type))
        holder.cardImageView.setImageResource(getImageResource(historyList[position].type))
    }


    private fun getCardTypeValue(type: CardType): Int {
        return when (type) {
            CardType.Opus -> R.string.opus_card_name
            CardType.Occasional -> R.string.occasional_card_name
        }
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
        val lastScanTimeTv = itemView.findViewById<TextView>(R.id.historyLastScanTimeTv)
        val lastScanTimeValueTv = itemView.findViewById<TextView>(R.id.historyLastScanTimeValueTv)
    }
}