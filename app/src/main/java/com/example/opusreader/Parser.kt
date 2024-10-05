package com.example.opusreader

import android.nfc.tech.IsoDep
import android.nfc.tech.MifareUltralight
import java.util.Calendar

class Parser {
    fun parseOccasionalCard(card: MifareUltralight): Card {
        val data = this.getDataFromMifareUltralight(card)

        val id = this.getOccasionalCardId(data)
        val fares = this.getOccasionalCardFare(data)
        val trips = this.getOccasionalCardTrips(data)
        val expiryDate = Calendar.getInstance()
        expiryDate.set(2040, Calendar.DECEMBER, 31)

        return Card(id, CardType.Occasional, Calendar.getInstance(), expiryDate, fares, trips)
    }

    fun parseOpusCard(card: IsoDep): Card {
        card.connect()

        val id = this.getOpusCardId(card)
        val expiryDate = this.getOpusCardExpiryDate(card)
        val fares = this.getOpusCardFares(card)
        val trips = this.getOpusCardTrips(card)

        return Card(id.toULong(), CardType.Opus, Calendar.getInstance(), expiryDate, fares, trips)
    }


    private fun getDataFromMifareUltralight(card: MifareUltralight): Array<ByteArray> {
        card.connect()
        return arrayOf(
            card.readPages(0),
            card.readPages(4),
            card.readPages(8),
            card.readPages(12),
        )
    }

    private fun getOccasionalCardId(data: Array<ByteArray>): ULong {
        return (data[0][7].toULong().and(0xFFu).shl(40)
                or data[0][6].toULong().and(0xFFu).shl(32)
                or data[0][5].toULong().and(0xFFu).shl(24)
                or data[0][4].toULong().and(0xFFu).shl(16)
                or data[0][2].toULong().and(0xFFu).shl(8)
                or data[0][1].toULong().and(0xFFu))
    }


    private fun getOccasionalCardFare(data: Array<ByteArray>): ArrayList<Fare> {
        val fares = ArrayList<Fare>()

        val typeId = this.getOccasionalCardFareTypeId(data)
        val operatorId = this.getOccasionalCardFareOperatorId(data)
        val buyingDate = this.getOccasionalCardFareBuyingDate(data)

        if (this.occasionalCardHasTicket(data)) {
            val ticketCount = when (typeId) {
                FareProductId.OCC_2TICKETS_ALL_MODES_ABCD_SPECIAL_ILE_AUX_TOURTES.ID -> {
                    val validityEndDate = Calendar.getInstance()
                    validityEndDate.set(2024, 5, 1, 0, 0)
                    if (Calendar.getInstance().timeInMillis > validityEndDate.timeInMillis)
                        0u else this.getOccasionalCardFareNbOfTickets(data)
                }
                else -> this.getOccasionalCardFareNbOfTickets(data)
            }


            fares.add(Fare(typeId, operatorId, buyingDate, ticketCount))
        } else if (this.occasionalCardHasPass(data)) {
            val validityFromDate = getOccasionalCardFareValidityFromDate(data[2])
            if (occasionalCardHasValidPass(data)) {
                fares.add(Fare(typeId, operatorId, buyingDate, null, validityFromDate, null))
            } else if (validityFromDate != null) {
                val validityUntilDate = Calendar.getInstance()
                validityFromDate.set(
                    validityFromDate.get(Calendar.YEAR),
                    validityFromDate.get(Calendar.MONTH),
                    validityFromDate.get(Calendar.DATE),
                    validityFromDate.get(Calendar.HOUR_OF_DAY),
                    validityFromDate.get(Calendar.MINUTE) + 1,
                )

                fares.add(Fare(typeId, operatorId, buyingDate, null, validityFromDate, validityUntilDate))
            }
        }

        return fares
    }

    private fun getOccasionalCardFareTypeId(data: Array<ByteArray>): UInt {
        return (data[1][4].toUInt().and(0xFFu).shl(16)
                or data[1][5].toUInt().and(0xFFu).shl(8)
                or data[1][6].toUInt().and(0xFFu))
    }

    private fun getOccasionalCardFareOperatorId(data: Array<ByteArray>): UInt {
        return data[0][0].toUInt().and(0xFFu)
    }

    private fun getOccasionalCardFareBuyingDate(data: Array<ByteArray>): Calendar {
        val fareBuyingDays = (data[1][8].toUInt().and(0xFFu).shl(6)
                or data[1][9].toUInt().and(0xFCu).shr(2))

        return this.uIntToDate(fareBuyingDays, 0u)
    }

    private fun occasionalCardHasTicket(data: Array<ByteArray>): Boolean {
        val hasTicketVerificationBits = (data[0][12].toUInt().and(0xFFu).shl(8)
            or data[0][13].toUInt().and(0xFFu))

        return hasTicketVerificationBits.compareTo(0xFFFFu) == 0
    }

    private fun occasionalCardHasPass(data: Array<ByteArray>): Boolean {
        val hasPassVerificationBits = (data[0][12].toUInt().and(0xFFu).shl(8)
                or data[0][13].toUInt().and(0xFFu))

        return ((hasPassVerificationBits.compareTo(0x0000u) == 0)
            or (hasPassVerificationBits.compareTo(0x8000u) == 0))
    }

    private fun occasionalCardHasValidPass(data: Array<ByteArray>): Boolean {
        val hasPassVerificationBits = (data[0][12].toUInt().and(0xFFu).shl(8)
                or data[0][13].toUInt().and(0xFFu))

        return hasPassVerificationBits.compareTo(0x0000u) == 0
    }

    private fun getOccasionalCardFareNbOfTickets(data: Array<ByteArray>): UInt {
        val ticketBits = (data[0][14].toUInt().and(0x03u).shl(8)
                or data[0][15].toUInt().and(0xFFu))

        return when (ticketBits) {
            0u -> 10u
            512u -> 9u
            768u -> 8u
            896u -> 7u
            960u -> 6u
            992u -> 5u
            1008u -> 4u
            1016u -> 3u
            1020u -> 2u
            1022u -> 1u
            else -> 0u
        }
    }

    private fun getOccasionalCardFareValidityFromDate(data: ByteArray): Calendar? {
        val tripFirstUseDays = (data[0].toUInt().and(0x03u).shl(12)
                or data[1].toUInt().and(0xFFu).shl(4)
                or data[2].toUInt().and(0xF0u).shr(4))
        val tripFirstUseMinutes = (data[2].toUInt().and(0x0Fu).shl(7)
                or data[3].toUInt().and(0xFEu).shr(1))

        return if (tripFirstUseDays != 0u) this.uIntToDate(tripFirstUseDays, tripFirstUseMinutes) else null
    }


    private fun getOccasionalCardTrips(data: Array<ByteArray>): ArrayList<Trip> {
        val trips = ArrayList<Trip>()
        for (i in 2..3) {
            val lineId = this.getOccasionalCardTripLineId(data[i])
            val operatorId = this.getOccasionalCardTripOperatorId(data[i])
            val useDate = this.getOccasionalCardTripUseDate(data[i])
            val firstUseDate = this.getOccasionalCardTripFirstUseDate(data[i])

            trips.add(Trip(lineId, operatorId, useDate, firstUseDate))
        }

        return trips
    }

    private fun getOccasionalCardTripLineId(data: ByteArray): UInt {
        return (data[5].toUInt().and(0x1Fu).shl(4)
                or data[6].toUInt().and(0xF0u).shr(4))
    }

    private fun getOccasionalCardTripOperatorId(data: ByteArray): UInt {
        return (data[12].toUInt().and(0x01u).shl(7)
                or data[13].toUInt().and(0xFEu).shr(1))
    }

    private fun getOccasionalCardTripUseDate(data: ByteArray): Calendar {
        val tripFirstUseDays = (data[0].toUInt().and(0x03u).shl(12)
                or data[1].toUInt().and(0xFFu).shl(4)
                or data[2].toUInt().and(0xF0u).shr(4))
        val tripUseMinutes = ((data[4].toUInt() and 0xFFu).shl(3)
                or data[5].toUInt().and(0xE0u).shr(5))

        return this.uIntToDate(tripFirstUseDays, tripUseMinutes)
    }

    private fun getOccasionalCardTripFirstUseDate(data: ByteArray): Calendar {
        val tripFirstUseDays = (data[0].toUInt().and(0x03u).shl(12)
                or data[1].toUInt().and(0xFFu).shl(4)
                or data[2].toUInt().and(0xF0u).shr(4))
        val tripFirstUseMinutes = (data[2].toUInt().and(0x0Fu).shl(7)
                or data[3].toUInt().and(0xFEu).shr(1))

        return this.uIntToDate(tripFirstUseDays, tripFirstUseMinutes)
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
        card.transceive(this.hexStringToByteArray("94A408000420002001"))
        val data = card.transceive(this.hexStringToByteArray("94B2010400"))
        val daysUntilExpiry = (data[5].toUInt().and(0x07u).shl(11)
                or data[6].toUInt().and(0xFFu).shl(3)
                or data[7].toUInt().and(0xE0u).shr(5))

        return uIntToDate(daysUntilExpiry, 0u)
    }


    private fun getOpusCardTrips(card: IsoDep): ArrayList<Trip> {
        val trips = ArrayList<Trip>()
        card.transceive(this.hexStringToByteArray("94a408000420002010"))
        for (i in 1..3) {
            val data = card.transceive(this.hexStringToByteArray("94b20${i}0400"))

            var lineId: UInt
            var operatorId: UInt
            var firstUseDate: Calendar
            if (this.opusCardHasToUseByteOffset(data)) {
                lineId = this.getOpusCardTripLineId(data, 1)
                operatorId = this.getOpusCardTripOperatorId(data, 1)
                firstUseDate = this.getOpusCardTripFirstUseDate(data, 5)
            } else {
                lineId = this.getOpusCardTripLineId(data)
                operatorId = this.getOpusCardTripOperatorId(data)
                firstUseDate = this.getOpusCardTripFirstUseDate(data)
            }

            val useDate = this.getOpusCardTripUseDate(data)

            trips.add(Trip(lineId, operatorId, useDate, firstUseDate))
        }

        return trips
    }

    private fun opusCardHasToUseByteOffset(data: ByteArray): Boolean {
        return (data[5].toUInt().and(0x0Fu).shl(8)
                or data[6].toUInt().and(0xFFu)).compareTo(0xFF8u) == 0
    }

    private fun getOpusCardTripLineId(data: ByteArray, byteOffset: Int = 0): UInt {
        return (data[11 + byteOffset].toUInt().and(0x0Fu).shl(5)
                or data[12 + byteOffset].toUInt().and(0xF8u).shr(3))
    }

    private fun getOpusCardTripOperatorId(data: ByteArray, byteOffset: Int = 0): UInt {
        return (data[7 + byteOffset].toUInt().and(0x01u).shl(7)
                or data[8 + byteOffset].toUInt().and(0xFEu).shr(1))
    }

    private fun getOpusCardTripUseDate(data: ByteArray): Calendar {
        val tripUseDays = (data[0].toUInt().and(0xFFu).shl(6)
                or data[1].toUInt().and(0xFCu).shr(2))
        val tripUseMinutes = (data[1].toUInt().and(0x03u).shl(9)
                or data[2].toUInt().and(0xFFu).shl(1)
                or data[3].toUInt().and(0x80u).shr(7))

        return this.uIntToDate(tripUseDays, tripUseMinutes)
    }

    private fun getOpusCardTripFirstUseDate(data: ByteArray, byteOffset: Int = 0): Calendar {
        val tripFirstUseDays = (data[14 + byteOffset].toUInt().and(0x7Fu).shl(7)
                or data[15 + byteOffset].toUInt().and(0xFEu).shr(1))
        val tripFirstUseMinutes = (data[15].toUInt().and(0x01u).shl(10)
                or data[16 + byteOffset].toUInt().and(0xFFu).shl(2)
                or data[17 + byteOffset].toUInt().and(0xC0u).shr(6))

        return this.uIntToDate(tripFirstUseDays, tripFirstUseMinutes)
    }


    private fun getOpusCardFares(card: IsoDep): ArrayList<Fare> {
        val ticketsData = ArrayList<ByteArray>()
        card.transceive(this.hexStringToByteArray("94a40200042000202A"))
        ticketsData.add(card.transceive(this.hexStringToByteArray("94b2010400")))
        card.transceive(this.hexStringToByteArray("94a40200042000202B"))
        ticketsData.add(card.transceive(this.hexStringToByteArray("94b2010400")))
        card.transceive(this.hexStringToByteArray("94a40200042000202C"))
        ticketsData.add(card.transceive(this.hexStringToByteArray("94b2010400")))
        card.transceive(this.hexStringToByteArray("94a40200042000202D"))
        ticketsData.add(card.transceive(this.hexStringToByteArray("94b2010400")))

        val fares = ArrayList<Fare>()
        card.transceive(this.hexStringToByteArray("94a402000420002020"))
        for (i in 1..4) {
            val data = card.transceive(this.hexStringToByteArray("94b20${i}0400"))
            if (data.size.compareTo(31) != 0) continue

            val typeId = this.getOpusCardFareTypeId(data)
            val operatorId = this.getOpusCardFareOperatorId(data)
            val buyingDate = this.getOpusCardFareBuyingDate(data)

            if ((data[5].toUInt().and(0xFFu).shl(8)
                    or data[6].toUInt().and(0xFFu)).compareTo(0u) == 0) {
                val ticketCount = ticketsData[i - 1][2].toUInt()

                fares.add(Fare(
                    typeId,
                    operatorId,
                    buyingDate,
                    ticketCount,
                    null,
                    null,
                    true))
            } else {
                val validityFromDate = this.getOpusCardFareValidityFromDate(data)
                val validityUntilDate = this.getOpusCardFareValidityUntilDate(data)

                fares.add(
                    Fare(
                        typeId,
                        operatorId,
                        buyingDate,
                        null,
                        validityFromDate,
                        validityUntilDate,
                        true
                    )
                )
            }
        }

        return fares
    }

    private fun getOpusCardFareTypeId(data: ByteArray): UInt {
        return (data[2].toUInt().and(0x7Fu).shl(9)
            or data[3].toUInt().and(0xFFu).shl(1)
            or data[4].toUInt().and(0x80u).shr(7))
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