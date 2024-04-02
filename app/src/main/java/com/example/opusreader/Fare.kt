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
) : Serializable {
    init {
        val validityFromDateValue = this.validityFromDate
        if (validityFromDateValue != null) {
            this.validityFromDate = setFareValidityFromDate(validityFromDateValue)
            this.validityUntilDate = setFareValidityUntilDate(validityFromDateValue)
        }
    }

    private fun setFareValidityFromDate(validityFromDate: Calendar): Calendar? {
        val date = Calendar.getInstance()

        return when (typeId) {
            FareProductId.OPUS_EVENING_UNLIMITED.ID -> {
                date.set(
                    validityFromDate.get(Calendar.YEAR),
                    validityFromDate.get(Calendar.MONTH),
                    validityFromDate.get(Calendar.DATE),
                    18,
                    0
                )

                date
            }

            FareProductId.OCC_3DAYS_ALL_MODES_AB.ID -> {
                date.set(
                    validityFromDate.get(Calendar.YEAR),
                    validityFromDate.get(Calendar.MONTH),
                    validityFromDate.get(Calendar.DATE) - 2,
                    0,
                    0
                )

                if (date.timeInMillis < buyingDate.timeInMillis) {
                    date.set(
                        buyingDate.get(Calendar.YEAR),
                        buyingDate.get(Calendar.MONTH),
                        buyingDate.get(Calendar.DATE),
                        0,
                        0
                    )
                }

                date
            }
            FareProductId.OCC_3DAYS_ALL_MODES_ABC.ID -> {
                date.set(
                    validityFromDate.get(Calendar.YEAR),
                    validityFromDate.get(Calendar.MONTH),
                    validityFromDate.get(Calendar.DATE) - 2,
                    0,
                    0
                )

                if (date.timeInMillis < buyingDate.timeInMillis) {
                    date.set(
                        buyingDate.get(Calendar.YEAR),
                        buyingDate.get(Calendar.MONTH),
                        buyingDate.get(Calendar.DATE),
                        0,
                        0
                    )
                }

                date
            }
            FareProductId.OCC_EVENING_UNLIMITED.ID -> {
                if (validityFromDate.get(Calendar.HOUR_OF_DAY) >= 18) {
                    date.set(
                        validityFromDate.get(Calendar.YEAR),
                        validityFromDate.get(Calendar.MONTH),
                        validityFromDate.get(Calendar.DATE),
                        18,
                        0
                    )
                } else {
                    date.set(
                        validityFromDate.get(Calendar.YEAR),
                        validityFromDate.get(Calendar.MONTH),
                        validityFromDate.get(Calendar.DATE) - 1,
                        18,
                        0
                    )
                }

                date
            }
            FareProductId.OCC_WEEKEND_UNLIMITED.ID -> {
                val daysToRemove = when (validityFromDate.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.SATURDAY -> 1
                    Calendar.SUNDAY -> 2
                    Calendar.MONDAY -> 3
                    else -> 0
                }
                date.set(
                    validityFromDate.get(Calendar.YEAR),
                    validityFromDate.get(Calendar.MONTH),
                    validityFromDate.get(Calendar.DATE) - daysToRemove,
                    16,
                    0
                )

                date
            }

            else -> validityFromDate
        }
    }

    private fun setFareValidityUntilDate(validityFromDate: Calendar): Calendar? {
        val date = Calendar.getInstance()

        return when (typeId) {
            FareProductId.OPUS_MONTHLY_STL.ID -> {
                date.set(
                    validityFromDate.get(Calendar.YEAR),
                    validityFromDate.get(Calendar.MONTH) + 1,
                    validityFromDate.get(Calendar.DATE) - 1,
                    23,
                    59
                )

                date
            }

            FareProductId.OPUS_EVENING_UNLIMITED.ID -> {
                date.set(
                    validityFromDate.get(Calendar.YEAR),
                    validityFromDate.get(Calendar.MONTH),
                    validityFromDate.get(Calendar.DATE) + 1,
                    5,
                    0
                )

                date
            }
            FareProductId.OPUS_MONTHLY_ALL_MODES_AB.ID -> {
                date.set(
                    validityFromDate.get(Calendar.YEAR),
                    validityFromDate.get(Calendar.MONTH) + 1,
                    validityFromDate.get(Calendar.DATE) - 1,
                    23,
                    59
                )

                date
            }


            FareProductId.OCC_24HOURS_ALL_MODES_AB.ID -> {
                date.set(
                    validityFromDate.get(Calendar.YEAR),
                    validityFromDate.get(Calendar.MONTH),
                    validityFromDate.get(Calendar.DATE) + 1,
                    validityFromDate.get(Calendar.HOUR_OF_DAY),
                    validityFromDate.get(Calendar.MINUTE)
                )

                date
            }
            FareProductId.OCC_3DAYS_ALL_MODES_AB.ID -> {
                date.set(
                    buyingDate.get(Calendar.YEAR),
                    buyingDate.get(Calendar.MONTH),
                    buyingDate.get(Calendar.DATE) + 2,
                    23,
                    59
                )

                if (validityFromDate.timeInMillis > date.timeInMillis) {
                    date.set(
                        validityFromDate.get(Calendar.YEAR),
                        validityFromDate.get(Calendar.MONTH),
                        validityFromDate.get(Calendar.DATE),
                        23,
                        59
                    )
                }

                date
            }
            FareProductId.OCC_3DAYS_ALL_MODES_ABC.ID -> {
                date.set(
                    buyingDate.get(Calendar.YEAR),
                    buyingDate.get(Calendar.MONTH),
                    buyingDate.get(Calendar.DATE) + 2,
                    23,
                    59
                )

                if (validityFromDate.timeInMillis > date.timeInMillis) {
                    date.set(
                        validityFromDate.get(Calendar.YEAR),
                        validityFromDate.get(Calendar.MONTH),
                        validityFromDate.get(Calendar.DATE),
                        23,
                        59
                    )
                }

                date
            }
            FareProductId.OCC_EVENING_UNLIMITED.ID -> {
                if (validityFromDate.get(Calendar.HOUR_OF_DAY) >= 18) {
                    date.set(
                        validityFromDate.get(Calendar.YEAR),
                        validityFromDate.get(Calendar.MONTH),
                        validityFromDate.get(Calendar.DATE) + 1,
                        5,
                        0
                    )
                } else {
                    date.set(
                        validityFromDate.get(Calendar.YEAR),
                        validityFromDate.get(Calendar.MONTH),
                        validityFromDate.get(Calendar.DATE),
                        5,
                        0
                    )
                }

                date
            }
            FareProductId.OCC_WEEKEND_UNLIMITED.ID -> {
                val daysToAdd = when (validityFromDate.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.FRIDAY -> 3
                    Calendar.SATURDAY -> 2
                    Calendar.SUNDAY -> 1
                    else -> 0
                }
                date.set(
                    validityFromDate.get(Calendar.YEAR),
                    validityFromDate.get(Calendar.MONTH),
                    validityFromDate.get(Calendar.DATE) + daysToAdd,
                    5,
                    0
                )

                date
            }

            else -> validityUntilDate
        }
    }
}