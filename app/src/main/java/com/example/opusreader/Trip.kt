package com.example.opusreader

import java.io.Serializable
import java.util.Calendar

class Trip(
    var lineId: UInt,
    var operatorId: UInt,
    var useDate: Calendar,
    var firstUseDate: Calendar
): Serializable