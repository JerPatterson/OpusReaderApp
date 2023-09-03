package com.example.opusreader

import android.nfc.tech.IsoDep
import android.util.Log
import java.util.Calendar

private const val TAG = "Parser"

class Parser {
    fun parseOccasionalCard(data: Array<ByteArray>) {
        val id = (data[0][7].toULong().and(0xFFu).shl(40)
                or data[0][6].toULong().and(0xFFu).shl(32)
                or data[0][5].toULong().and(0xFFu).shl(24)
                or data[0][4].toULong().and(0xFFu).shl(16)
                or data[0][2].toULong().and(0xFFu).shl(8)
                or data[0][1].toULong().and(0xFFu))

        val fareType = this.getOccasionalCardFareType(data)
        val fareOperatorId = this.getOccasionalCardFareOperatorId(data)
        var fareNbOfTickets = 0u
        if (this.isTicketsOccasionalCard(data)) {
            Log.i(TAG, "Tickets Available")
            fareNbOfTickets = this.getOccasionalCardFareNbOfTickets(data)
        } else if (this.isUnlimitedPassOccasionalCard(data)) {
            Log.i(TAG, "Unlimited Pass Available")
            fareNbOfTickets = 1u
        }

        Log.i(TAG, "id: $id")
        Log.i(TAG, "fareType: $fareType")
        Log.i(TAG, "fareOperatorId: $fareOperatorId")
        Log.i(TAG, "fareNbOfTickets: $fareNbOfTickets")

        this.getOccasionalCardTrips(data)
    }

    fun parseOpusCard(card: IsoDep) {
        card.connect()

        card.transceive("94A4000002000219".toByteArray())
        val id = card.transceive("94B201041D".toByteArray())
        card.transceive("94a408000420002001".toByteArray())
        val expiryDate = card.transceive("94b2010400".toByteArray())

        val validations = arrayListOf<ByteArray>()
        card.transceive("94a408000420002010".toByteArray())
        for (i in 1..8) {
            validations.add(card.transceive("94b20${i}0400".toByteArray()))
        }

        val passes = arrayListOf<ByteArray>()
        card.transceive("94a408000420002020".toByteArray())
        for (i in 1..4) {
            passes.add(card.transceive("94b20${i}0400".toByteArray()))
        }

        val tickets = arrayListOf<ByteArray>()
        card.transceive("94a40800042000202A".toByteArray())
        tickets.add(card.transceive("94b2010400".toByteArray()))
        card.transceive("94a40800042000202B".toByteArray())
        tickets.add(card.transceive("94b2010400".toByteArray()))
        card.transceive("94a40800042000202C".toByteArray())
        tickets.add(card.transceive("94b2010400".toByteArray()))
        card.transceive("94a40800042000202D".toByteArray())
        tickets.add(card.transceive("94b2010400".toByteArray()))
    }

    private fun getOccasionalCardFareType(data: Array<ByteArray>): UInt {
        return (data[1][4].toUInt().and(0xFFu).shl(9)
                or data[1][5].toUInt().and(0xFFu).shl(1)
                or data[1][6].toUInt().and(0xFFu).shr(7))
    }

    private fun getOccasionalCardFareOperatorId(data: Array<ByteArray>): UInt {
        return (data[1][6].toUInt().and(0x3Fu).shl(2)
                or data[1][7].toUInt().and(0xC0u).shr(6))
    }

    private fun isTicketsOccasionalCard(data: Array<ByteArray>): Boolean {
        return (data[1][0].toUInt().and(0xFFu).shl(24)
                or data[1][1].toUInt().and(0xFFu).shl(16)
                or data[1][2].toUInt().and(0xFFu).shl(8)
                or data[1][3].toUInt().and(0xFFu)).compareTo(0x00000000u) == 0
    }

    private fun isUnlimitedPassOccasionalCard(data: Array<ByteArray>): Boolean {
        return (data[1][0].toUInt().and(0xFFu).shl(24)
                or data[1][1].toUInt().and(0xFFu).shl(16)
                or data[1][2].toUInt().and(0xFFu).shl(8)
                or data[1][3].toUInt().and(0xFFu)).compareTo(0x80000000u) == 0
    }

    private fun getOccasionalCardFareNbOfTickets(data: Array<ByteArray>): UInt {
        return data[1][7].toUInt().and(0x1Fu)
    }

    private fun getOccasionalCardTrips(data: Array<ByteArray>) {
        for (i in 2..3) {
            val tripFirstUseDays = (data[i][0].toUInt().and(0x03u).shl(12)
                    or data[i][1].toUInt().and(0xFFu).shl(4)
                    or data[i][2].toUInt().and(0xF0u).shr(4))
            val tripFirstUseMinutes = (data[i][2].toUInt().and(0x0Fu).shl(7)
                    or data[i][3].toUInt().and(0xFEu))
            val tripFirstUse = uIntToDate(tripFirstUseDays, tripFirstUseMinutes)

            val tripMinutes = ((data[i][4].toUInt() and 0xFFu).shl(3)
                    or data[i][5].toUInt().and(0xE0u).shr(5))
            val tripLineId = (data[i][5].toUInt().and(0x1Fu).shl(4)
                    or data[i][6].toUInt().and(0xF0u).shr(4))
            val tripOperatorId = (data[i][12].toUInt().and(0x01u).shl(7)
                    or data[i][13].toUInt().and(0xFEu).shr(1))

            Log.i(TAG, "tripFirstUse: $tripFirstUse")
            Log.i(TAG, "tripMinutes: $tripMinutes")
            Log.i(TAG, "tripLineId: $tripLineId")
            Log.i(TAG, "tripOperatorId: $tripOperatorId")
        }
    }

    private fun uIntToDate(days: UInt, minutes: UInt): Calendar {
        val date = Calendar.getInstance()
        date.set(1997, Calendar.JANUARY, 1, 0, 0, 0)
        date.add(Calendar.DATE, days.toInt())
        date.add(Calendar.MINUTE, minutes.toInt())
        return date
    }
}