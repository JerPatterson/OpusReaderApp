package com.transition.ora

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
            FareProductId.OCC_3DAYS_BUS.id,
            FareProductId.OCC_3DAYS_BUS_OOT.id,
            FareProductId.OCC_3DAYS_ALL_MODES_A.id,
            FareProductId.OCC_3DAYS_ALL_MODES_AB.id,
            FareProductId.OCC_3DAYS_ALL_MODES_ABC.id,
            FareProductId.OCC_3DAYS_ALL_MODES_ABCD.id -> {
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


            FareProductId.OPUS_EVENING_UNLIMITED.id -> {
                date.set(
                    validityFromDate.get(Calendar.YEAR),
                    validityFromDate.get(Calendar.MONTH),
                    validityFromDate.get(Calendar.DATE),
                    18,
                    0
                )
                date
            }
            FareProductId.OCC_EVENING_UNLIMITED.id -> {
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

            FareProductId.OCC_WEEKEND_UNLIMITED.id -> {
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
            FareProductId.OPUS_MONTHLY_TRAM1.id,
            FareProductId.OPUS_MONTHLY_TRAM1_STU.id,
            FareProductId.OPUS_MONTHLY_TRAM1_RED.id,
            FareProductId.OPUS_MONTHLY_TRAM2.id,
            FareProductId.OPUS_MONTHLY_TRAM2_STU.id,
            FareProductId.OPUS_MONTHLY_TRAM2_RED.id,
            FareProductId.OPUS_MONTHLY_TRAM3.id,
            FareProductId.OPUS_MONTHLY_TRAM3_STU.id,
            FareProductId.OPUS_MONTHLY_TRAM3_RED.id,
            FareProductId.OPUS_MONTHLY_TRAM4.id,
            FareProductId.OPUS_MONTHLY_TRAM4_STU.id,
            FareProductId.OPUS_MONTHLY_TRAM4_RED.id,
            FareProductId.OPUS_MONTHLY_TRAM5.id,
            FareProductId.OPUS_MONTHLY_TRAM5_STU.id,
            FareProductId.OPUS_MONTHLY_TRAM5_RED.id,
            FareProductId.OPUS_MONTHLY_TRAM6.id,
            FareProductId.OPUS_MONTHLY_TRAM6_STU.id,
            FareProductId.OPUS_MONTHLY_TRAM6_RED.id,
            FareProductId.OPUS_MONTHLY_TRAM7.id,
            FareProductId.OPUS_MONTHLY_TRAM7_STU.id,
            FareProductId.OPUS_MONTHLY_TRAM7_RED.id,
            FareProductId.OPUS_MONTHLY_TRAM8.id,
            FareProductId.OPUS_MONTHLY_TRAM8_STU.id,
            FareProductId.OPUS_MONTHLY_TRAM8_RED.id,
            FareProductId.OPUS_MONTHLY_TRAIN4.id,
            FareProductId.OPUS_MONTHLY_TRAIN4_STU.id,
            FareProductId.OPUS_MONTHLY_TRAIN4_RED.id,
            FareProductId.OPUS_MONTHLY_TRAIN5.id,
            FareProductId.OPUS_MONTHLY_TRAIN5_STU.id,
            FareProductId.OPUS_MONTHLY_TRAIN5_RED.id,
            FareProductId.OPUS_MONTHLY_TRAIN6.id,
            FareProductId.OPUS_MONTHLY_TRAIN6_STU.id,
            FareProductId.OPUS_MONTHLY_TRAIN6_RED.id,
            FareProductId.OPUS_MONTHLY_TRAIN7.id,
            FareProductId.OPUS_MONTHLY_TRAIN7_STU.id,
            FareProductId.OPUS_MONTHLY_TRAIN7_RED.id,
            FareProductId.OPUS_MONTHLY_RTL.id,
            FareProductId.OPUS_MONTHLY_RTL_STU.id,
            FareProductId.OPUS_MONTHLY_STL.id,
            FareProductId.OPUS_MONTHLY_STL_STU.id,
            FareProductId.OPUS_MONTHLY_STL_RED.id,
            FareProductId.OPUS_MONTHLY_STM.id,
            FareProductId.OPUS_MONTHLY_STM_RED.id,
            FareProductId.OPUS_MONTHLY_BUS.id,
            FareProductId.OPUS_MONTHLY_BUS_STU.id,
            FareProductId.OPUS_MONTHLY_BUS_RED.id,
            FareProductId.OPUS_MONTHLY_BUS_OOT.id,
            FareProductId.OPUS_MONTHLY_BUS_OOT_STU.id,
            FareProductId.OPUS_MONTHLY_BUS_OOT_RED.id,
            FareProductId.OPUS_MONTHLY_ALL_MODES_A.id,
            FareProductId.OPUS_MONTHLY_ALL_MODES_A_STU.id,
            FareProductId.OPUS_MONTHLY_ALL_MODES_A_RED.id,
            FareProductId.OPUS_MONTHLY_ALL_MODES_A_ELDER.id,
            FareProductId.OPUS_MONTHLY_ALL_MODES_AB.id,
            FareProductId.OPUS_MONTHLY_ALL_MODES_AB_STU.id,
            FareProductId.OPUS_MONTHLY_ALL_MODES_AB_RED.id,
            FareProductId.OPUS_MONTHLY_ALL_MODES_ABC.id,
            FareProductId.OPUS_MONTHLY_ALL_MODES_ABC_STU.id,
            FareProductId.OPUS_MONTHLY_ALL_MODES_ABC_RED.id,
            FareProductId.OPUS_MONTHLY_ALL_MODES_ABCD.id,
            FareProductId.OPUS_MONTHLY_ALL_MODES_ABCD_STU.id,
            FareProductId.OPUS_MONTHLY_ALL_MODES_ABCD_RED.id,
            FareProductId.OPUS_MONTHLY_TRAIN3_SPECIAL_LULA.id,
            FareProductId.OPUS_MONTHLY_TRAIN3_SPECIAL_LULA_RED.id,
            FareProductId.OPUS_MONTHLY_TRAIN4_SPECIAL_LULA.id,
            FareProductId.OPUS_MONTHLY_TRAIN4_SPECIAL_LULA_RED.id,
            FareProductId.OPUS_MONTHLY_TRAIN5_SPECIAL_LULA.id,
            FareProductId.OPUS_MONTHLY_TRAIN5_SPECIAL_LULA_RED.id -> {
                date.set(
                    validityFromDate.get(Calendar.YEAR),
                    validityFromDate.get(Calendar.MONTH) + 1,
                    validityFromDate.get(Calendar.DATE) - 1,
                    23,
                    59
                )

                date
            }


            FareProductId.OCC_24HOURS_BUS.id,
            FareProductId.OCC_24HOURS_BUS_OOT.id,
            FareProductId.OCC_24HOURS_ALL_MODES_A.id,
            FareProductId.OCC_24HOURS_ALL_MODES_AB.id,
            FareProductId.OCC_24HOURS_ALL_MODES_ABC.id,
            FareProductId.OCC_24HOURS_ALL_MODES_ABCD.id -> {
                date.set(
                    validityFromDate.get(Calendar.YEAR),
                    validityFromDate.get(Calendar.MONTH),
                    validityFromDate.get(Calendar.DATE) + 1,
                    validityFromDate.get(Calendar.HOUR_OF_DAY),
                    validityFromDate.get(Calendar.MINUTE)
                )

                date
            }

            FareProductId.OCC_3DAYS_BUS.id,
            FareProductId.OCC_3DAYS_BUS_OOT.id,
            FareProductId.OCC_3DAYS_ALL_MODES_A.id,
            FareProductId.OCC_3DAYS_ALL_MODES_AB.id,
            FareProductId.OCC_3DAYS_ALL_MODES_ABC.id,
            FareProductId.OCC_3DAYS_ALL_MODES_ABCD.id -> {
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
            FareProductId.OCC_EVENING_UNLIMITED.id -> {
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

            FareProductId.OCC_WEEKEND_UNLIMITED.id -> {
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