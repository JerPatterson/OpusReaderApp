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

            FareProductId.OPUS_MONTHLY_TRAIN1.id,
            FareProductId.OPUS_MONTHLY_TRAIN1_STU.id,
            FareProductId.OPUS_MONTHLY_TRAIN1_RED.id,
            FareProductId.OPUS_MONTHLY_TRAIN2.id,
            FareProductId.OPUS_MONTHLY_TRAIN2_STU.id,
            FareProductId.OPUS_MONTHLY_TRAIN2_RED.id,
            FareProductId.OPUS_MONTHLY_TRAIN3.id,
            FareProductId.OPUS_MONTHLY_TRAIN3_STU.id,
            FareProductId.OPUS_MONTHLY_TRAIN3_RED.id,
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

            FareProductId.OPUS_MONTHLY_ZHT_CITCRC.id,
            FareProductId.OPUS_MONTHLY_ZHT_CITCRC_RED.id,
            FareProductId.OPUS_MONTHLY_Z1356_CITCRC.id,
            FareProductId.OPUS_MONTHLY_Z1356_CITCRC_RED.id,
            FareProductId.OPUS_MONTHLY_Z15_CITHSL.id,
            FareProductId.OPUS_MONTHLY_Z15_CITHSL_STU.id,
            FareProductId.OPUS_MONTHLY_Z15_CITHSL_RED.id,
            FareProductId.OPUS_MONTHLY_Z16_CITHSL.id,
            FareProductId.OPUS_MONTHLY_Z16_CITHSL_STU.id,
            FareProductId.OPUS_MONTHLY_Z16_CITHSL_RED.id,
            FareProductId.OPUS_MONTHLY_Z17_CITHSL.id,
            FareProductId.OPUS_MONTHLY_Z17_CITHSL_STU.id,
            FareProductId.OPUS_MONTHLY_Z17_CITHSL_RED.id,
            FareProductId.OPUS_MONTHLY_Z18_CITHSL.id,
            FareProductId.OPUS_MONTHLY_Z18_CITHSL_STU.id,
            FareProductId.OPUS_MONTHLY_Z18_CITHSL_RED.id,
            FareProductId.OPUS_MONTHLY_Z5_CITHSL.id,
            FareProductId.OPUS_MONTHLY_Z5_CITHSL_STU.id,
            FareProductId.OPUS_MONTHLY_Z5_CITHSL_RED.id,
            FareProductId.OPUS_MONTHLY_Z56_CITHSL.id,
            FareProductId.OPUS_MONTHLY_Z56_CITHSL_STU.id,
            FareProductId.OPUS_MONTHLY_Z56_CITHSL_RED.id,
            FareProductId.OPUS_MONTHLY_Z57_CITHSL.id,
            FareProductId.OPUS_MONTHLY_Z57_CITHSL_STU.id,
            FareProductId.OPUS_MONTHLY_Z57_CITHSL_RED.id,
            FareProductId.OPUS_MONTHLY_Z58_CITHSL.id,
            FareProductId.OPUS_MONTHLY_Z58_CITHSL_STU.id,
            FareProductId.OPUS_MONTHLY_Z58_CITHSL_RED.id,
            FareProductId.OPUS_MONTHLY_Z6_CITHSL.id,
            FareProductId.OPUS_MONTHLY_Z6_CITHSL_STU.id,
            FareProductId.OPUS_MONTHLY_Z6_CITHSL_RED.id,
            FareProductId.OPUS_MONTHLY_Z67_CITHSL.id,
            FareProductId.OPUS_MONTHLY_Z67_CITHSL_STU.id,
            FareProductId.OPUS_MONTHLY_Z67_CITHSL_RED.id,
            FareProductId.OPUS_MONTHLY_Z68_CITHSL.id,
            FareProductId.OPUS_MONTHLY_Z68_CITHSL_STU.id,
            FareProductId.OPUS_MONTHLY_Z68_CITHSL_RED.id,
            FareProductId.OPUS_MONTHLY_Z7_CITHSL.id,
            FareProductId.OPUS_MONTHLY_Z7_CITHSL_STU.id,
            FareProductId.OPUS_MONTHLY_Z7_CITHSL_RED.id,
            FareProductId.OPUS_MONTHLY_Z78_CITHSL.id,
            FareProductId.OPUS_MONTHLY_Z78_CITHSL_STU.id,
            FareProductId.OPUS_MONTHLY_Z78_CITHSL_RED.id,
            FareProductId.OPUS_MONTHLY_Z8_CITHSL.id,
            FareProductId.OPUS_MONTHLY_Z8_CITHSL_STU.id,
            FareProductId.OPUS_MONTHLY_Z8_CITHSL_RED.id,
            FareProductId.OPUS_MONTHLY_CITLA.id,
            FareProductId.OPUS_MONTHLY_CITLA_RED.id,
            FareProductId.OPUS_PASSE_ATOUT_CITLA.id,
            FareProductId.OPUS_PASSE_TEMPS_CITLA.id,
            FareProductId.OPUS_PASSE_SOLEIL_CITLA.id,
            FareProductId.OPUS_MONTHLY_Z345_CITLR.id,
            FareProductId.OPUS_MONTHLY_Z345_CITLR_RED.id,
            FareProductId.OPUS_MONTHLY_Z1345_CITLR.id,
            FareProductId.OPUS_MONTHLY_Z1345_CITLR_RED.id,
            FareProductId.OPUS_MONTHLY_Z35_CITPI.id,
            FareProductId.OPUS_MONTHLY_Z35_CITPI_STU.id,
            FareProductId.OPUS_MONTHLY_Z35_CITPI_RED.id,
            FareProductId.OPUS_MONTHLY_Z37_CITPI.id,
            FareProductId.OPUS_MONTHLY_Z37_CITPI_STU.id,
            FareProductId.OPUS_MONTHLY_Z37_CITPI_RED.id,
            FareProductId.OPUS_MONTHLY_EXP_CITPI.id,
            FareProductId.OPUS_MONTHLY_EXP_CITPI_STU.id,
            FareProductId.OPUS_MONTHLY_EXP_CITPI_RED.id,
            FareProductId.OPUS_MONTHLY_Z15_CITROUS.id,
            FareProductId.OPUS_MONTHLY_Z15_CITROUS_RED.id,
            FareProductId.OPUS_MONTHLY_Z15_CITSO.id,
            FareProductId.OPUS_MONTHLY_Z15_CITSO_RED.id,
            FareProductId.OPUS_MONTHLY_Z16_CITSO.id,
            FareProductId.OPUS_MONTHLY_Z16_CITSO_RED.id,
            FareProductId.OPUS_MONTHLY_Z18_CITSO.id,
            FareProductId.OPUS_MONTHLY_Z18_CITSO_RED.id,
            FareProductId.OPUS_MONTHLY_Z25_CITSO.id,
            FareProductId.OPUS_MONTHLY_Z25_CITSO_RED.id,
            FareProductId.OPUS_MONTHLY_Z26_CITSO.id,
            FareProductId.OPUS_MONTHLY_Z26_CITSO_RED.id,
            FareProductId.OPUS_MONTHLY_Z28_CITSO.id,
            FareProductId.OPUS_MONTHLY_Z28_CITSO_RED.id,
            FareProductId.OPUS_MONTHLY_Z5_CITSO.id,
            FareProductId.OPUS_MONTHLY_Z5_CITSO_RED.id,
            FareProductId.OPUS_MONTHLY_Z56_CITSO.id,
            FareProductId.OPUS_MONTHLY_Z56_CITSO_RED.id,
            FareProductId.OPUS_MONTHLY_Z58_CITSO.id,
            FareProductId.OPUS_MONTHLY_Z58_CITSO_RED.id,
            FareProductId.OPUS_MONTHLY_Z6_CITSO.id,
            FareProductId.OPUS_MONTHLY_Z6_CITSO_RED.id,
            FareProductId.OPUS_MONTHLY_Z68_CITSO.id,
            FareProductId.OPUS_MONTHLY_Z68_CITSO_RED.id,
            FareProductId.OPUS_MONTHLY_Z8_CITSO.id,
            FareProductId.OPUS_MONTHLY_Z8_CITSO_RED.id,
            FareProductId.OPUS_MONTHLY_Z35_CITSV.id,
            FareProductId.OPUS_MONTHLY_Z35_CITSV_RED.id,
            FareProductId.OPUS_MONTHLY_Z356_CITSV.id,
            FareProductId.OPUS_MONTHLY_Z356_CITSV_RED.id,
            FareProductId.OPUS_MONTHLY_Z3567_CITSV.id,
            FareProductId.OPUS_MONTHLY_Z3567_CITSV_RED.id,
            FareProductId.OPUS_MONTHLY_Z35678_CITSV.id,
            FareProductId.OPUS_MONTHLY_Z35678_CITSV_RED.id,
            FareProductId.OPUS_MONTHLY_Z5_CITSV.id,
            FareProductId.OPUS_MONTHLY_Z5_CITSV_RED.id,
            FareProductId.OPUS_MONTHLY_Z56_CITSV.id,
            FareProductId.OPUS_MONTHLY_Z56_CITSV_RED.id,
            FareProductId.OPUS_MONTHLY_Z567_CITSV.id,
            FareProductId.OPUS_MONTHLY_Z567_CITSV_RED.id,
            FareProductId.OPUS_MONTHLY_Z5678_CITSV.id,
            FareProductId.OPUS_MONTHLY_Z5678_CITSV_RED.id,
            FareProductId.OPUS_MONTHLY_Z6_CITSV.id,
            FareProductId.OPUS_MONTHLY_Z6_CITSV_RED.id,
            FareProductId.OPUS_MONTHLY_Z67_CITSV.id,
            FareProductId.OPUS_MONTHLY_Z67_CITSV_RED.id,
            FareProductId.OPUS_MONTHLY_Z678_CITSV.id,
            FareProductId.OPUS_MONTHLY_Z678_CITSV_RED.id,
            FareProductId.OPUS_MONTHLY_Z7_CITSV.id,
            FareProductId.OPUS_MONTHLY_Z7_CITSV_RED.id,
            FareProductId.OPUS_MONTHLY_Z78_CITSV.id,
            FareProductId.OPUS_MONTHLY_Z78_CITSV_RED.id,
            FareProductId.OPUS_MONTHLY_Z8_CITSV.id,
            FareProductId.OPUS_MONTHLY_Z8_CITSV_RED.id,
            FareProductId.OPUS_MONTHLY_ACCES_CITVR.id,
            FareProductId.OPUS_MONTHLY_ACCES_CITVR_RED.id,
            FareProductId.OPUS_MONTHLY_TRAINBUS_CITVR.id,
            FareProductId.OPUS_MONTHLY_STH_CITVR.id,
            FareProductId.OPUS_MONTHLY_STH_CITVR_RED.id,
            FareProductId.OPUS_MONTHLY_Z15_CITVR.id,
            FareProductId.OPUS_MONTHLY_Z15_CITVR_RED.id,
            FareProductId.OPUS_MONTHLY_Z16_CITVR.id,
            FareProductId.OPUS_MONTHLY_Z16_CITVR_RED.id,
            FareProductId.OPUS_MONTHLY_Z17_CITVR.id,
            FareProductId.OPUS_MONTHLY_Z17_CITVR_RED.id,
            FareProductId.OPUS_MONTHLY_Z18_CITVR.id,
            FareProductId.OPUS_MONTHLY_Z18_CITVR_RED.id,
            FareProductId.OPUS_MONTHLY_Z35_CITVR.id,
            FareProductId.OPUS_MONTHLY_Z35_CITVR_RED.id,
            FareProductId.OPUS_MONTHLY_Z36_CITVR.id,
            FareProductId.OPUS_MONTHLY_Z36_CITVR_RED.id,
            FareProductId.OPUS_MONTHLY_Z37_CITVR.id,
            FareProductId.OPUS_MONTHLY_Z37_CITVR_RED.id,
            FareProductId.OPUS_MONTHLY_Z38_CITVR.id,
            FareProductId.OPUS_MONTHLY_Z38_CITVR_RED.id,
            FareProductId.OPUS_MONTHLY_Z5_CITVR.id,
            FareProductId.OPUS_MONTHLY_Z5_CITVR_RED.id,
            FareProductId.OPUS_MONTHLY_Z56_CITVR.id,
            FareProductId.OPUS_MONTHLY_Z56_CITVR_RED.id,
            FareProductId.OPUS_MONTHLY_Z57_CITVR.id,
            FareProductId.OPUS_MONTHLY_Z57_CITVR_RED.id,
            FareProductId.OPUS_MONTHLY_Z58_CITVR.id,
            FareProductId.OPUS_MONTHLY_Z58_CITVR_RED.id,
            FareProductId.OPUS_MONTHLY_Z6_CITVR.id,
            FareProductId.OPUS_MONTHLY_Z6_CITVR_RED.id,
            FareProductId.OPUS_MONTHLY_Z67_CITVR.id,
            FareProductId.OPUS_MONTHLY_Z67_CITVR_RED.id,
            FareProductId.OPUS_MONTHLY_Z68_CITVR.id,
            FareProductId.OPUS_MONTHLY_Z68_CITVR_RED.id,
            FareProductId.OPUS_MONTHLY_Z7_CITVR.id,
            FareProductId.OPUS_MONTHLY_Z7_CITVR_RED.id,
            FareProductId.OPUS_MONTHLY_Z78_CITVR.id,
            FareProductId.OPUS_MONTHLY_Z78_CITVR_RED.id,
            FareProductId.OPUS_MONTHLY_Z8_CITVR.id,
            FareProductId.OPUS_MONTHLY_Z8_CITVR_RED.id,
            FareProductId.OPUS_MONTHLY_Z27_LASSO.id,
            FareProductId.OPUS_MONTHLY_Z27_LASSO_RED.id,
            FareProductId.OPUS_MONTHLY_Z57_LASSO.id,
            FareProductId.OPUS_MONTHLY_Z57_LASSO_RED.id,
            FareProductId.OPUS_MONTHLY_PLATINE_LASSO.id,
            FareProductId.OPUS_MONTHLY_Z56_MRCLM.id,
            FareProductId.OPUS_MONTHLY_Z56_MRCLM_RED.id,
            FareProductId.OPUS_MONTHLY_Z235_MRCLM.id,
            FareProductId.OPUS_MONTHLY_Z235_MRCLM_RED.id,
            FareProductId.OPUS_MONTHLY_Z2356_MRCLM.id,
            FareProductId.OPUS_MONTHLY_Z2356_MRCLM_RED.id,
            FareProductId.OPUS_PASSE_FUTEE_Z56_MRCLM.id,
            FareProductId.OPUS_PASSE_FUTEE_Z2356_MRCLM.id,
            FareProductId.OPUS_MONTHLY_Z15_OMITSJU.id,
            FareProductId.OPUS_MONTHLY_Z15_OMITSJU_RED.id,
            FareProductId.OPUS_MONTHLY_Z35_OMITSJU.id,
            FareProductId.OPUS_MONTHLY_Z35_OMITSJU_RED.id,
            FareProductId.OPUS_MONTHLY_EVENING_AND_WEEKEND_18MINUS_RTC.id,
            FareProductId.OPUS_MONTHLY_RTC.id,
            FareProductId.OPUS_MONTHLY_RTC_STU.id,
            FareProductId.OPUS_MONTHLY_RTC_STU_2.id,
            FareProductId.OPUS_MONTHLY_RTC_ELDER.id,
            FareProductId.OPUS_MONTHLY_RTC_SCHOLAR.id,
            FareProductId.OPUS_MONTHLY_RTC_EQUIMOBILITE.id,
            FareProductId.OPUS_MONTHLY_METROPOLITAN_RTC.id,
            FareProductId.OPUS_MONTHLY_METROPOLITAN_RTC_STU.id,
            FareProductId.OPUS_MONTHLY_METROPOLITAN_RTC_ELDER.id,
            FareProductId.OPUS_MONTHLY_UNIVERSITY_RTC.id,
            FareProductId.OPUS_MONTHLY_UNIVERSITY_UL_RTC.id,
            FareProductId.OPUS_MONTHLY_RTL.id,
            FareProductId.OPUS_MONTHLY_RTL_STU.id,
            FareProductId.OPUS_MONTHLY_ACCES65_OFF_PEAK_RTL.id,
            FareProductId.OPUS_MONTHLY_ACCES65_OFF_PEAK_RTL_BROSSARD.id,
            FareProductId.OPUS_MONTHLY_ACCES65_OFF_PEAK_RTL_BOUCHERVILLE.id,
            FareProductId.OPUS_MONTHLY_ACCES65_OFF_PEAK_RTL_SAINT_LAMBERT.id,
            FareProductId.OPUS_MONTHLY_STL.id,
            FareProductId.OPUS_MONTHLY_STL_STU.id,
            FareProductId.OPUS_MONTHLY_STL_RED.id,
            FareProductId.OPUS_MONTHLY_CSSWL_STL.id,
            FareProductId.OPUS_MONTHLY_HORIZON65PLUS_STL.id,
            FareProductId.OPUS_MONTHLY_STLEVIS.id,
            FareProductId.OPUS_MONTHLY_STLEVIS_REG.id,
            FareProductId.OPUS_MONTHLY_STLEVIS_PRIVILEGE.id,
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

            FareProductId.OPUS_MONTHLY_TRAM6_HSL_CRC_RED.id,
            FareProductId.OPUS_MONTHLY_TRAM6_HSL_CRC_RED.id,
            FareProductId.OPUS_MONTHLY_TRANSIT_RTL_REM.id,
            FareProductId.OPUS_MONTHLY_TRANSIT_RTL_REM_RED.id,
            FareProductId.OPUS_MONTHLY_CRC_SJU_VR_REM.id,
            FareProductId.OPUS_MONTHLY_CRC_SJU_VR_REM_RED.id,
            FareProductId.OPUS_MONTHLY_CRC_MARIEVILLE_REM.id,
            FareProductId.OPUS_MONTHLY_CRC_MARIEVILLE_REM_RED.id,
            FareProductId.OPUS_MONTHLY_LR_ROUS_REM.id,
            FareProductId.OPUS_MONTHLY_LR_ROUS_REM_RED.id,
            FareProductId.OPUS_MONTHLY_VR_REM.id,
            FareProductId.OPUS_MONTHLY_VR_REM_RED.id,
            FareProductId.OPUS_MONTHLY_ALL_MODES_ABC_REM.id,
            FareProductId.OPUS_MONTHLY_ALL_MODES_ABC_REM_RED.id,
            FareProductId.OPUS_MONTHLY_MTLREM.id,
            FareProductId.OPUS_MONTHLY_MTLREM_STU.id,
            FareProductId.OPUS_MONTHLY_MTLREM_RED.id,
            FareProductId.OPUS_MONTHLY_TRAMREM.id,
            FareProductId.OPUS_MONTHLY_TRAMREM_STU.id,
            FareProductId.OPUS_MONTHLY_TRAMREM_RED.id,
            FareProductId.OPUS_MONTHLY_LAU_REM404.id,
            FareProductId.OPUS_MONTHLY_LAU_REM404_STU.id,
            FareProductId.OPUS_MONTHLY_TRAM1_REM.id,
            FareProductId.OPUS_MONTHLY_TRAM1_REM_STU.id,
            FareProductId.OPUS_MONTHLY_TRAM1_REM_RED.id,
            FareProductId.OPUS_MONTHLY_TRAM2_REM.id,
            FareProductId.OPUS_MONTHLY_TRAM2_REM_STU.id,
            FareProductId.OPUS_MONTHLY_TRAM2_REM_RED.id,
            FareProductId.OPUS_MONTHLY_TRAM3_REM.id,
            FareProductId.OPUS_MONTHLY_TRAM3_REM_STU.id,
            FareProductId.OPUS_MONTHLY_TRAM3_REM_RED.id,
            FareProductId.OPUS_MONTHLY_TRAM5_REM.id,
            FareProductId.OPUS_MONTHLY_TRAM5_REM_STU.id,
            FareProductId.OPUS_MONTHLY_TRAM5_REM_RED.id,
            FareProductId.OPUS_MONTHLY_TRAM6_REM.id,
            FareProductId.OPUS_MONTHLY_TRAM6_REM_STU.id,
            FareProductId.OPUS_MONTHLY_TRAM6_REM_RED.id,
            FareProductId.OPUS_MONTHLY_TRAM7_REM.id,
            FareProductId.OPUS_MONTHLY_TRAM7_REM_STU.id,
            FareProductId.OPUS_MONTHLY_TRAM7_REM_RED.id,
            FareProductId.OPUS_MONTHLY_TRAIN1_REM.id,
            FareProductId.OPUS_MONTHLY_TRAIN1_REM_STU.id,
            FareProductId.OPUS_MONTHLY_TRAIN1_REM_RED.id,
            FareProductId.OPUS_MONTHLY_TRAIN2_REM.id,
            FareProductId.OPUS_MONTHLY_TRAIN2_REM_STU.id,
            FareProductId.OPUS_MONTHLY_TRAIN2_REM_RED.id,
            FareProductId.OPUS_MONTHLY_TRAIN3_REM.id,
            FareProductId.OPUS_MONTHLY_TRAIN3_REM_STU.id,
            FareProductId.OPUS_MONTHLY_TRAIN3_REM_RED.id,
            FareProductId.OPUS_MONTHLY_TRAIN5_REM.id,
            FareProductId.OPUS_MONTHLY_TRAIN5_REM_STU.id,
            FareProductId.OPUS_MONTHLY_TRAIN5_REM_RED.id,
            FareProductId.OPUS_MONTHLY_TRAIN6_REM.id,
            FareProductId.OPUS_MONTHLY_TRAIN6_REM_STU.id,
            FareProductId.OPUS_MONTHLY_TRAIN6_REM_RED.id,
            FareProductId.OPUS_MONTHLY_TRAM1_DEUX_MONTAGNES.id,
            FareProductId.OPUS_MONTHLY_TRAM1_DEUX_MONTAGNES_STU.id,
            FareProductId.OPUS_MONTHLY_TRAM1_DEUX_MONTAGNES_RED.id,
            FareProductId.OPUS_MONTHLY_TRAM2_DEUX_MONTAGNES.id,
            FareProductId.OPUS_MONTHLY_TRAM2_DEUX_MONTAGNES_STU.id,
            FareProductId.OPUS_MONTHLY_TRAM2_DEUX_MONTAGNES_RED.id,
            FareProductId.OPUS_MONTHLY_TRAM3_DEUX_MONTAGNES.id,
            FareProductId.OPUS_MONTHLY_TRAM3_DEUX_MONTAGNES_STU.id,
            FareProductId.OPUS_MONTHLY_TRAM3_DEUX_MONTAGNES_RED.id,
            FareProductId.OPUS_MONTHLY_TRAM5_DEUX_MONTAGNES.id,
            FareProductId.OPUS_MONTHLY_TRAM5_DEUX_MONTAGNES_STU.id,
            FareProductId.OPUS_MONTHLY_TRAM5_DEUX_MONTAGNES_RED.id,
            FareProductId.OPUS_MONTHLY_TRAM6_DEUX_MONTAGNES.id,
            FareProductId.OPUS_MONTHLY_TRAM6_DEUX_MONTAGNES_STU.id,
            FareProductId.OPUS_MONTHLY_TRAM6_DEUX_MONTAGNES_RED.id,
            FareProductId.OPUS_MONTHLY_TRAM7_DEUX_MONTAGNES.id,
            FareProductId.OPUS_MONTHLY_TRAM7_DEUX_MONTAGNES_STU.id,
            FareProductId.OPUS_MONTHLY_TRAM7_DEUX_MONTAGNES_RED.id,
            FareProductId.OPUS_MONTHLY_TRAIN1_DEUX_MONTAGNES.id,
            FareProductId.OPUS_MONTHLY_TRAIN1_DEUX_MONTAGNES_STU.id,
            FareProductId.OPUS_MONTHLY_TRAIN1_DEUX_MONTAGNES_RED.id,
            FareProductId.OPUS_MONTHLY_TRAIN2_DEUX_MONTAGNES.id,
            FareProductId.OPUS_MONTHLY_TRAIN2_DEUX_MONTAGNES_STU.id,
            FareProductId.OPUS_MONTHLY_TRAIN2_DEUX_MONTAGNES_RED.id,
            FareProductId.OPUS_MONTHLY_TRAIN3_DEUX_MONTAGNES.id,
            FareProductId.OPUS_MONTHLY_TRAIN3_DEUX_MONTAGNES_STU.id,
            FareProductId.OPUS_MONTHLY_TRAIN3_DEUX_MONTAGNES_RED.id,
            FareProductId.OPUS_MONTHLY_TRAIN5_DEUX_MONTAGNES.id,
            FareProductId.OPUS_MONTHLY_TRAIN5_DEUX_MONTAGNES_STU.id,
            FareProductId.OPUS_MONTHLY_TRAIN5_DEUX_MONTAGNES_RED.id,
            FareProductId.OPUS_MONTHLY_TRAIN6_DEUX_MONTAGNES.id,
            FareProductId.OPUS_MONTHLY_TRAIN6_DEUX_MONTAGNES_STU.id,
            FareProductId.OPUS_MONTHLY_TRAIN6_DEUX_MONTAGNES_RED.id,
            FareProductId.OPUS_MONTHLY_TRAIN3_SPECIAL_LULA.id,
            FareProductId.OPUS_MONTHLY_TRAIN3_SPECIAL_LULA_RED.id,
            FareProductId.OPUS_MONTHLY_TRAIN4_SPECIAL_LULA.id,
            FareProductId.OPUS_MONTHLY_TRAIN4_SPECIAL_LULA_RED.id,
            FareProductId.OPUS_MONTHLY_TRAIN5_SPECIAL_LULA.id,
            FareProductId.OPUS_MONTHLY_TRAIN5_SPECIAL_LULA_RED.id,
            FareProductId.OPUS_MONTHLY_TRAM4_REM_DISCOUNT.id,
            FareProductId.OPUS_MONTHLY_TRAM4_REM_DISCOUNT_RED.id,
            FareProductId.OPUS_MONTHLY_ALL_MODES_A_REM_DISCOUNT.id,
            FareProductId.OPUS_MONTHLY_ALL_MODES_A_REM_DISCOUNT_RED.id,
            FareProductId.OPUS_MONTHLY_ALL_MODES_AB_REM_DISCOUNT.id,
            FareProductId.OPUS_MONTHLY_ALL_MODES_AB_REM_DISCOUNT_RED.id,
            FareProductId.OPUS_MONTHLY_ALL_MODES_ABC_REM_DISCOUNT.id,
            FareProductId.OPUS_MONTHLY_ALL_MODES_ABC_REM_DISCOUNT_RED.id,
            FareProductId.OPUS_MONTHLY_ALL_MODES_ABCD_REM_DISCOUNT.id,
            FareProductId.OPUS_MONTHLY_ALL_MODES_ABCD_REM_DISCOUNT_RED.id,

            FareProductId.OPUS_YEARLY_TRAM1.id,
            FareProductId.OPUS_YEARLY_TRAM1_STU.id,
            FareProductId.OPUS_YEARLY_TRAM1_RED.id,
            FareProductId.OPUS_YEARLY_TRAM2.id,
            FareProductId.OPUS_YEARLY_TRAM2_STU.id,
            FareProductId.OPUS_YEARLY_TRAM2_RED.id,
            FareProductId.OPUS_YEARLY_TRAM3.id,
            FareProductId.OPUS_YEARLY_TRAM3_STU.id,
            FareProductId.OPUS_YEARLY_TRAM3_RED.id,
            FareProductId.OPUS_YEARLY_TRAM4.id,
            FareProductId.OPUS_YEARLY_TRAM4_STU.id,
            FareProductId.OPUS_YEARLY_TRAM4_RED.id,
            FareProductId.OPUS_YEARLY_TRAM5.id,
            FareProductId.OPUS_YEARLY_TRAM5_STU.id,
            FareProductId.OPUS_YEARLY_TRAM5_RED.id,
            FareProductId.OPUS_YEARLY_TRAM6.id,
            FareProductId.OPUS_YEARLY_TRAM6_STU.id,
            FareProductId.OPUS_YEARLY_TRAM6_RED.id,
            FareProductId.OPUS_YEARLY_TRAM7.id,
            FareProductId.OPUS_YEARLY_TRAM7_STU.id,
            FareProductId.OPUS_YEARLY_TRAM7_RED.id,
            FareProductId.OPUS_YEARLY_TRAM8.id,
            FareProductId.OPUS_YEARLY_TRAM8_STU.id,
            FareProductId.OPUS_YEARLY_TRAM8_RED.id,

            FareProductId.OPUS_YEARLY_TRAIN1.id,
            FareProductId.OPUS_YEARLY_TRAIN1_STU.id,
            FareProductId.OPUS_YEARLY_TRAIN1_RED.id,
            FareProductId.OPUS_YEARLY_TRAIN2.id,
            FareProductId.OPUS_YEARLY_TRAIN2_STU.id,
            FareProductId.OPUS_YEARLY_TRAIN2_RED.id,
            FareProductId.OPUS_YEARLY_TRAIN3.id,
            FareProductId.OPUS_YEARLY_TRAIN3_STU.id,
            FareProductId.OPUS_YEARLY_TRAIN3_RED.id,
            FareProductId.OPUS_YEARLY_TRAIN4.id,
            FareProductId.OPUS_YEARLY_TRAIN4_STU.id,
            FareProductId.OPUS_YEARLY_TRAIN4_RED.id,
            FareProductId.OPUS_YEARLY_TRAIN5.id,
            FareProductId.OPUS_YEARLY_TRAIN5_STU.id,
            FareProductId.OPUS_YEARLY_TRAIN5_RED.id,
            FareProductId.OPUS_YEARLY_TRAIN6.id,
            FareProductId.OPUS_YEARLY_TRAIN6_STU.id,
            FareProductId.OPUS_YEARLY_TRAIN6_RED.id,
            FareProductId.OPUS_YEARLY_TRAIN7.id,
            FareProductId.OPUS_YEARLY_TRAIN7_STU.id,
            FareProductId.OPUS_YEARLY_TRAIN7_RED.id,

            FareProductId.OPUS_YEARLY_Z1356_CITCRC.id,
            FareProductId.OPUS_YEARLY_Z1356_CITCRC_RED.id,
            FareProductId.OPUS_YEARLY_Z15_CITHSL.id,
            FareProductId.OPUS_YEARLY_Z15_CITHSL_STU.id,
            FareProductId.OPUS_YEARLY_Z15_CITHSL_RED.id,
            FareProductId.OPUS_YEARLY_Z16_CITHSL.id,
            FareProductId.OPUS_YEARLY_Z16_CITHSL_STU.id,
            FareProductId.OPUS_YEARLY_Z16_CITHSL_RED.id,
            FareProductId.OPUS_YEARLY_Z17_CITHSL.id,
            FareProductId.OPUS_YEARLY_Z17_CITHSL_STU.id,
            FareProductId.OPUS_YEARLY_Z17_CITHSL_RED.id,
            FareProductId.OPUS_YEARLY_Z18_CITHSL.id,
            FareProductId.OPUS_YEARLY_Z18_CITHSL_STU.id,
            FareProductId.OPUS_YEARLY_Z18_CITHSL_RED.id,
            FareProductId.OPUS_YEARLY_CITLA.id,
            FareProductId.OPUS_YEARLY_CITLA_RED.id,
            FareProductId.OPUS_PASSE_ATOUT_CITLA.id,
            FareProductId.OPUS_PASSE_TEMPS_CITLA.id,
            FareProductId.OPUS_PASSE_SOLEIL_CITLA.id,
            FareProductId.OPUS_YEARLY_Z345_CITLR.id,
            FareProductId.OPUS_YEARLY_Z345_CITLR_RED.id,
            FareProductId.OPUS_YEARLY_Z1345_CITLR.id,
            FareProductId.OPUS_YEARLY_Z1345_CITLR_RED.id,
            FareProductId.OPUS_YEARLY_Z37_CITPI.id,
            FareProductId.OPUS_YEARLY_EXP_CITPI.id,
            FareProductId.OPUS_YEARLY_Z15_CITROUS.id,
            FareProductId.OPUS_YEARLY_Z15_CITROUS_RED.id,
            FareProductId.OPUS_YEARLY_Z15_CITSO.id,
            FareProductId.OPUS_YEARLY_Z15_CITSO_RED.id,
            FareProductId.OPUS_YEARLY_Z16_CITSO.id,
            FareProductId.OPUS_YEARLY_Z16_CITSO_RED.id,
            FareProductId.OPUS_YEARLY_Z18_CITSO.id,
            FareProductId.OPUS_YEARLY_Z18_CITSO_RED.id,
            FareProductId.OPUS_YEARLY_Z25_CITSO.id,
            FareProductId.OPUS_YEARLY_Z25_CITSO_RED.id,
            FareProductId.OPUS_YEARLY_Z26_CITSO.id,
            FareProductId.OPUS_YEARLY_Z26_CITSO_RED.id,
            FareProductId.OPUS_YEARLY_Z28_CITSO.id,
            FareProductId.OPUS_YEARLY_Z28_CITSO_RED.id,
            FareProductId.OPUS_YEARLY_Z5_CITSO.id,
            FareProductId.OPUS_YEARLY_Z5_CITSO_RED.id,
            FareProductId.OPUS_YEARLY_Z56_CITSO.id,
            FareProductId.OPUS_YEARLY_Z56_CITSO_RED.id,
            FareProductId.OPUS_YEARLY_Z58_CITSO.id,
            FareProductId.OPUS_YEARLY_Z58_CITSO_RED.id,
            FareProductId.OPUS_YEARLY_Z6_CITSO.id,
            FareProductId.OPUS_YEARLY_Z6_CITSO_RED.id,
            FareProductId.OPUS_YEARLY_Z68_CITSO.id,
            FareProductId.OPUS_YEARLY_Z68_CITSO_RED.id,
            FareProductId.OPUS_YEARLY_Z8_CITSO.id,
            FareProductId.OPUS_YEARLY_Z8_CITSO_RED.id,
            FareProductId.OPUS_YEARLY_Z35_CITSV.id,
            FareProductId.OPUS_YEARLY_Z35_CITSV_RED.id,
            FareProductId.OPUS_YEARLY_Z356_CITSV.id,
            FareProductId.OPUS_YEARLY_Z3567_CITSV.id,
            FareProductId.OPUS_YEARLY_Z35678_CITSV.id,
            FareProductId.OPUS_YEARLY_Z5_CITSV.id,
            FareProductId.OPUS_YEARLY_Z56_CITSV.id,
            FareProductId.OPUS_YEARLY_Z567_CITSV.id,
            FareProductId.OPUS_YEARLY_Z5678_CITSV.id,
            FareProductId.OPUS_YEARLY_Z6_CITSV.id,
            FareProductId.OPUS_YEARLY_Z67_CITSV.id,
            FareProductId.OPUS_YEARLY_Z678_CITSV.id,
            FareProductId.OPUS_YEARLY_Z7_CITSV.id,
            FareProductId.OPUS_YEARLY_Z78_CITSV.id,
            FareProductId.OPUS_YEARLY_Z8_CITSV.id,
            FareProductId.OPUS_YEARLY_Z15_CITVR.id,
            FareProductId.OPUS_YEARLY_Z16_CITVR.id,
            FareProductId.OPUS_YEARLY_Z35_CITVR.id,
            FareProductId.OPUS_YEARLY_Z36_CITVR.id,
            FareProductId.OPUS_YEARLY_Z27_LASSO.id,
            FareProductId.OPUS_YEARLY_Z27_LASSO_RED.id,
            FareProductId.OPUS_YEARLY_Z57_LASSO.id,
            FareProductId.OPUS_YEARLY_Z57_LASSO_RED.id,
            FareProductId.OPUS_YEARLY_PLATINE_LASSO.id,
            FareProductId.OPUS_YEARLY_Z56_MRCLM.id,
            FareProductId.OPUS_YEARLY_Z56_MRCLM_RED.id,
            FareProductId.OPUS_YEARLY_Z235_MRCLM.id,
            FareProductId.OPUS_YEARLY_Z235_MRCLM_RED.id,
            FareProductId.OPUS_YEARLY_Z2356_MRCLM.id,
            FareProductId.OPUS_YEARLY_Z2356_MRCLM_RED.id,
            FareProductId.OPUS_PASSE_FUTEE_Z56_MRCLM.id,
            FareProductId.OPUS_PASSE_FUTEE_Z2356_MRCLM.id,
            FareProductId.OPUS_YEARLY_Z15_OMITSJU.id,
            FareProductId.OPUS_YEARLY_Z15_OMITSJU_RED.id,
            FareProductId.OPUS_YEARLY_Z35_OMITSJU.id,
            FareProductId.OPUS_YEARLY_Z35_OMITSJU_RED.id,
            FareProductId.OPUS_YEARLY_RTC.id,
            FareProductId.OPUS_YEARLY_RTC_STU.id,
            FareProductId.OPUS_YEARLY_RTC_ELDER.id,
            FareProductId.OPUS_YEARLY_RTC_SCHOLAR.id,
            FareProductId.OPUS_YEARLY_METROPOLITAN_RTC.id,
            FareProductId.OPUS_YEARLY_METROPOLITAN_RTC_STU.id,
            FareProductId.OPUS_YEARLY_METROPOLITAN_RTC_ELDER.id,
            FareProductId.OPUS_YEARLY_STL.id,
            FareProductId.OPUS_YEARLY_STL_STU.id,
            FareProductId.OPUS_YEARLY_STL_RED.id,

            FareProductId.OPUS_YEARLY_BUS.id,
            FareProductId.OPUS_YEARLY_BUS_STU.id,
            FareProductId.OPUS_YEARLY_BUS_RED.id,
            FareProductId.OPUS_YEARLY_BUS_OOT.id,
            FareProductId.OPUS_YEARLY_BUS_OOT_STU.id,
            FareProductId.OPUS_YEARLY_BUS_OOT_RED.id,
            FareProductId.OPUS_YEARLY_ALL_MODES_A.id,
            FareProductId.OPUS_YEARLY_ALL_MODES_A_STU.id,
            FareProductId.OPUS_YEARLY_ALL_MODES_A_RED.id,
            FareProductId.OPUS_YEARLY_ALL_MODES_A_ELDER.id,
            FareProductId.OPUS_YEARLY_ALL_MODES_AB.id,
            FareProductId.OPUS_YEARLY_ALL_MODES_AB_STU.id,
            FareProductId.OPUS_YEARLY_ALL_MODES_AB_RED.id,
            FareProductId.OPUS_YEARLY_ALL_MODES_ABC.id,
            FareProductId.OPUS_YEARLY_ALL_MODES_ABC_STU.id,
            FareProductId.OPUS_YEARLY_ALL_MODES_ABC_RED.id,
            FareProductId.OPUS_YEARLY_ALL_MODES_ABCD.id,
            FareProductId.OPUS_YEARLY_ALL_MODES_ABCD_STU.id,
            FareProductId.OPUS_YEARLY_ALL_MODES_ABCD_RED.id,

            FareProductId.OPUS_YEARLY_TRAM6_HSL_CRC_RED.id,
            FareProductId.OPUS_YEARLY_TRAM6_HSL_CRC_RED.id,
            FareProductId.OPUS_YEARLY_TRANSIT_RTL_REM.id,
            FareProductId.OPUS_YEARLY_TRANSIT_RTL_REM_RED.id,
            FareProductId.OPUS_YEARLY_CRC_SJU_VR_REM.id,
            FareProductId.OPUS_YEARLY_CRC_SJU_VR_REM_RED.id,
            FareProductId.OPUS_YEARLY_CRC_MARIEVILLE_REM.id,
            FareProductId.OPUS_YEARLY_CRC_MARIEVILLE_REM_RED.id,
            FareProductId.OPUS_YEARLY_LR_ROUS_REM.id,
            FareProductId.OPUS_YEARLY_LR_ROUS_REM_RED.id,
            FareProductId.OPUS_YEARLY_VR_REM.id,
            FareProductId.OPUS_YEARLY_VR_REM_RED.id,
            FareProductId.OPUS_YEARLY_LAU_REM404.id,

            FareProductId.OPUS_EMPLOYEE_OPT.id,
            FareProductId.OPUS_EMPLOYEE_AMT.id,
            FareProductId.OPUS_EMPLOYEE_CIT.id -> {
                date.set(
                    validityUntilDate!!.get(Calendar.YEAR),
                    validityUntilDate!!.get(Calendar.MONTH),
                    validityUntilDate!!.get(Calendar.DATE),
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