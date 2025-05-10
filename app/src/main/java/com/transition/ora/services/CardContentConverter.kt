package com.transition.ora.services

import android.content.Context
import com.transition.ora.database.CardDatabase
import com.transition.ora.R
import com.transition.ora.database.entities.CardPropositionEntity
import com.transition.ora.enums.CardTypeVariant
import com.transition.ora.registries.FareProductRegistry
import com.transition.ora.registries.LineRegistry
import com.transition.ora.registries.OperatorRegistry
import com.transition.ora.types.FareProduct
import com.transition.ora.types.Line
import com.transition.ora.types.Operator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class CardContentConverter {
    companion object {
        fun getCardTypeVariantById(id: UInt?): CardTypeVariant? {
            return when (id) {
                392u -> CardTypeVariant.Standard
                705u -> CardTypeVariant.StandardReduced
                762u -> CardTypeVariant.AllModesAB

                else -> null
            }
        }

        fun getFareProductById(context: Context, operatorId: UInt, id: UInt): FareProduct {
            return FareProductRegistry.get(context, id) ?: run {
                val proposition: FareProduct? = lookForFareProposition(
                    context,
                    operatorId.toString(),
                    id.toString()
                )

                proposition ?: FareProduct(context.getString(R.string.unknown_fare, id), R.string.unknown_fare_info)
            }
        }

        fun getOperatorById(id: UInt): Operator {
            return OperatorRegistry.get(id) ?: Operator("Unknown (id: $id)", "#696969", R.drawable.unknown)
        }

        fun getLineById(context: Context, zoneId: UInt, operatorId: UInt, lineId: UInt): Line {
            return when (operatorId) {
                2u -> this.getSTMLineById(context, zoneId, operatorId, lineId)
                3u -> this.getRTLLineById(context, operatorId, lineId)
                4u -> this.getEXOLineById(context, zoneId, operatorId, lineId)
                5u -> this.getRTCLineById(context, operatorId, lineId)
                6u -> this.getSTLLineById(context, operatorId, lineId)
                15u -> this.getEXOLaurentidesLineById(context, operatorId, lineId)
                16u -> this.getSTLevisLineById(context, operatorId, lineId)
                18u -> this.getEXOTerrebonneMascoucheLineById(context, operatorId, lineId)
                20u -> this.getMRCJolietteLineById(context, operatorId, lineId)
                22u -> this.getREMLineById(context, zoneId, operatorId, lineId)

                else -> {
                    val proposition: Line? = lookForLineProposition(
                        context,
                        operatorId.toString(),
                        lineId.toString(),
                        R.drawable.bus
                    )

                    return proposition ?: Line("?", "Unknown (operatorID: $operatorId)", "#696969", "#ffffff", R.drawable.unknown)
                }
            }
        }

        private fun getZoneById(id: UInt): String {
            return when (id) {
                0x65u -> "A"
                0xC9u -> "A"
                0x67u -> "B"
                0xCBu -> "B"
                0x69u -> "C"
                0x6Bu -> "C"

                else -> ""
            }
        }

        private fun getSTMLineById(context: Context, zoneId: UInt, operatorId: UInt, id: UInt): Line {
            val zone = getZoneById(zoneId)
            return LineRegistry.getLineForSTM(id, zone) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.bus
                )

                return proposition ?: Line("?", "STM ($id)", "#009ee0", "#ffffff", R.drawable.bus)
            }
        }

        private fun getRTLLineById(context: Context, operatorId: UInt, id: UInt): Line {
            return LineRegistry.getLineForRTL(id) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.bus
                )

                return proposition ?: Line("?", "RTL ($id)", "#9e2536", "#ffffff", R.drawable.bus)
            }
        }

        private fun getEXOLineById(context: Context, zoneId: UInt, operatorId: UInt, id: UInt): Line {
            val zone = getZoneById(zoneId)
            return LineRegistry.getLineForEXO(id, zone) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.train
                )

                return proposition ?: Line("?", "exo ($id)", "#000000", "#ffffff", R.drawable.train)
            }
        }

        private fun getEXOLaurentidesLineById(context: Context, operatorId: UInt, id: UInt): Line {
            return LineRegistry.getLineForEXOLaurentides(id) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.bus
                )

                return proposition ?: Line("?", "exo Laurentides ($id)", "#000000", "#ffffff", R.drawable.bus)
            }
        }

        private fun getEXOTerrebonneMascoucheLineById(context: Context, operatorId: UInt, id: UInt): Line {
            return LineRegistry.getLineForEXOTerrebonneMascouche(id) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.bus
                )

                return proposition ?: Line("?", "exo Terrebonne-Mascouche ($id)", "#000000", "#ffffff", R.drawable.bus)
            }
        }

        private fun getRTCLineById(context: Context, operatorId: UInt, id: UInt): Line {
            return LineRegistry.getLineForRTC(id) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.bus
                )

                return proposition ?: Line("?", "RTC ($id)", "#003878", "#ffffff", R.drawable.bus)
            }
        }

        private fun getSTLLineById(context: Context, operatorId: UInt, id: UInt): Line {
            return LineRegistry.getLineForSTL(id) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.bus
                )

                return proposition ?: Line("?", "STL ($id)", "#151f6d", "#ffffff", R.drawable.bus)
            }
        }

        private fun getSTLevisLineById(context: Context, operatorId: UInt, id: UInt): Line {
            return LineRegistry.getLineForSTLevis(id) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.bus
                )

                return proposition ?: Line("?", "STLévis ($id)", "#0091b3", "#ffffff", R.drawable.bus)
            }
        }

        private fun getMRCJolietteLineById(context: Context, operatorId: UInt, id: UInt): Line {
            return LineRegistry.getLineForMRCJoliette(id) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.bus
                )

                return proposition ?: Line("?", "MRC Joliette ($id)", "#81a449", "#ffffff", R.drawable.bus)
            }
        }

        private fun getREMLineById(context: Context, zoneId: UInt, operatorId: UInt, id: UInt): Line {
            val zone = getZoneById(zoneId)
            return LineRegistry.getLineForREM(id, zone) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.lightmetro
                )

                return proposition ?: Line("?", "REM ($id)", "#82bf00", "#000000", R.drawable.lightmetro)
            }
        }

        private fun lookForLineProposition(
            context: Context,
            operatorId: String,
            id: String,
            icon: Int = R.drawable.unknown
        ): Line? {
            var proposition: CardPropositionEntity? = null
            try {
                val job = CoroutineScope(Dispatchers.IO).launch {
                    val localDb = CardDatabase.getInstance(context)
                    proposition = localDb.daoProposition.getStoredPropositionById(operatorId, id, "line")
                }

                runBlocking {
                    job.join()
                }

                if (proposition != null) {
                    return Line(
                        proposition!!.id,
                        proposition!!.name,
                        proposition!!.color,
                        proposition!!.textColor,
                        icon
                    )
                }
            } catch (_: Error) {}

            return null
        }

        private fun lookForFareProposition(
            context: Context,
            operatorId: String,
            id: String
        ): FareProduct? {
            var proposition: CardPropositionEntity? = null
            try {
                val job = CoroutineScope(Dispatchers.IO).launch {
                    val localDb = CardDatabase.getInstance(context)
                    proposition = localDb.daoProposition.getStoredPropositionById(operatorId, id, "fare")
                }

                runBlocking {
                    job.join()
                }

                if (proposition != null) {
                    return FareProduct(
                        proposition!!.name,
                        R.string.unknown_fare_info
                    )
                }
            } catch (_: Error) {}

            return null
        }
    }
}