package com.transition.ora.types

import com.google.gson.Gson
import com.transition.ora.database.entities.CardEntity
import com.transition.ora.enums.CardType
import java.util.Calendar


class Card(
    var id: ULong,
    var type: CardType,
    var scanDate: Calendar,
    var expiryDate: Calendar,
    val birthDate: Calendar?,
    val typeVariant: UInt?,
    private var fares: ArrayList<Fare>,
    private var trips: ArrayList<Trip>
) {
    fun getCardEntity(): CardEntity {
        return CardEntity(
            id.toString(),
            type.name,
            scanDate.timeInMillis.toString(),
            expiryDate.timeInMillis.toString(),
            birthDate?.timeInMillis?.toString(),
            typeVariant?.toString(),
            Gson().toJson(fares, ArrayList<Fare>()::class.java).toString(),
            Gson().toJson(trips, ArrayList<Fare>()::class.java).toString(),
        )
    }

    fun getFares(): List<Fare> {
        return fares.filter { fare -> fare.typeId != 0u }
            .sortedBy { fare -> fare.buyingDate.timeInMillis }
            .asReversed()
    }

    fun getTrips(filterOutInvalid: Boolean = true): List<Trip> {
        return trips.withIndex().filter { (i, trip) -> trip.operatorId != 0u
                && (i == 0 || trip.useDate.time != trips[i - 1].useDate.time)
                && (!filterOutInvalid || trip.isValid ?: false) }
            .sortedBy { trip -> trip.value.useDate.timeInMillis }
            .map { (_, trip) -> trip }
    }
}