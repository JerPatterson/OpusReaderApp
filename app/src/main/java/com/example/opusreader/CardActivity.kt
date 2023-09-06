package com.example.opusreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class CardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        val lines = intent.getSerializableExtra("lines") as Array<*>
        findViewById<TextView>(R.id.tripLineIdTv1).text = lines[0].toString()
        findViewById<TextView>(R.id.tripLineIdTv2).text = lines[1].toString()
        findViewById<TextView>(R.id.tripLineIdTv3).text = lines[2].toString()
    }
}