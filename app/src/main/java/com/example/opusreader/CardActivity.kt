package com.example.opusreader

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.gson.Gson

class CardActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        val gson = Gson()
        val card = gson.fromJson(intent.getStringExtra("card"), Card::class.java)
        if (card != null) this.addCardInfo(card)
    }

    private fun addCardInfo(card: Card) {
        val title = findViewById<TextView>(R.id.cardSectionTitleTv)
        val identifier = findViewById<TextView>(R.id.idValueTv)
        val expirationDate = findViewById<TextView>(R.id.expiryDateValueTv)

        title.text = if (card.type == CardType.Opus) "CARTE OPUS" else "CARTE OCCASIONNELLE"
        identifier.text = card.id.toString()
        expirationDate.text = "Unknown"
    }
}