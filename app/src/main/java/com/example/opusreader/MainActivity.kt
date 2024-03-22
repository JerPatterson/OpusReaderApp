package com.example.opusreader

import android.content.Intent
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import java.util.Calendar

private const val FLAGS = NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B

class MainActivity : AppCompatActivity() {
    private lateinit var db: CardDatabase
    private lateinit var reader: Reader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = CardDatabase.getInstance(this)
        reader = Reader(db.dao, this)

        this.findViewById<Button>(R.id.scanHistoryButton)
            .setOnClickListener(HistoryButtonListener(db, this))
    }

    override fun onPause() {
        super.onPause()
        this.disableReaderMode()
    }

    override fun onResume() {
        super.onResume()
        this.enableReaderMode()
    }

    private fun enableReaderMode() {
        val nfc = NfcAdapter.getDefaultAdapter(this) ?: return
        nfc.enableReaderMode(this, reader, FLAGS, null)
    }

    private fun disableReaderMode() {
        val nfc = NfcAdapter.getDefaultAdapter(this) ?: return
        nfc.disableReaderMode(this)
    }

    class HistoryButtonListener(
        private val db: CardDatabase,
        private val context: AppCompatActivity,
    ) : View.OnClickListener {
        override fun onClick(view: View) {
            val gson = Gson()
            val cards = ArrayList<Card>()
            CoroutineScope(Dispatchers.IO).launch {
                val cardEntities = db.dao.getStoredCards()
                val cardEntityIterator = cardEntities.listIterator()
                while (cardEntityIterator.hasNext()) {
                    val cardEntity = cardEntityIterator.next()
                    Log.i("Info", cardEntity.id)
                    Log.i("Info", cardEntity.type)
                    if (cardEntity.type == CardType.Opus.name) {
                        Log.i("Info", "Adding Opus")
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

                Log.i("Info", cards.size.toString())

                val intent = Intent(context , HistoryActivity::class.java)
                val cardsType: Type = object : TypeToken<ArrayList<Card>>() {}.type
                intent.putExtra("cards", gson.toJson(cards, cardsType))
                context.startActivity(intent)
            }
        }
    }
}