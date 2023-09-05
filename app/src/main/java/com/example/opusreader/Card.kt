package com.example.opusreader

import android.util.Log
import java.util.Calendar

private const val TAG = "Card"

class Card {
    private var type: CardType
    private var id: ULong
    private var fares: ArrayList<Fare>
    private var trips: ArrayList<Trip>
    private var expiryDate: Calendar?

    constructor(
        type: CardType,
        id: ULong,
        expiryDate: Calendar?,
        fares: ArrayList<Fare>,
        trips: ArrayList<Trip>,
    ) {
        this.type = type
        this.id = id
        this.expiryDate = expiryDate
        this.fares = fares
        this.trips = trips
        this.log()
    }

    private fun log() {
        Log.i(TAG, "-----")
        Log.i(TAG, "CARD")
        Log.i(TAG, "type: $type")
        Log.i(TAG, "id: $id")
        Log.i(TAG, "expiryDate: $expiryDate")
        this.logFares()
        Log.i(TAG, "\t-----")
        this.logTrips()
        Log.i(TAG, "-----")
    }

    private fun logFares() {
        for ((i, fare) in this.fares.withIndex()) {
            fare.log("\t")
            if (i !== this.fares.lastIndex) Log.i(TAG, "\t-----")
        }

    }

    private fun logTrips() {
        for ((i, trip) in this.trips.withIndex()) {
            trip.log("\t")
            if (i !== this.trips.lastIndex) Log.i(TAG, "\t-----")
        }
    }
}