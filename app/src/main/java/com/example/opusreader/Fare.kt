package com.example.opusreader

import android.util.Log
import java.util.Calendar

private const val TAG = "Fare"

class Fare(
    private var typeId: UInt,
    private var operatorId: UInt,
    private var buyingDate: Calendar,
    private var ticketCount: UInt?,
    private var validityFromDate: Calendar? = null,
    private var validityUntilDate: Calendar? = null
) {

    fun log(prefix: String = "") {
        Log.i(TAG, "${prefix}FARE")
        Log.i(TAG, "${prefix}typeId: $typeId")
        Log.i(TAG, "${prefix}operatorId: $operatorId")
        Log.i(TAG, "${prefix}buyingDate: $buyingDate")
        Log.i(TAG, "${prefix}ticketCount: $ticketCount")
        Log.i(TAG, "${prefix}validityFromDate: $validityFromDate")
        Log.i(TAG, "${prefix}validityUntilDate: $validityUntilDate")
    }
}