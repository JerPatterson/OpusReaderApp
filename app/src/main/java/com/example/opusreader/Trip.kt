package com.example.opusreader

import java.util.Calendar

class Trip {
    private var lineId: UInt
    private var operatorId: UInt
    private var useDate: Calendar
    private var firstUseDate: Calendar

    constructor(lineId: UInt, operatorId: UInt, useDate: Calendar, firstUseDate: Calendar) {
        this.lineId = lineId
        this.operatorId = operatorId
        this.useDate = useDate
        this.firstUseDate = firstUseDate
    }
}