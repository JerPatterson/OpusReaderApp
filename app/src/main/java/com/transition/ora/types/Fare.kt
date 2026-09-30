package com.transition.ora.types

import com.transition.ora.enums.FareProductId
import java.io.Serializable
import java.util.Calendar


class Fare(
    var typeId: UInt,
    var operatorId: UInt,
    var buyingId: UInt,
    var buyingDate: Calendar,
    var ticketCount: UInt?,
    var validityFromDate: Calendar? = null,
    var validityUntilDate: Calendar? = null,
    var reloadingDate: Calendar? = null,
    var buyingDateHasMinutes: Boolean = false,
    var fareIndex: UInt = 1u
) : Serializable {
    init {
        val validityFromDateValue = this.validityFromDate
        val validityUntilDateValue = this.validityUntilDate
        if (validityFromDateValue != null && validityUntilDateValue != null) {
            this.validityFromDate = setFareValidityFromDate(validityFromDateValue, validityUntilDateValue)
            this.validityUntilDate = setFareValidityUntilDate(validityFromDateValue, validityUntilDateValue)
        }
    }

    private fun setFareValidityFromDate(validityFromDate: Calendar, validityUntilDate: Calendar): Calendar? {
        val date = Calendar.getInstance()

        return when (typeId) {
            FareProductId.OCC_24HOURS_RTC.id,
            FareProductId.OCC_24HOURS_RTL.id,
            FareProductId.OCC_24HOURS_BUS.id,
            FareProductId.OCC_24HOURS_BUS_OOT.id,
            FareProductId.OCC_24HOURS_ALL_MODES_A.id,
            FareProductId.OCC_24HOURS_ALL_MODES_AB.id,
            FareProductId.OCC_24HOURS_ALL_MODES_ABC.id,
            FareProductId.OCC_24HOURS_ALL_MODES_ABCD.id,

            FareProductId.OPUS_24HOURS_RTC.id,
            FareProductId.OPUS_24HOURS_BUS.id,
            FareProductId.OPUS_24HOURS_BUS_OOT.id,
            FareProductId.OPUS_24HOURS_ALL_MODES_A.id,
            FareProductId.OPUS_24HOURS_ALL_MODES_AB.id,
            FareProductId.OPUS_24HOURS_ALL_MODES_ABC.id,
            FareProductId.OPUS_24HOURS_ALL_MODES_ABCD.id -> {
                validityFromDate
            }

            FareProductId.OCC_3DAYS_BUS.id,
            FareProductId.OCC_3DAYS_BUS_OOT.id,
            FareProductId.OCC_3DAYS_ALL_MODES_A.id,
            FareProductId.OCC_3DAYS_ALL_MODES_AB.id,
            FareProductId.OCC_3DAYS_ALL_MODES_ABC.id,
            FareProductId.OCC_3DAYS_ALL_MODES_ABCD.id,

            FareProductId.OPUS_3DAYS_BUS.id,
            FareProductId.OPUS_3DAYS_BUS_OOT.id,
            FareProductId.OPUS_3DAYS_ALL_MODES_A.id,
            FareProductId.OPUS_3DAYS_ALL_MODES_AB.id,
            FareProductId.OPUS_3DAYS_ALL_MODES_ABC.id,
            FareProductId.OPUS_3DAYS_ALL_MODES_ABCD.id -> {
                date.set(
                    validityUntilDate.get(Calendar.YEAR),
                    validityUntilDate.get(Calendar.MONTH),
                    validityUntilDate.get(Calendar.DATE) - 2,
                    0,
                    0
                )

                date
            }

            FareProductId.OPUS_WEEKLY_ALL_MODES_A.id,
            FareProductId.OPUS_WEEKLY_ALL_MODES_A_RED.id,
            FareProductId.OPUS_WEEKLY_ALL_MODES_A_ELDER.id -> {
                val daysToRemove = when (validityFromDate.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.TUESDAY -> 1
                    Calendar.WEDNESDAY -> 2
                    Calendar.THURSDAY -> 3
                    Calendar.FRIDAY -> 4
                    Calendar.SATURDAY -> 5
                    Calendar.SUNDAY -> 6
                    else -> 0
                }
                date.set(
                    validityFromDate.get(Calendar.YEAR),
                    validityFromDate.get(Calendar.MONTH),
                    validityFromDate.get(Calendar.DATE) - daysToRemove,
                    0,
                    0
                )

                date
            }

            FareProductId.OCC_9DAYS_ALL_MODES_ABC_SPECIAL_UCI_CHAMPIONSHIPS.id,
            FareProductId.OPUS_9DAYS_ALL_MODES_ABC_SPECIAL_UCI_CHAMPIONSHIPS.id -> {
                date.set(
                    2026,
                    9,
                    19,
                    0,
                    0
                )

                date
            }


            FareProductId.OCC_EVENING_UNLIMITED.id,
            FareProductId.OPUS_EVENING_UNLIMITED.id -> {
                date.set(
                    validityUntilDate.get(Calendar.YEAR),
                    validityUntilDate.get(Calendar.MONTH),
                    validityUntilDate.get(Calendar.DATE),
                    18,
                    0
                )

                date
            }

            FareProductId.OCC_WEEKEND_UNLIMITED.id,
            FareProductId.OPUS_WEEKEND_UNLIMITED.id -> {
                val daysToRemove = when (validityUntilDate.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.SATURDAY -> 1
                    Calendar.SUNDAY -> 2
                    Calendar.MONDAY -> 3
                    Calendar.TUESDAY -> 4
                    else -> 0
                }
                date.set(
                    validityUntilDate.get(Calendar.YEAR),
                    validityUntilDate.get(Calendar.MONTH),
                    validityUntilDate.get(Calendar.DATE) - daysToRemove,
                    16,
                    0
                )

                date
            }
            FareProductId.OCC_WEEKEND_UNLIMITED_SPECIAL.id -> {
                val daysToRemove = when (validityUntilDate.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.SATURDAY -> 1
                    Calendar.SUNDAY -> 2
                    Calendar.MONDAY -> 3
                    Calendar.TUESDAY -> 4
                    else -> 0
                }
                date.set(
                    validityUntilDate.get(Calendar.YEAR),
                    validityUntilDate.get(Calendar.MONTH),
                    validityUntilDate.get(Calendar.DATE) - daysToRemove,
                    when (validityUntilDate.get(Calendar.MONTH)) {
                        Calendar.MAY,
                        Calendar.JUNE -> 5
                        Calendar.JULY,
                        Calendar.AUGUST,
                        Calendar.SEPTEMBER -> when (validityUntilDate.get(Calendar.YEAR)) {
                            in 2022..2024 -> 10
                            else -> 5
                        }

                        else -> 16
                    },
                    0
                )

                date
            }

            else -> {
                date.set(
                    validityFromDate.get(Calendar.YEAR),
                    validityFromDate.get(Calendar.MONTH),
                    validityFromDate.get(Calendar.DATE),
                    0,
                    0
                )

                date
            }
        }
    }

    private fun setFareValidityUntilDate(validityFromDate: Calendar, validityUntilDate: Calendar): Calendar? {
        val date = Calendar.getInstance()

        return when (typeId) {
            FareProductId.OCC_24HOURS_RTC.id,
            FareProductId.OCC_24HOURS_RTL.id,
            FareProductId.OCC_24HOURS_BUS.id,
            FareProductId.OCC_24HOURS_BUS_OOT.id,
            FareProductId.OCC_24HOURS_ALL_MODES_A.id,
            FareProductId.OCC_24HOURS_ALL_MODES_AB.id,
            FareProductId.OCC_24HOURS_ALL_MODES_ABC.id,
            FareProductId.OCC_24HOURS_ALL_MODES_ABCD.id,

            FareProductId.OPUS_24HOURS_RTC.id,
            FareProductId.OPUS_24HOURS_BUS.id,
            FareProductId.OPUS_24HOURS_BUS_OOT.id,
            FareProductId.OPUS_24HOURS_ALL_MODES_A.id,
            FareProductId.OPUS_24HOURS_ALL_MODES_AB.id,
            FareProductId.OPUS_24HOURS_ALL_MODES_ABC.id,
            FareProductId.OPUS_24HOURS_ALL_MODES_ABCD.id -> {
                date.set(
                    validityFromDate.get(Calendar.YEAR),
                    validityFromDate.get(Calendar.MONTH),
                    validityFromDate.get(Calendar.DATE) + 1,
                    validityFromDate.get(Calendar.HOUR_OF_DAY),
                    validityFromDate.get(Calendar.MINUTE)
                )

                date
            }

            FareProductId.OPUS_WEEKLY_ALL_MODES_A.id,
            FareProductId.OPUS_WEEKLY_ALL_MODES_A_RED.id,
            FareProductId.OPUS_WEEKLY_ALL_MODES_A_ELDER.id -> {
                val daysToAdd = when (validityFromDate.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.TUESDAY -> 6
                    Calendar.WEDNESDAY -> 5
                    Calendar.THURSDAY -> 4
                    Calendar.FRIDAY -> 3
                    Calendar.SATURDAY -> 2
                    Calendar.SUNDAY -> 1
                    else -> 0
                }
                date.set(
                    validityFromDate.get(Calendar.YEAR),
                    validityFromDate.get(Calendar.MONTH),
                    validityFromDate.get(Calendar.DATE) + daysToAdd,
                    23,
                    59
                )

                date
            }

            FareProductId.OCC_EVENING_UNLIMITED.id,
            FareProductId.OPUS_EVENING_UNLIMITED.id -> {
                date.set(
                    validityFromDate.get(Calendar.YEAR),
                    validityFromDate.get(Calendar.MONTH),
                    validityFromDate.get(Calendar.DATE) + 1,
                    5,
                    0
                )
                date
            }

            FareProductId.OCC_WEEKEND_UNLIMITED.id,
            FareProductId.OCC_WEEKEND_UNLIMITED_SPECIAL.id,
            FareProductId.OPUS_WEEKEND_UNLIMITED.id -> {
                val daysToAdd = when (validityUntilDate.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.FRIDAY -> 3
                    Calendar.SATURDAY -> 2
                    Calendar.SUNDAY -> 1
                    Calendar.TUESDAY -> -1
                    else -> 0
                }
                date.set(
                    validityUntilDate.get(Calendar.YEAR),
                    validityUntilDate.get(Calendar.MONTH),
                    validityUntilDate.get(Calendar.DATE) + daysToAdd,
                    5,
                    0
                )

                date
            }

            else -> {
                date.set(
                    validityUntilDate.get(Calendar.YEAR),
                    validityUntilDate.get(Calendar.MONTH),
                    validityUntilDate.get(Calendar.DATE),
                    23,
                    59
                )

                date
            }
        }
    }
}