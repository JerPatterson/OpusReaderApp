package com.example.opusreader

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.nfc.tech.MifareUltralight
import android.util.Log
import com.google.gson.Gson
import java.io.IOException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private const val TAG = "Reader"

class Reader(
    private var dao: CardDao,
    private var activity: MainActivity,
) : NfcAdapter.ReaderCallback {

    override fun onTagDiscovered(tag: Tag) {
        lateinit var cardParsed: Card
        if (tag.toString().contains("MifareUltralight")
            || tag.toString().contains("NfcA")) {
            try {
                val card = MifareUltralight.get(tag)
                cardParsed = Parser().parseOccasionalCard(card)
                CoroutineScope(Dispatchers.IO).launch {
                    dao.insertStoredCard(cardParsed.getCardEntity())
                }
            } catch (e: IOException) {
                Log.e(TAG, "Error occurred during the communication: $e")
                return
            }
        } else if (tag.toString().contains("IsoDep")
            || tag.toString().contains("NfcB")) {
            try {
                val card = IsoDep.get(tag)
                cardParsed = Parser().parseOpusCard(card)
                CoroutineScope(Dispatchers.IO).launch {
                    dao.insertStoredCard(cardParsed.getCardEntity())
                }
            } catch (e: IOException) {
                Log.e(TAG, "Error occurred during the communication: $e")
                return
            }
        } else {
            return
        }

        val gson = Gson()
        val intent = Intent(activity, CardActivity::class.java)
        intent.putExtra("card", gson.toJson(cardParsed))
        activity.startActivity(intent)
    }
}