package com.example.opusreader

import android.util.Log
import java.util.Calendar

private const val TAG = "Fare"

class Fare {
    private var typeId: UInt
    private var operatorId: UInt
    private var buyingDate: Calendar
    private var ticketCount: UInt?
    private var validityFromDate: Calendar?
    private var validityUntilDate: Calendar?

    constructor(
        typeId: UInt,
        operatorId: UInt,
        buyingDate: Calendar,
        ticketCount: UInt?,
    ) {
        this.typeId = typeId
        this.operatorId = operatorId
        this.buyingDate = buyingDate
        this.ticketCount = ticketCount
        this.validityFromDate = null
        this.validityUntilDate = null
        this.log()
    }

    constructor(
        typeId: UInt,
        operatorId: UInt,
        buyingDate: Calendar,
        validityFromDate: Calendar,
        validityUntilDate: Calendar
    ) {
        this.typeId = typeId
        this.operatorId = operatorId
        this.buyingDate = buyingDate
        this.ticketCount = null
        this.validityFromDate = validityFromDate
        this.validityUntilDate = validityUntilDate
        this.log()
    }

    private fun log() {
        Log.i(TAG, "-----")
        Log.i(TAG, "Fare")
        Log.i(TAG, "typeId: $typeId")
        Log.i(TAG, "operatorId: $operatorId")
        Log.i(TAG, "buyingDate: $buyingDate")
        Log.i(TAG, "ticketCount: $ticketCount")
        Log.i(TAG, "validityFromDate: $validityFromDate")
        Log.i(TAG, "validityUntilDate: $validityUntilDate")
    }
}