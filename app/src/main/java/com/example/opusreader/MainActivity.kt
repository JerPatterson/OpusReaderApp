package com.example.opusreader

import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.room.Room

private const val FLAGS = NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B

class MainActivity : AppCompatActivity() {
    private lateinit var db: CardDatabase
    private lateinit var reader: Reader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(
            applicationContext,
            CardDatabase::class.java,
            "cards"
        ).build()
        reader = Reader(db.dao, this)

        this.findViewById<Button>(R.id.scanHistoryButton).setOnClickListener(HistoryButtonListener())
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

    class HistoryButtonListener : View.OnClickListener {
        override fun onClick(view: View) {
            Log.i("Test", "Clicked")
        }
    }
}