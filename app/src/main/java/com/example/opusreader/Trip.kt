package com.example.opusreader

import android.util.Log
import java.util.Calendar

private const val TAG = "Trip"

class Trip {
    private var lineId: UInt
    private var operatorId: UInt
    private var useDate: Calendar
    private var firstUseDate: Calendar

    constructor(
        lineId: UInt,
        operatorId: UInt,
        useDate: Calendar,
        firstUseDate: Calendar
    ) {
        this.lineId = lineId
        this.operatorId = operatorId
        this.useDate = useDate
        this.firstUseDate = firstUseDate
        this.log()
    }

    private fun log() {
        Log.i(TAG, "-----")
        Log.i(TAG, "TRIP")
        Log.i(TAG, "lineId: $lineId")
        Log.i(TAG, "operatorId: $operatorId")
        Log.i(TAG, "useDate: $useDate")
        Log.i(TAG, "firstUseDate: $firstUseDate")
    }
}