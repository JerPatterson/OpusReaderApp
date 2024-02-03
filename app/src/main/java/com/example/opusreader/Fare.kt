package com.example.opusreader

import android.util.Log
import java.io.Serializable
import java.util.Calendar

private const val TAG = "Fare"

class Fare(
    var typeId: UInt,
    var operatorId: UInt,
    var buyingDate: Calendar,
    var ticketCount: UInt?,
    var validityFromDate: Calendar? = null,
    var validityUntilDate: Calendar? = null,
    var buyingDateHasMinutes: Boolean = false
): Serializable {

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