package com.example.opusreader

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.nfc.tech.MifareUltralight
import android.util.Log
import java.io.IOException

private const val TAG = "Reader"

class Reader: NfcAdapter.ReaderCallback {
    override fun onTagDiscovered(tag: Tag) {
        Log.i(TAG, "Card discovered !")

        if (tag.toString().contains("MifareUltralight")
            || tag.toString().contains("NfcA")) {
            try {
                Log.i(TAG, "Occasional card detected")
                val card = MifareUltralight.get(tag)
                val data = this.getDataFromMifareUltralight(card)
                Parser().parseOccasionalCard(data)
            } catch (e: IOException) {
                Log.e(TAG, "Error occurred during the communication: $e")
            }
        } else if (tag.toString().contains("IsoDep")
            || tag.toString().contains("NfcB")) {
            val card = IsoDep.get(tag)
            try {
                Log.i(TAG, "Opus card detected")
                Parser().parseOpusCard(card)
            } catch (e: IOException) {
                Log.e(TAG, "Error occurred during the communication: $e")
            }
        }
    }

    private fun getDataFromMifareUltralight(card: MifareUltralight): Array<ByteArray> {
        card.connect()
        return arrayOf(
            card.readPages(0),
            card.readPages(4),
            card.readPages(8),
            card.readPages(12),
        )
    }
}