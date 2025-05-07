package com.transition.ora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.transition.ora.adapters.HistoryCardAdapter
import com.transition.ora.enums.CardType
import com.transition.ora.types.Card
import com.transition.ora.types.Fare
import com.transition.ora.types.Trip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar


class HistoryActivity : AppCompatActivity() {
    private lateinit var db: CardDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        addHistoryItems()
        enableDeleteAllButton()
        enableBackButton()
    }

    private fun addHistoryItems() {
        db = CardDatabase.getInstance(this)
        val cards: ArrayList<Card> = getStoredCards()

        val historyRecyclerView = this.findViewById<RecyclerView>(R.id.historyRecyclerView)
        historyRecyclerView.layoutManager = LinearLayoutManager(this)
        historyRecyclerView.adapter = HistoryCardAdapter(cards)
    }

    private fun getStoredCards(): ArrayList<Card> {
        val gson = Gson()
        val cards = ArrayList<Card>()
        val job = CoroutineScope(Dispatchers.IO).launch {
            val cardEntities = db.dao.getStoredCards()
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
                } else if (cardEntity.type == CardType.Occasional.name) {
                    cards.add(Card(
                        cardEntity.id.toULong(),
                        CardType.Occasional,
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

    private fun enableDeleteAllButton() {
        val deleteAllButton: Button = findViewById(R.id.deleteAllHistoryButton)
        deleteAllButton.setOnClickListener(HistoryDeleteAllListener(db,this))
    }

    class HistoryDeleteAllListener(
        private val db: CardDatabase,
        private val activity: HistoryActivity,
    ) : View.OnClickListener {
        override fun onClick(view: View) {
            val builder = AlertDialog.Builder(view.context)
            builder.setMessage(R.string.delete_all_confirmation_message)
                .setNegativeButton(R.string.no) { _, _ -> }
                .setPositiveButton(R.string.yes) { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        db.dao.deleteStoredCards()
                        db.daoProposition.deleteStoredPropositions()
                    }
                    activity.finish()
                }
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun enableBackButton() {
        val backButton: ImageView = findViewById(R.id.historyActivityBack)
        backButton.setOnClickListener(HistoryBackListener(this))
    }

    class HistoryBackListener(
        private val activity: HistoryActivity,
    ) : View.OnClickListener {
        override fun onClick(view: View) {
            activity.finish()
        }
    }
}