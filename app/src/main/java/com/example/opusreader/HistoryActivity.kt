package com.example.opusreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

private const val ARG_CARDS = "cards"


class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val historyRecyclerView = this.findViewById<RecyclerView>(R.id.historyRecyclerView)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)

        val cardsType: Type = object : TypeToken<ArrayList<Card>>() {}.type
        val cards: ArrayList<Card> = Gson().fromJson(intent.getStringExtra(ARG_CARDS), cardsType)
        historyRecyclerView.adapter = HistoryAdapter(cards)
    }
}