package com.example.opusreader

import java.util.Calendar

class Card(
    var type: CardType,
    var id: ULong,
    var expiryDate: Calendar?,
    var fares: ArrayList<Fare>,
    var trips: ArrayList<Trip>
)