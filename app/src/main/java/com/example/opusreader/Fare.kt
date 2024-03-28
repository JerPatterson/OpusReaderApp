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

    fun hasValidityDates(): Boolean {
        val validityFromDateValue = this.validityFromDate
        val validityUntilDateValue = this.validityUntilDate
        return validityFromDateValue != null && validityUntilDateValue != null
    }

    private fun setFareValidityFromDate(validityFromDate: Calendar): Calendar? {
        val date = Calendar.getInstance()

        return when (typeId) {
            744u -> {
                date.set(
                    validityFromDate.get(Calendar.YEAR),
                    validityFromDate.get(Calendar.MONTH),
                    validityFromDate.get(Calendar.DATE),
                    18,
                    0
                )
                date
            }

            3305921u -> {
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
            3305985u -> {
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
                    0,
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
            744u -> {
                date.set(
                    validityFromDate.get(Calendar.YEAR),
                    validityFromDate.get(Calendar.MONTH),
                    validityFromDate.get(Calendar.DATE) + 1,
                    5,
                    0
                )
                date
            }
            752u -> {
                date.set(
                    validityFromDate.get(Calendar.YEAR),
                    validityFromDate.get(Calendar.MONTH) + 1,
                    validityFromDate.get(Calendar.DATE) - 1,
                    23,
                    59
                )
                date
            }


            3322369u -> {
                date.set(
                    validityFromDate.get(Calendar.YEAR),
                    validityFromDate.get(Calendar.MONTH),
                    validityFromDate.get(Calendar.DATE) + 1,
                    validityFromDate.get(Calendar.HOUR_OF_DAY),
                    validityFromDate.get(Calendar.MINUTE)
                )
                date
            }
            3321601u -> {
                date.set(
                    validityFromDate.get(Calendar.YEAR),
                    validityFromDate.get(Calendar.MONTH),
                    validityFromDate.get(Calendar.DATE) + 2,
                    23,
                    59
                )
                date
            }
            3305921u -> {
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
            3305985u -> {
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