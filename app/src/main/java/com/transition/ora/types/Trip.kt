package com.transition.ora.types

import java.io.Serializable
import java.util.Calendar


class Trip(
    var lineId: UInt,
    var operatorId: UInt,
    var zoneId: UInt,
    var useDate: Calendar,
    var firstUseDate: Calendar,
    var fareIndex: UInt = 1u
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