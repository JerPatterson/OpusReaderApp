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
            Log.i(TAG, "Occasional card detected")
            val occasionalCard = MifareUltralight.get(tag)

            try {
                occasionalCard.connect()
                val data: Array<ByteArray> = arrayOf(
                    occasionalCard.readPages(0),
                    occasionalCard.readPages(4),
                    occasionalCard.readPages(8),
                    occasionalCard.readPages(12),
                )

            } catch (e: IOException) {
                Log.e(TAG, "Error occurred during the communication: $e")
            }
        } else if (tag.toString().contains("IsoDep")
            || tag.toString().contains("NfcB")) {
            Log.i(TAG, "Opus card detected")
            val opusCard = IsoDep.get(tag)

            try {
                opusCard.connect()

                opusCard.transceive("94A4000002000219".toByteArray())
                val id = opusCard.transceive("94B201041D".toByteArray())
                opusCard.transceive("94a408000420002001".toByteArray())
                val expiryDate = opusCard.transceive("94b2010400".toByteArray())

                val validations = arrayListOf<ByteArray>()
                opusCard.transceive("94a408000420002010".toByteArray())
                for (i in 1..8) {
                    validations.add(opusCard.transceive("94b20${i}0400".toByteArray()))
                }

                val passes = arrayListOf<ByteArray>()
                opusCard.transceive("94a408000420002020".toByteArray())
                for (i in 1..4) {
                    passes.add(opusCard.transceive("94b20${i}0400".toByteArray()))
                }

                val tickets = arrayListOf<ByteArray>()
                opusCard.transceive("94a40800042000202A".toByteArray())
                tickets.add(opusCard.transceive("94b2010400".toByteArray()))
                opusCard.transceive("94a40800042000202B".toByteArray())
                tickets.add(opusCard.transceive("94b2010400".toByteArray()))
                opusCard.transceive("94a40800042000202C".toByteArray())
                tickets.add(opusCard.transceive("94b2010400".toByteArray()))
                opusCard.transceive("94a40800042000202D".toByteArray())
                tickets.add(opusCard.transceive("94b2010400".toByteArray()))

            } catch (e: IOException) {
                Log.e(TAG, "Error occurred during the communication: $e")
            }
        }
    }
}