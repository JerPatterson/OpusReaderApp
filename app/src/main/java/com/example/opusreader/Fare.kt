package com.example.opusreader

import java.util.Calendar

class Fare {
    private var id: UInt
    private var operatorId: UInt
    private var ticketCount: UInt?
    private var buyingDate: Calendar
    private var validityFromDate: Calendar
    private var validityUntilDate: Calendar?

    constructor(
        id: UInt,
        operatorId: UInt,
        ticketCount: UInt?,
        buyingDate: Calendar,
        validityFromDate: Calendar,
        validityUntilDate: Calendar?
    ) {
        this.id = id
        this.operatorId = operatorId
        this.ticketCount = ticketCount
        this.buyingDate = buyingDate
        this.validityFromDate = validityFromDate
        this.validityUntilDate = validityUntilDate
    }
}