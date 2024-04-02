package com.example.opusreader

import java.io.Serializable
import java.util.Calendar

class Trip(
    var lineId: UInt,
    var operatorId: UInt,
    var useDate: Calendar,
    var firstUseDate: Calendar
): Serializable {
    init {
        val date = Calendar.getInstance()
        if (firstUseDate.timeInMillis - useDate.timeInMillis > 1000) {
            date.set(
                useDate.get(Calendar.YEAR),
                useDate.get(Calendar.MONTH),
                useDate.get(Calendar.DATE) + 1,
                useDate.get(Calendar.HOUR_OF_DAY),
                useDate.get(Calendar.MINUTE),
            )

            useDate = date
        }
    }
}