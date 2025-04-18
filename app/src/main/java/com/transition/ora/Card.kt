package com.transition.ora

import com.google.gson.Gson
import java.util.Calendar


class Card(
    var id: ULong,
    var type: CardType,
    var scanDate: Calendar,
    var expiryDate: Calendar,
    val birthDate: Calendar?,
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
            Gson().toJson(fares, ArrayList<Fare>()::class.java).toString(),
            Gson().toJson(trips, ArrayList<Fare>()::class.java).toString(),
        )
    }

    fun getFares(): List<Fare> {
        return fares.filter { fare -> fare.typeId != 0u }
            .sortedBy { fare -> fare.buyingDate.timeInMillis }
            .asReversed()
    }

    fun getUnlimitedFares(): List<Fare> {
        return getFares().filter { fare -> fare.validityUntilDate != null }
            .sortedBy { fare -> fare.validityUntilDate }
    }

    fun getTrips(): List<Trip> {
        return trips.withIndex().filter { (i, trip) -> trip.operatorId != 0u
                && (i == 0 || trip.useDate.time != trips[i - 1].useDate.time) }
            .sortedBy { trip -> trip.value.useDate.timeInMillis }
            .map { (_, trip) -> trip }
    }
}