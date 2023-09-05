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
        if (tag.toString().contains("MifareUltralight")
            || tag.toString().contains("NfcA")) {
            try {
                val card = MifareUltralight.get(tag)
                Parser().parseOccasionalCard(card)
            } catch (e: IOException) {
                Log.e(TAG, "Error occurred during the communication: $e")
            }
        } else if (tag.toString().contains("IsoDep")
            || tag.toString().contains("NfcB")) {
            try {
                val card = IsoDep.get(tag)
                Parser().parseOpusCard(card)
            } catch (e: IOException) {
                Log.e(TAG, "Error occurred during the communication: $e")
            }
        }
    }
}