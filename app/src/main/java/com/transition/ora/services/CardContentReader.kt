package com.transition.ora.services

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.nfc.tech.MifareUltralight
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.transition.ora.CardActivity
import com.transition.ora.MainActivity
import com.transition.ora.R
import com.transition.ora.database.daos.CardDao
import com.transition.ora.types.Card
import java.io.IOException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "Reader"

class CardContentReader(
    private var dao: CardDao,
    private var activity: MainActivity,
) : NfcAdapter.ReaderCallback {

    override fun onTagDiscovered(tag: Tag) {
        val techList = tag.techList.toList()
        val cardParsed: Card

        try {
            when {
                techList.contains(IsoDep::class.java.name) -> {
                    val card = IsoDep.get(tag)
                    cardParsed = CardContentParser().parseOpusCard(card)
                }
                techList.contains(MifareUltralight::class.java.name) -> {
                    val card = MifareUltralight.get(tag)
                    cardParsed = CardContentParser().parseOccasionalCard(card)
                }
                else -> {
                    Log.w(TAG, "Unsupported card technology: $techList")
                    showErrorBubble(R.string.error_unsupported_card)
                    return
                }
            }
        } catch (_: IOException) {
            showErrorBubble(R.string.error_read_failed)
            return
        } catch (_: Exception) {
            showErrorBubble(R.string.error_parse_failed)
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            dao.insertStoredCard(cardParsed.getCardEntity())
        }

        val gson = Gson()
        val intent = Intent(activity, CardActivity::class.java).apply {
            putExtra("card", gson.toJson(cardParsed))
        }

        activity.runOnUiThread {
            activity.disableReaderMode()
            activity.startActivity(intent)
        }
    }

    private fun showErrorBubble(message: Int) {
        activity.runOnUiThread {
            if (!activity.isFinishing && !activity.isDestroyed) {
                val rootView = activity.findViewById<android.view.View>(android.R.id.content)
                Snackbar.make(rootView, message, 5000)
                    .setBackgroundTint(ContextCompat.getColor(activity, R.color.secondary))
                    .setTextColor(ContextCompat.getColor(activity, R.color.accent))
                    .setActionTextColor(ContextCompat.getColor(activity, R.color.accent))
                    .setAction(R.string.dismiss_snackbar_message) { }
                    .show()
            }
        }
    }
}
