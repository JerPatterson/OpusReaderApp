package com.example.opusreader

import java.io.Serializable
import java.util.Calendar

class Fare(
    var typeId: UInt,
    var operatorId: UInt,
    var buyingDate: Calendar,
    var ticketCount: UInt?,
    var validityFromDate: Calendar? = null,
    var validityUntilDate: Calendar? = null,
    var buyingDateHasMinutes: Boolean = false
): Serializable