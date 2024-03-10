package com.example.opusreader

import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

private const val FLAGS = NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B

class MainActivity : AppCompatActivity() {
    private val reader = Reader(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
}