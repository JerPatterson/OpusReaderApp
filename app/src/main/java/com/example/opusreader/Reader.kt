package com.example.opusreader

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.nfc.tech.MifareUltralight
import android.util.Log
import java.io.IOException


private const val TAG = "Reader"

class Reader(private var activity: MainActivity) : NfcAdapter.ReaderCallback {

    override fun onTagDiscovered(tag: Tag) {
        var cardParsed: Card? = null
        if (tag.toString().contains("MifareUltralight")
            || tag.toString().contains("NfcA")) {
            try {
                val card = MifareUltralight.get(tag)
                cardParsed = Parser().parseOccasionalCard(card)
            } catch (e: IOException) {
                Log.e(TAG, "Error occurred during the communication: $e")
                return
            }
        } else if (tag.toString().contains("IsoDep")
            || tag.toString().contains("NfcB")) {
            try {
                val card = IsoDep.get(tag)
                cardParsed = Parser().parseOpusCard(card)
            } catch (e: IOException) {
                Log.e(TAG, "Error occurred during the communication: $e")
                return
            }
        }

        if (cardParsed != null) {
            val lines = Array(3) { _ -> 0 }
            val intent = Intent(activity, CardActivity::class.java)
            for ((i, trip) in cardParsed.trips.withIndex()) {
                lines[i] = trip.lineId.toInt()
            }

            intent.putExtra("lines", lines)
            activity.startActivity(intent)
        }
    }
}