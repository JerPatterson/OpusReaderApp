package com.example.opusreader

import android.nfc.tech.IsoDep
import android.util.Log
import java.math.BigInteger
import java.util.Calendar

private const val TAG = "Parser"

class Parser {
    fun parseOccasionalCard(data: Array<ByteArray>) {
        val id = this.getOccasionalCardId(data)

        val fareType = this.getOccasionalCardFareType(data)
        val fareOperatorId = this.getOccasionalCardFareOperatorId(data)
        var fareUnlimited = false
        var fareNbOfTickets = 0u
        if (this.isTicketsOccasionalCard(data)) {
            fareNbOfTickets = this.getOccasionalCardFareNbOfTickets(data)
        } else if (this.isUnlimitedPassOccasionalCard(data)) {
            fareUnlimited = true
            fareNbOfTickets = 1u
        }

        Log.i(TAG, "id: $id")
        Log.i(TAG, "fareType: $fareType")
        Log.i(TAG, "fareOperatorId: $fareOperatorId")
        Log.i(TAG, "fareUnlimited: $fareUnlimited")
        Log.i(TAG, "fareNbOfTickets: $fareNbOfTickets")

        this.getOccasionalCardTrips(data)
    }

    fun parseOpusCard(card: IsoDep) {
        card.connect()

        val id = this.getOpusCardId(card)
        val expiryDate = this.getOpusCardExpiryDate(card)

        Log.i(TAG, "id: $id")
        Log.i(TAG, "expiryDate: $expiryDate")

        this.getOpusCardTrips(card)
        this.getOpusCardUnlimitedFares(card)

        // TODO Tickets
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

    private fun getOccasionalCardId(data: Array<ByteArray>): ULong {
        return (data[0][7].toULong().and(0xFFu).shl(40)
                or data[0][6].toULong().and(0xFFu).shl(32)
                or data[0][5].toULong().and(0xFFu).shl(24)
                or data[0][4].toULong().and(0xFFu).shl(16)
                or data[0][2].toULong().and(0xFFu).shl(8)
                or data[0][1].toULong().and(0xFFu))
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
            val tripFirstUse = this.uIntToDate(tripFirstUseDays, tripFirstUseMinutes)

            val tripMinutes = ((data[i][4].toUInt() and 0xFFu).shl(3)
                    or data[i][5].toUInt().and(0xE0u).shr(5))
            val tripUse = this.uIntToDate(tripFirstUseDays, tripMinutes)

            val tripLineId = (data[i][5].toUInt().and(0x1Fu).shl(4)
                    or data[i][6].toUInt().and(0xF0u).shr(4))
            val tripOperatorId = (data[i][12].toUInt().and(0x01u).shl(7)
                    or data[i][13].toUInt().and(0xFEu).shr(1))

            Log.i(TAG, "tripUse: $tripUse")
            Log.i(TAG, "tripFirstUse: $tripFirstUse")
            Log.i(TAG, "tripLineId: $tripLineId")
            Log.i(TAG, "tripOperatorId: $tripOperatorId")
        }
    }


    private fun getOpusCardId(card: IsoDep): UInt {
        card.transceive(this.hexStringToByteArray("94A4000002000219"))
        val data = card.transceive(this.hexStringToByteArray("94B201041D"))

        return (data[16].toUInt().and(0xFFu).shl(24)
                or data[17].toUInt().and(0xFFu).shl(16)
                or data[18].toUInt().and(0xFFu).shl(8)
                or data[19].toUInt().and(0xFFu))
    }

    private fun getOpusCardExpiryDate(card: IsoDep): Calendar {
        card.transceive(this.hexStringToByteArray("94a408000420002001"))
        val data = card.transceive(this.hexStringToByteArray("94B2010400"))
        val daysUntilExpiry = (data[5].toUInt().and(0x07u).shl(11)
                or data[6].toUInt().and(0xFFu).shl(3)
                or data[7].toUInt().and(0xE0u).shr(5))

        return uIntToDate(daysUntilExpiry, 0u)
    }

    private fun getOpusCardTrips(card: IsoDep) {
        card.transceive(this.hexStringToByteArray("94a408000420002010"))
        for (i in 1..3) {
            val data = card.transceive(this.hexStringToByteArray("94b20${i}0400"))
            val tripDays = (data[0].toUInt().and(0xFFu).shl(6)
                    or data[1].toUInt().and(0xFCu).shr(2))
            val tripMinutes = (data[1].toUInt().and(0x03u).shl(9)
                    or data[2].toUInt().and(0xFFu).shl(1)
                    or data[3].toUInt().and(0x80u).shr(7))

            val tripUse = this.uIntToDate(tripDays, tripMinutes)

            var tripLineId: UInt
            var tripOperatorId: UInt
            if ((data[5].toUInt().and(0x0Fu).shl(7)
                    or data[6].toUInt().and(0xFEu).shr(1)
                        ).compareTo(0xFF8u) == 0) {
                tripLineId = (data[12].toUInt().and(0x0Fu).shl(5)
                        or data[13].toUInt().and(0xF8u).shr(3))
                tripOperatorId = (data[8].toUInt().and(0x01u).shl(7)
                        or data[9].toUInt().and(0xFEu).shr(1))
            } else {
                tripLineId = (data[11].toUInt().and(0x0Fu).shl(5)
                        or data[12].toUInt().and(0xF8u).shr(3))
                tripOperatorId = (data[7].toUInt().and(0x01u).shl(7)
                        or data[8].toUInt().and(0xFEu).shr(1))
            }

            Log.i(TAG, "tripUse: $tripUse")
            Log.i(TAG, "tripLineId: $tripLineId")
            Log.i(TAG, "tripOperatorId: $tripOperatorId")
        }
    }

    private fun getOpusCardUnlimitedFares(card: IsoDep) {
        card.transceive(this.hexStringToByteArray("94a408000420002020"))
        for (i in 1..4) {
            val data = card.transceive(this.hexStringToByteArray("94b20${i}0400"))
            val fareDays = (data[5].toUInt().and(0x01u).shl(13)
                    or data[6].toUInt().and(0xFFu).shl(5)
                    or data[7].toUInt().and(0xF8u).shr(3))

            val fareValidity = this.uIntToDate(fareDays, 0u)

            Log.i(TAG, "fareValidity: $fareValidity")
        }
    }


    private fun uIntToDate(days: UInt, minutes: UInt): Calendar {
        val date = Calendar.getInstance()
        date.set(1997, Calendar.JANUARY, 1, 0, 0, 0)

        date.add(Calendar.DATE, days.toInt())
        date.add(Calendar.MINUTE, minutes.toInt())

        return date
    }

    private fun hexStringToByteArray(hexString: String): ByteArray {
        if (hexString.length % 2 != 0) return ByteArray(0)

        return hexString.chunked(2)
                .map { it.toInt(16).toByte() }
                .toByteArray()
    }
}