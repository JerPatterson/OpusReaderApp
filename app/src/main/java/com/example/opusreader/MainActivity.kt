package com.example.opusreader

import android.content.Intent
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

private const val FLAGS = NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B

class MainActivity : AppCompatActivity() {
    private lateinit var reader: Reader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        reader = Reader(CardDatabase.getInstance(this).dao, this)

        this.findViewById<Button>(R.id.scanHistoryButton)
            .setOnClickListener(HistoryButtonListener(this))
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
        private val context: AppCompatActivity,
    ) : View.OnClickListener {
        override fun onClick(view: View) {
            val intent = Intent(context , HistoryActivity::class.java)
            context.startActivity(intent)
        }
    }
}