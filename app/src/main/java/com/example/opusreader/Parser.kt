package com.example.opusreader

import android.nfc.tech.IsoDep
import android.util.Log
import java.util.Calendar

private const val TAG = "Parser"

class Parser {
    fun parseOccasionalCard(data: Array<ByteArray>) {
        val id = this.getOccasionalCardId(data)

        Log.i(TAG, "id: $id")

        this.getOccasionalCardFare(data)
        this.getOccasionalCardTrips(data)
    }

    fun parseOpusCard(card: IsoDep) {
        card.connect()

        val id = this.getOpusCardId(card)
        val expiryDate = this.getOpusCardExpiryDate(card)

        Log.i(TAG, "id: $id")
        Log.i(TAG, "expiryDate: $expiryDate")

        this.getOpusCardFares(card)
        this.getOpusCardTrips(card)
    }


    private fun getOccasionalCardId(data: Array<ByteArray>): ULong {
        return (data[0][7].toULong().and(0xFFu).shl(40)
                or data[0][6].toULong().and(0xFFu).shl(32)
                or data[0][5].toULong().and(0xFFu).shl(24)
                or data[0][4].toULong().and(0xFFu).shl(16)
                or data[0][2].toULong().and(0xFFu).shl(8)
                or data[0][1].toULong().and(0xFFu))
    }


    private fun getOccasionalCardFare(data: Array<ByteArray>) {
        val fareTypeId = this.getOccasionalCardFareTypeId(data)
        val fareOperatorId = this.getOccasionalCardFareOperatorId(data)
        val fareBuyingDate = this.getOccasionalCardFareBuyingDate(data)

        Log.i(TAG, "fareTypeId: $fareTypeId")
        Log.i(TAG, "fareOperatorId: $fareOperatorId")
        Log.i(TAG, "fareBuyingDate: $fareBuyingDate")

        if (this.occasionalCardHasTicket(data)) {
            val fareTicketCount = this.getOccasionalCardFareNbOfTickets(data)

            Log.i(TAG, "fareTicketCount: $fareTicketCount")
        } else if (this.occasionalCardHasPass(data)) {
            // TODO Find Validity Start/End Dates
        }
    }

    private fun getOccasionalCardFareTypeId(data: Array<ByteArray>): UInt {
        return (data[1][4].toUInt().and(0xFFu).shl(19)
                or data[1][5].toUInt().and(0xFFu).shl(11)
                or data[1][6].toUInt().and(0xFFu).shl(3)
                or data[1][7].toUInt().and(0xE0u).shr(5))
    }

    private fun getOccasionalCardFareOperatorId(data: Array<ByteArray>): UInt {
        return (data[1][6].toUInt().and(0x3Fu).shl(2)
                or data[1][7].toUInt().and(0xC0u).shr(6))
    }

    private fun getOccasionalCardFareBuyingDate(data: Array<ByteArray>): Calendar {
        val fareBuyingDays = (data[1][8].toUInt().and(0xFFu).shl(6)
                or data[1][9].toUInt().and(0xFCu).shr(2))

        return this.uIntToDate(fareBuyingDays, 0u)
    }

    private fun occasionalCardHasTicket(data: Array<ByteArray>): Boolean {
        return (data[1][0].toUInt().and(0xFFu).shl(24)
                or data[1][1].toUInt().and(0xFFu).shl(16)
                or data[1][2].toUInt().and(0xFFu).shl(8)
                or data[1][3].toUInt().and(0xFFu)).compareTo(0x00000000u) == 0
    }

    private fun occasionalCardHasPass(data: Array<ByteArray>): Boolean {
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
            val tripLineId = this.getOccasionalCardTripLineId(data[i])
            val tripOperatorId = this.getOccasionalCardTripOperatorId(data[i])
            val tripUseDate = this.getOccasionalCardTripUseDate(data[i])
            val tripFirstUseDate = this.getOccasionalCardTripFirstUseDate(data[i])

            Log.i(TAG, "tripLineId: $tripLineId")
            Log.i(TAG, "tripOperatorId: $tripOperatorId")
            Log.i(TAG, "tripUseDate: $tripUseDate")
            Log.i(TAG, "tripFirstUseDate: $tripFirstUseDate")
        }
    }

    private fun getOccasionalCardTripFirstUseDate(data: ByteArray): Calendar {
        val tripFirstUseDays = (data[0].toUInt().and(0x03u).shl(12)
                or data[1].toUInt().and(0xFFu).shl(4)
                or data[2].toUInt().and(0xF0u).shr(4))
        val tripFirstUseMinutes = (data[2].toUInt().and(0x0Fu).shl(7)
                or data[3].toUInt().and(0xFEu))

        return this.uIntToDate(tripFirstUseDays, tripFirstUseMinutes)
    }

    private fun getOccasionalCardTripUseDate(data: ByteArray): Calendar {
        val tripFirstUseDays = (data[0].toUInt().and(0x03u).shl(12)
                or data[1].toUInt().and(0xFFu).shl(4)
                or data[2].toUInt().and(0xF0u).shr(4))
        val tripUseMinutes = ((data[4].toUInt() and 0xFFu).shl(3)
                or data[5].toUInt().and(0xE0u).shr(5))

        return this.uIntToDate(tripFirstUseDays, tripUseMinutes)
    }

    private fun getOccasionalCardTripLineId(data: ByteArray): UInt {
        return (data[5].toUInt().and(0x1Fu).shl(4)
                or data[6].toUInt().and(0xF0u).shr(4))
    }

    private fun getOccasionalCardTripOperatorId(data: ByteArray): UInt {
        return (data[12].toUInt().and(0x01u).shl(7)
                or data[13].toUInt().and(0xFEu).shr(1))
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


    private fun getOpusCardFares(card: IsoDep) {
        card.transceive(this.hexStringToByteArray("94a408000420002020"))
        for (i in 1..4) {
            val fareData = card.transceive(this.hexStringToByteArray("94b20${i}0400"))
            val fareTypeId = this.getOpusCardFareTypeId(fareData)
            val fareOperatorId = this.getOpusCardFareOperatorId(fareData)
            val fareBuyingDate = this.getOpusCardFareBuyingDate(fareData)

            Log.i(TAG, "fareTypeId: $fareTypeId")
            Log.i(TAG, "fareOperatorId: $fareOperatorId")
            Log.i(TAG, "fareBuyingDate: $fareBuyingDate")

            if ((fareData[5].toUInt().and(0xFFu).shl(8)
                    or fareData[6].toUInt().and(0xFFu)).compareTo(0u) == 0) {
                when (i) {
                    1 -> card.transceive(this.hexStringToByteArray("94a40800042000202A"))
                    2 -> card.transceive(this.hexStringToByteArray("94a40800042000202B"))
                    3 -> card.transceive(this.hexStringToByteArray("94a40800042000202C"))
                    4 -> card.transceive(this.hexStringToByteArray("94a40800042000202D"))
                }

                val fareTicketsData = card.transceive(this.hexStringToByteArray("94b2010400"))
                val fareTicketCount = fareTicketsData[2].toUInt().and(0xFFu)

                Log.i(TAG, "fareTicketCount: $fareTicketCount")
            } else {
                val fareValidityFromDate = this.getOpusCardFareValidityFromDate(fareData)
                val fareValidityUntilDate = this.getOpusCardFareValidityUntilDate(fareData)

                Log.i(TAG, "fareValidityFromDate: $fareValidityFromDate")
                Log.i(TAG, "fareValidityUntilDate: $fareValidityUntilDate")
            }
        }
    }

    private fun getOpusCardFareTypeId(data: ByteArray): UInt {
        return (data[0].toUInt().and(0xFFu).shl(24)
                or data[1].toUInt().and(0xFFu).shl(16)
                or data[2].toUInt().and(0xFFu).shl(8)
                or data[3].toUInt().and(0xFFu))
    }

    private fun getOpusCardFareOperatorId(data: ByteArray): UInt {
        return (data[1].toUInt().and(0x7Fu).shl(1)
                or data[2].toUInt().and(0x10u).shr(7))
    }

    private fun getOpusCardFareBuyingDate(data: ByteArray): Calendar {
        val fareDaysBuying = (data[9].toUInt().and(0x03u).shl(12)
                or data[10].toUInt().and(0xFFu).shl(4)
                or data[11].toUInt().and(0xF0u).shr(4))
        val fareMinutesBuying = (data[11].toUInt().and(0x0Fu).shl(7)
                or data[12].toUInt().and(0xFEu).shr(1))

        return this.uIntToDate(fareDaysBuying, fareMinutesBuying)
    }

    private fun getOpusCardFareValidityFromDate(data: ByteArray): Calendar {
        val fareValidityFromDays = (data[4].toUInt().and(0x7Fu).shl(7)
                or data[5].toUInt().and(0xFEu).shr(1))

        return this.uIntToDate(fareValidityFromDays, 0u)
    }

    private fun getOpusCardFareValidityUntilDate(data: ByteArray): Calendar {
        val fareValidityUntilDays = (data[5].toUInt().and(0x01u).shl(13)
                or data[6].toUInt().and(0xFFu).shl(5)
                or data[7].toUInt().and(0xF8u).shr(3))

        return this.uIntToDate(fareValidityUntilDays, 0u)
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