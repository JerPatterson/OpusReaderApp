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
    }

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