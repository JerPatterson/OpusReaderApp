package com.example.opusreader

import android.util.Log
import java.io.Serializable
import java.util.Calendar

private const val TAG = "Trip"

class Trip(
    var lineId: UInt,
    private var operatorId: UInt,
    private var useDate: Calendar,
    private var firstUseDate: Calendar
): Serializable {

    fun log(prefix: String = "") {
        Log.i(TAG, "${prefix}TRIP")
        Log.i(TAG, "${prefix}lineId: $lineId")
        Log.i(TAG, "${prefix}operatorId: $operatorId")
        Log.i(TAG, "${prefix}useDate: $useDate")
        Log.i(TAG, "${prefix}firstUseDate: $firstUseDate")
    }
}