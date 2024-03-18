package com.example.opusreader

import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Card(
    var id: ULong,
    var type: CardType,
    var scanDate: Calendar,
    var expiryDate: Calendar?,
    var fares: ArrayList<Fare>,
    var trips: ArrayList<Trip>
) {
    fun getCardEntity(): CardEntity {
        return CardEntity(
            id.toString(),
            type.name,
            scanDate.timeInMillis.toString(),
            expiryDate?.timeInMillis.toString(),
            Gson().toJson(fares, ArrayList<Fare>()::class.java).toString(),
            Gson().toJson(trips, ArrayList<Fare>()::class.java).toString(),
        )
    }
}