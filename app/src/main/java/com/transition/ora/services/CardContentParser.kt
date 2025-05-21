package com.transition.ora.services

import android.nfc.tech.IsoDep
import android.nfc.tech.MifareUltralight
import com.transition.ora.enums.CardType
import com.transition.ora.enums.FareProductId
import com.transition.ora.types.Card
import com.transition.ora.types.Fare
import com.transition.ora.types.Trip
import java.util.Calendar


class CardContentParser {
    fun parseOccasionalCard(card: MifareUltralight): Card {
        val data = this.getDataFromMifareUltralight(card)

        val id = this.getOccasionalCardId(data)
        val fares = this.getOccasionalCardFare(data)
        val trips = this.getOccasionalCardTrips(data)
        val expiryDate = this.getOccasionalCardExpiryDate(data)

        return Card(id, CardType.Occasional, Calendar.getInstance(), expiryDate, null, null, fares, trips)
    }

    fun parseOpusCard(card: IsoDep): Card {
        card.connect()

        val id = this.getOpusCardId(card)
        card.transceive(this.hexStringToByteArray("94A408000420002001"))
        val data = card.transceive(this.hexStringToByteArray("94B2010400"))
        val expiryDate = this.getOpusCardExpiryDate(data)
        val birthDate = this.getOpusCardBirthDate(data)
        val typeVariant = this.getOpusCardTypeVariant(data)

        val fares = this.getOpusCardFares(card)
        val trips = this.getOpusCardTrips(card, fares)

        return Card(id.toULong(), CardType.Opus, Calendar.getInstance(), expiryDate, birthDate, typeVariant, fares, trips)
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

    private fun getOccasionalCardExpiryDate(data: Array<ByteArray>): Calendar {
        val expiryDateDays = (data[2][10].toUInt().and(0x7Fu).shl(7)
                or data[2][11].toUInt().and(0xFEu).shr(1))
        val expiryDate = this.uIntToDate(if (expiryDateDays != 0u) expiryDateDays else 16070u, 1439u)

        when (this.getOccasionalCardFareTypeId(data)) {
            FareProductId.OCC_24HOURS_BUS.id,
            FareProductId.OCC_24HOURS_BUS_OOT.id,
            FareProductId.OCC_24HOURS_ALL_MODES_A.id,
            FareProductId.OCC_24HOURS_ALL_MODES_AB.id,
            FareProductId.OCC_24HOURS_ALL_MODES_ABC.id,
            FareProductId.OCC_24HOURS_ALL_MODES_ABCD.id -> {
                expiryDate.add(Calendar.DATE, 1)
            }

            FareProductId.OCC_EVENING_UNLIMITED.id -> {
                expiryDate.add(Calendar.DATE, 1)
            }

            FareProductId.OCC_WEEKEND_UNLIMITED.id -> {
                expiryDate.add(Calendar.DATE, -1)
            }

            FareProductId.OCC_2TICKETS_ALL_MODES_ABCD_SPECIAL_ILE_AUX_TOURTES.id -> {
                expiryDate.set(2024, 4, 31, 23, 59)
            }
        }

        return expiryDate
    }


    private fun getOccasionalCardFare(data: Array<ByteArray>): ArrayList<Fare> {
        val fares = ArrayList<Fare>()

        val typeId = this.getOccasionalCardFareTypeId(data)
        val operatorId = this.getOccasionalCardFareOperatorId(data)
        val buyingDate = this.getOccasionalCardFareBuyingDate(data)

        if (this.occasionalCardHasTicket(data)) {
            val ticketCount = when (typeId) {
                FareProductId.OCC_2TICKETS_ALL_MODES_ABCD_SPECIAL_ILE_AUX_TOURTES.id -> 0u
                else -> this.getOccasionalCardFareNbOfTickets(data)
            }

            fares.add(Fare(typeId, operatorId, buyingDate, ticketCount))
        } else if (this.occasionalCardHasPass(data)) {
            val validityFromDate = getOccasionalCardFareValidityFromDate(data[2])
            if (occasionalCardHasValidPass(data)) {
                fares.add(Fare(typeId, operatorId, buyingDate, null, validityFromDate, null))
            } else if (validityFromDate != null) {
                val validityUntilDate = getOccasionalCardFareValidityUntilDate(data[2])
                fares.add(Fare(typeId, operatorId, buyingDate, null, validityFromDate, validityUntilDate))
            }
        }

        return fares
    }

    private fun getOccasionalCardFareTypeId(data: Array<ByteArray>): UInt {
        return (data[1][4].toUInt().and(0x03u).shl(10)
                or data[1][5].toUInt().and(0xFFu).shl(2)
                or data[1][6].toUInt().and(0xC0u).shr(6))
    }

    private fun getOccasionalCardFareOperatorId(data: Array<ByteArray>): UInt {
        return data[1][6].toUInt().and(0x3Fu)
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
        val ticketBits = (data[0][12].toUInt().and(0xFFu).shl(24)
                or data[0][13].toUInt().and(0xFFu).shl(16)
                or data[0][14].toUInt().and(0xFFu).shl(8)
                or data[0][15].toUInt().and(0xFFu))

        return when (ticketBits) {
            0xC0000000u -> 30u
            0xE0000000u -> 29u
            0xF0000000u -> 28u
            0xF8000000u -> 27u
            0xFC000000u -> 26u
            0xFE000000u -> 25u
            0xFF000000u -> 24u
            0xFF800000u -> 23u
            0xFFC00000u -> 22u
            0xFFE00000u -> 21u
            0xFFF00000u -> 20u
            0xFFF80000u -> 19u
            0xFFFC0000u -> 18u
            0xFFFE0000u -> 17u
            0xFFFF0000u -> 16u
            0xFFFF8000u -> 15u
            0xFFFFC000u -> 14u
            0xFFFFE000u -> 13u
            0xFFFFF000u -> 12u
            0xFFFFF800u -> 11u
            0xFFFFFC00u -> 10u
            0xFFFFFE00u -> 9u
            0xFFFFFF00u -> 8u
            0xFFFFFF80u -> 7u
            0xFFFFFFC0u -> 6u
            0xFFFFFFE0u -> 5u
            0xFFFFFFF0u -> 4u
            0xFFFFFFF8u -> 3u
            0xFFFFFFFCu -> 2u
            0xFFFFFFFEu -> 1u
            0xFFFFFFFFu -> 0u

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

    private fun getOccasionalCardFareValidityUntilDate(data: ByteArray): Calendar? {
        val validityUntilDays = (data[10].toUInt().and(0x7Fu).shl(7)
                or data[11].toUInt().and(0xFEu).shr(1))

        return if (validityUntilDays != 0u) this.uIntToDate(validityUntilDays, 0u) else null
    }


    private fun getOccasionalCardTrips(data: Array<ByteArray>): ArrayList<Trip> {
        val trips = ArrayList<Trip>()
        for (i in 2..3) {
            val lineId = this.getOccasionalCardTripLineId(data[i])
            val operatorId = this.getOccasionalCardTripOperatorId(data[i])
            val zoneId = this.getOccasionalCardTripZoneId(data[i])
            val useDate = this.getOccasionalCardTripUseDate(data[i])
            val firstUseDate = this.getOccasionalCardTripFirstUseDate(data[i])

            trips.add(Trip(lineId, operatorId, zoneId, useDate, firstUseDate))
        }

        return trips
    }

    private fun getOccasionalCardTripLineId(data: ByteArray): UInt {
        return (data[5].toUInt().and(0x1Fu).shl(4)
                or data[6].toUInt().and(0xF0u).shr(4))
    }

    private fun getOccasionalCardTripOperatorId(data: ByteArray): UInt {
        return (data[12].toUInt().and(0x01u).shl(5)
                or data[13].toUInt().and(0xF8u).shr(3))
    }

    private fun getOccasionalCardTripZoneId(data: ByteArray): UInt {
        return data[8].toUInt().and(0xFFu)
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

    private fun getOpusCardExpiryDate(data: ByteArray): Calendar {
        val daysUntilExpiry = (data[5].toUInt().and(0x07u).shl(11)
                or data[6].toUInt().and(0xFFu).shl(3)
                or data[7].toUInt().and(0xE0u).shr(5))

        return uIntToDate(daysUntilExpiry, 0u)
    }

    private fun getOpusCardBirthDate(data: ByteArray): Calendar? {
        val yearThousand = data[9].toUInt().and(0x1Eu).shr(1)
        val yearHundred = (data[9].toUInt().and(0x01u).shl(3)
                or data[10].toUInt().and(0xE0u).shr(5))
        val yearTen = data[10].toUInt().and(0x1Eu).shr(1)
        val yearUnit = (data[10].toUInt().and(0x01u).shl(3)
                or data[11].toUInt().and(0xE0u).shr(5))
        val year = (yearThousand * 1000u + yearHundred * 100u + yearTen * 10u + yearUnit).toInt()

        val monthTen = data[11].toUInt().and(0x1Eu).shr(1)
        val monthUnit = (data[11].toUInt().and(0x01u).shl(3)
                or data[12].toUInt().and(0xE0u).shr(5))
        val month = (monthTen * 10u + monthUnit).toInt()

        val dateTen = data[12].toUInt().and(0x1Eu).shr(1)
        val dateUnit = (data[12].toUInt().and(0x01u).shl(3)
                or data[13].toUInt().and(0xE0u).shr(5))
        val date = (dateTen * 10u + dateUnit).toInt()

        if (date == 0) {
            return null
        }

        val birthDate = Calendar.getInstance()
        birthDate.set(year, month - 1, date, 0, 0, 0)

        return birthDate
    }

    private fun getOpusCardTypeVariant(data: ByteArray): UInt {
        return (data[13].toUInt().and(0x03u).shl(8)
                or data[14].toUInt().and(0xFFu))
    }


    private fun getOpusCardTrips(card: IsoDep, fares: ArrayList<Fare>): ArrayList<Trip> {
        val trips = ArrayList<Trip>()
        card.transceive(this.hexStringToByteArray("94a408000420002010"))
        for (i in 1..3) {
            val data = card.transceive(this.hexStringToByteArray("94b20${i}0400"))

            val lineId: UInt
            val operatorId: UInt
            val firstUseDate: Calendar
            val fareIndex: UInt
            val isValid: Boolean
            if (this.opusCardHasToUseByteOffset(data)) {
                lineId = this.getOpusCardTripLineId(data, 1)
                operatorId = this.getOpusCardTripOperatorId(data, 1)
                firstUseDate = this.getOpusCardTripFirstUseDate(data, 5)
                fareIndex = this.getOpusCardTripFareIndex(data, 5)
                isValid = this.isValidOpusCardTrip(data, 1)
            } else {
                lineId = this.getOpusCardTripLineId(data)
                operatorId = this.getOpusCardTripOperatorId(data)
                firstUseDate = this.getOpusCardTripFirstUseDate(data)
                fareIndex = this.getOpusCardTripFareIndex(data)
                isValid = this.isValidOpusCardTrip(data)
            }

            val zoneId = this.getOpusCardTripZoneId(data)
            val useDate = this.getOpusCardTripUseDate(data)

            trips.add(Trip(lineId, operatorId, zoneId, useDate, firstUseDate, fareIndex, fares[fareIndex.toInt() - 1].typeId, isValid))
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
        return (data[7 + byteOffset].toUInt().and(0x01u).shl(5)
                or data[8 + byteOffset].toUInt().and(0xF8u).shr(3))
    }

    private fun getOpusCardTripZoneId(data: ByteArray): UInt {
        return (data[9].toUInt().and(0x07u).shl(5)
                or data[10].toUInt().and(0xF8u).shr(3))
    }

    private fun getOpusCardTripUseDate(data: ByteArray): Calendar {
        val tripUseDays = (data[0].toUInt().and(0xFFu).shl(6)
                or data[1].toUInt().and(0xFCu).shr(2))
        val tripUseMinutes = (data[1].toUInt().and(0x03u).shl(9)
                or data[2].toUInt().and(0xFFu).shl(1)
                or data[3].toUInt().and(0x80u).shr(7))

        return this.uIntToDate(tripUseDays, tripUseMinutes)
    }

    private fun getOpusCardTripFareIndex(data: ByteArray, byteOffset: Int = 0): UInt {
        return (data[12 + byteOffset].toUInt().and(0x01u).shl(2)
                or data[13 + byteOffset].toUInt().and(0xC0u).shr(6))
    }

    private fun getOpusCardTripFirstUseDate(data: ByteArray, byteOffset: Int = 0): Calendar {
        val tripFirstUseDays = (data[14 + byteOffset].toUInt().and(0x7Fu).shl(7)
                or data[15 + byteOffset].toUInt().and(0xFEu).shr(1))
        val tripFirstUseMinutes = (data[15 + byteOffset].toUInt().and(0x01u).shl(10)
                or data[16 + byteOffset].toUInt().and(0xFFu).shl(2)
                or data[17 + byteOffset].toUInt().and(0xC0u).shr(6))

        return this.uIntToDate(tripFirstUseDays, tripFirstUseMinutes)
    }

    private fun isValidOpusCardTrip(data: ByteArray, byteOffset: Int = 0): Boolean {
        return data[7 + byteOffset].toUInt().and(0x38u).shr(3) == 0u
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

                fares.add(
                    Fare(
                        typeId,
                        operatorId,
                        buyingDate,
                        ticketCount,
                        null,
                        null,
                        true,
                        i.toUInt()
                    )
                )
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
                        true,
                        i.toUInt()
                    )
                )
            }
        }

        return fares
    }

    private fun getOpusCardFareTypeId(data: ByteArray): UInt {
        return (data[2].toUInt().and(0x7Fu).shl(7)
            or data[3].toUInt().and(0xFEu).shr(1))
    }

    private fun getOpusCardFareOperatorId(data: ByteArray): UInt {
        return data[1].toUInt().and(0x7Eu).shr(1)
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