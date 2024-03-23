package com.example.opusreader

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class HistoryScanAdapter(
    private val historyList: ArrayList<Card>,
    private val historyCardPosition: Int,
    private val historyCardAdapter: HistoryCardAdapter,
): RecyclerView.Adapter<HistoryScanAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.history_scan_row, parent, false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.scanTimeValueTv.text = holder.calendarToStringWithTime(historyList[position].scanDate)

        holder.itemView.setOnClickListener(HistoryScanItemListener(historyList[position], holder.itemView.context))
        holder.deleteItemIcon.setOnClickListener(HistoryItemDeleteListener(historyList[position], position, this))
    }

    private fun removeItem(position: Int) {
        historyList.removeAt(position)
        notifyItemRemoved(position)
        if (historyList.size == 0) historyCardAdapter.notifyItemRemoved(historyCardPosition)
    }


    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val scanTimeValueTv: TextView = itemView.findViewById(R.id.historyScanTimeValueTv)
        val deleteItemIcon: ImageView = itemView.findViewById(R.id.deleteItemHistoryIcon)

        fun calendarToStringWithTime(cal: Calendar): String {
            return SimpleDateFormat(
                getString(itemView.context, R.string.calendar_with_time_pattern),
                Locale(getString(itemView.context, R.string.calendar_language),
                    getString(itemView.context, R.string.calendar_country))
            ).format(cal.time)
        }
    }

    class HistoryScanItemListener(
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
        private val adapter: HistoryScanAdapter
    ): View.OnClickListener {
        override fun onClick(view: View) {
            val builder = AlertDialog.Builder(view.context)
            builder.setMessage(R.string.delete_confirmation_message)
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .setPositiveButton(R.string.confirm) { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        CardDatabase.getInstance(view.context).dao.deleteStoredCardScan(card.getCardEntity())
                    }
                    adapter.removeItem(position)
                }
            val dialog = builder.create()
            dialog.show()
        }
    }
}