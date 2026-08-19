package com.transition.ora.services

import android.content.Context
import com.transition.ora.database.CardDatabase
import com.transition.ora.R
import com.transition.ora.database.entities.CardPropositionEntity
import com.transition.ora.enums.CardType
import com.transition.ora.enums.CardTypeVariant
import com.transition.ora.registries.FareProductRegistry
import com.transition.ora.registries.HeadsignRegistry
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
        fun getCardTypeVariantById(context: Context, id: UInt): CardTypeVariant? {
            return when (id) {
                392u -> CardTypeVariant.Standard
                707u -> CardTypeVariant.Standard
                767u -> CardTypeVariant.Standard
                705u -> CardTypeVariant.StandardReduced
                757u -> CardTypeVariant.StandardSubscription

                727u -> CardTypeVariant.AllModesAB
                762u -> CardTypeVariant.AllModesAB

                763u -> CardTypeVariant.AllModesABC
                764u -> CardTypeVariant.AllModesABCD
                765u -> CardTypeVariant.BusOutOfTerritory

                1024u -> CardTypeVariant.InvalidOccasional
                in 0u..1023u -> lookForCardTypeVariantProposition(context, id.toString())
                else -> CardTypeVariant.ValidOccasional
            }
        }

        fun getFareProductById(context: Context, operatorId: UInt, id: UInt): FareProduct {
            return FareProductRegistry.get(id) ?: run {
                val proposition: FareProduct? = lookForFareProposition(
                    context,
                    operatorId.toString(),
                    id.toString()
                )

                proposition ?: FareProduct(R.string.unknown_fare, R.string.unknown_fare_info)
            }
        }

        fun getZoneById(id: UInt): String {
            return when (id) {
                0x65u -> "A"
                0x66u -> "A"
                0x67u -> "B"
                0x68u -> "C"
                0x69u -> "C"
                0x6Au -> "C"
                0x6Bu -> "C"

                0xC9u -> "A"
                0xCAu -> "A"
                0xCBu -> "B"
                0xCCu -> "C"
                0xCDu -> "C"
                0xCEu -> "C"
                0xCFu -> "C"

                in 0x00u..0xFFu -> "? ($id)"
                else -> id.toString()
            }
        }

        fun getOperatorById(id: UInt): Operator {
            return OperatorRegistry.get(id) ?: Operator("Unknown (id: $id)", "#696969", R.drawable.unknown)
        }

        fun getLineById(context: Context, zoneId: UInt, operatorId: UInt, lineId: UInt): Line {
            return when (operatorId) {
                2u -> this.getSTMLineById(context, zoneId, operatorId, lineId)
                3u -> this.getRTLLineById(context, operatorId, lineId)
                4u -> this.getExoLineById(context, zoneId, operatorId, lineId)
                5u -> this.getRTCLineById(context, operatorId, lineId)
                6u -> this.getSTLLineById(context, operatorId, lineId)
                7u -> this.getExoSorelVarennesLineById(context, operatorId, lineId)
                8u -> this.getExoSainteJulieLineById(context, operatorId, lineId)
                9u -> this.getExoValleeRichelieuLineById(context, operatorId, lineId)
                10u -> this.getExoChamblyRichelieuCarignanLineById(context, operatorId, lineId)
                11u -> this.getExoLeRichelainLineById(context, operatorId, lineId)
                12u -> this.getExoRoussillonLineById(context, operatorId, lineId)
                13u -> this.getExoHautSaintLaurentLineById(context, operatorId, lineId)
                14u -> this.getExoSudOuestLineById(context, operatorId, lineId)
                15u -> this.getExoLaurentidesLineById(context, operatorId, lineId)
                16u -> this.getSTLevisLineById(context, operatorId, lineId)
                17u -> this.getExoPresquIleLineById(context, operatorId, lineId)
                18u -> this.getExoTerrebonneMascoucheLineById(context, operatorId, lineId)
                19u -> this.getExoLassomptionLineById(context, operatorId, lineId)
                20u -> this.getMRCJolietteLineById(context, operatorId, lineId)
                21u -> this.getSTQLineById(context, operatorId, lineId)
                22u -> this.getREMLineById(context, zoneId, operatorId, lineId)

                else -> {
                    val proposition: Line? = lookForLineProposition(
                        context,
                        operatorId.toString(),
                        lineId.toString(),
                        R.drawable.bus
                    )

                    proposition ?: Line("?", "Unknown (operatorID: $operatorId)", "#696969", "#ffffff", R.drawable.unknown)
                }
            }
        }

        fun getHeadsignById(operatorId: UInt, lineId: UInt, directionId: UInt): String {
            return when (operatorId) {
                2u -> this.getSTMHeadsignById(lineId, directionId)
                3u -> this.getRTLHeadsignById(lineId, directionId)
                5u -> this.getRTCHeadsignById(lineId, directionId)
                6u -> this.getSTLHeadsignById(lineId, directionId)
                7u -> this.getExoSorelVarennesHeadsignById(lineId, directionId)
                8u -> this.getExoSainteJulieHeadsignById(lineId, directionId)
                9u -> this.getExoValleeRichelieuHeadsignById(lineId, directionId)
                10u -> this.getExoChamblyRichelieuCarignanHeadsignById(lineId, directionId)
                11u -> this.getExoLeRichelainHeadsignById(lineId, directionId)
                14u -> this.getExoSudOuestHeadsignById(lineId, directionId)
                15u -> this.getExoLaurentidesHeadsignById(lineId, directionId)
                16u -> this.getSTLevisHeadsignById(lineId, directionId)
                17u -> this.getExoPresquIleHeadsignById(lineId, directionId)
                18u -> this.getExoTerrebonneMascoucheHeadsignById(lineId, directionId)
                19u -> this.getExoLassomptionHeadsignById(lineId, directionId)
                20u -> this.getMRCJolietteHeadsignById(lineId, directionId)
                21u -> this.getSTQHeadsignById(lineId, directionId)

                else -> "? ($directionId)"
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

        private fun getSTMHeadsignById(lineId: UInt, directionId: UInt): String {
            if (lineId == 3u) {
                return "Berri-UQAM"
            }

            return HeadsignRegistry.getHeadsignForSTM(directionId) ?: run {
                return "? ($directionId)"
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

                return proposition ?: Line("?", "RTL ($id)", "#ce0037", "#ffe9d1", R.drawable.bus)
            }
        }

        private fun getExoLineById(context: Context, zoneId: UInt, operatorId: UInt, id: UInt): Line {
            val zone = getZoneById(zoneId)
            return LineRegistry.getLineForExo(id, zone) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.train
                )

                return proposition ?: Line("?", "exo ($id)", "#1f1f1f", "#ffffff", R.drawable.train)
            }
        }

        private fun getRTLHeadsignById(lineId: UInt, directionId: UInt): String {
            return HeadsignRegistry.getHeadsignForRTL(lineId, directionId) ?: run {
                return "? ($directionId)"
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

        private fun getRTCHeadsignById(lineId: UInt, directionId: UInt): String {
            return HeadsignRegistry.getHeadsignForRTC(lineId, directionId) ?: run {
                return "? ($directionId)"
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

        private fun getSTLHeadsignById(lineId: UInt, directionId: UInt): String {
            return HeadsignRegistry.getHeadsignForSTL(lineId, directionId) ?: run {
                return "? ($directionId)"
            }
        }

        private fun getExoSorelVarennesLineById(context: Context, operatorId: UInt, id: UInt): Line {
            return LineRegistry.getLineForExoSorelVarennes(id) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.bus
                )

                return proposition ?: Line("?", "exo Sorel-Varennes ($id)", "#1f1f1f", "#ffffff", R.drawable.bus)
            }
        }

        private fun getExoSorelVarennesHeadsignById(lineId: UInt, directionId: UInt): String {
            return HeadsignRegistry.getHeadsignForExoSorelVarennes(lineId, directionId) ?: run {
                return "? ($directionId)"
            }
        }

        private fun getExoSainteJulieLineById(context: Context, operatorId: UInt, id: UInt): Line {
            return LineRegistry.getLineForExoSainteJulie(id) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.bus
                )

                return proposition ?: Line("?", "exo Sainte-Julie ($id)", "#1f1f1f", "#ffffff", R.drawable.bus)
            }
        }

        private fun getExoSainteJulieHeadsignById(lineId: UInt, directionId: UInt): String {
            return HeadsignRegistry.getHeadsignForExoSainteJulie(lineId, directionId) ?: run {
                return "? ($directionId)"
            }
        }

        private fun getExoValleeRichelieuLineById(context: Context, operatorId: UInt, id: UInt): Line {
            return LineRegistry.getLineForExoValleeRichelieu(id) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.bus
                )

                return proposition ?: Line("?", "exo Vallée du Richelieu ($id)", "#1f1f1f", "#ffffff", R.drawable.bus)
            }
        }

        private fun getExoValleeRichelieuHeadsignById(lineId: UInt, directionId: UInt): String {
            return HeadsignRegistry.getHeadsignForExoValleeRichelieu(lineId, directionId) ?: run {
                return "? ($directionId)"
            }
        }

        private fun getExoChamblyRichelieuCarignanLineById(context: Context, operatorId: UInt, id: UInt): Line {
            return LineRegistry.getLineForExoChamblyRichelieuCarignan(id) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.bus
                )

                return proposition ?: Line("?", "exo Chambly-Richelieu-Carignan ($id)", "#1f1f1f", "#ffffff", R.drawable.bus)
            }
        }

        private fun getExoChamblyRichelieuCarignanHeadsignById(lineId: UInt, directionId: UInt): String {
            return HeadsignRegistry.getHeadsignForExoChamblyRichelieuCarignan(lineId, directionId) ?: run {
                return "? ($directionId)"
            }
        }

        private fun getExoLeRichelainLineById(context: Context, operatorId: UInt, id: UInt): Line {
            return LineRegistry.getLineForExoLeRichelain(id) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.bus
                )

                return proposition ?: Line("?", "exo Le Richelain-Rousillon ($id)", "#1f1f1f", "#ffffff", R.drawable.bus)
            }
        }

        private fun getExoLeRichelainHeadsignById(lineId: UInt, directionId: UInt): String {
            return HeadsignRegistry.getHeadsignForExoLeRichelain(lineId, directionId) ?: run {
                return "? ($directionId)"
            }
        }

        private fun getExoRoussillonLineById(context: Context, operatorId: UInt, id: UInt): Line {
            return LineRegistry.getLineForExoRoussillon(id) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.bus
                )

                return proposition ?: Line("?", "exo Rousillon ($id)", "#1f1f1f", "#ffffff", R.drawable.bus)
            }
        }
 
        private fun getExoSudOuestLineById(context: Context, operatorId: UInt, id: UInt): Line {
            return LineRegistry.getLineForExoSudOuest(id) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.bus
                )

                return proposition ?: Line("?", "exo Sud-Ouest ($id)", "#1f1f1f", "#ffffff", R.drawable.bus)
            }
        }

        private fun getExoSudOuestHeadsignById(lineId: UInt, directionId: UInt): String {
            return HeadsignRegistry.getHeadsignForExoSudOuest(lineId, directionId) ?: run {
                return "? ($directionId)"
            }
        }

        private fun getExoLaurentidesLineById(context: Context, operatorId: UInt, id: UInt): Line {
            return LineRegistry.getLineForExoLaurentides(id) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.bus
                )

                return proposition ?: Line("?", "exo Laurentides ($id)", "#1f1f1f", "#ffffff", R.drawable.bus)
            }
        }

        private fun getExoLaurentidesHeadsignById(lineId: UInt, directionId: UInt): String {
            return HeadsignRegistry.getHeadsignForExoLaurentides(lineId, directionId) ?: run {
                return "? ($directionId)"
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

        private fun getSTLevisHeadsignById(lineId: UInt, directionId: UInt): String {
            return HeadsignRegistry.getHeadsignForSTLevis(lineId, directionId) ?: run {
                return "? ($directionId)"
            }
        }

        private fun getExoPresquIleLineById(context: Context, operatorId: UInt, id: UInt): Line {
            return LineRegistry.getLineForExoPresquIle(id) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.bus
                )

                return proposition ?: Line("?", "exo La Presqu'île ($id)", "#1f1f1f", "#ffffff", R.drawable.bus)
            }
        }

        private fun getExoPresquIleHeadsignById(lineId: UInt, directionId: UInt): String {
            return HeadsignRegistry.getHeadsignForExoPresquIle(lineId, directionId) ?: run {
                return "? ($directionId)"
            }
        }

        private fun getExoTerrebonneMascoucheLineById(context: Context, operatorId: UInt, id: UInt): Line {
            return LineRegistry.getLineForExoTerrebonneMascouche(id) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.bus
                )

                return proposition ?: Line("?", "exo Terrebonne-Mascouche ($id)", "#1f1f1f", "#ffffff", R.drawable.bus)
            }
        }

        private fun getExoTerrebonneMascoucheHeadsignById(lineId: UInt, directionId: UInt): String {
            return HeadsignRegistry.getHeadsignForExoTerrebonneMascouche(lineId, directionId) ?: run {
                return "? ($directionId)"
            }
        }

        private fun getExoLassomptionLineById(context: Context, operatorId: UInt, id: UInt): Line {
            return LineRegistry.getLineForExoLassomption(id) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.bus
                )

                return proposition ?: Line("?", "exo L'Assomption ($id)", "#1f1f1f", "#ffffff", R.drawable.bus)
            }
        }

        private fun getExoLassomptionHeadsignById(lineId: UInt, directionId: UInt): String {
            return HeadsignRegistry.getHeadsignForExoLassomption(lineId, directionId) ?: run {
                return "? ($directionId)"
            }
        }

        private fun getExoHautSaintLaurentLineById(context: Context, operatorId: UInt, id: UInt): Line {
            return LineRegistry.getLineForExoHautSaintLaurent(id) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.bus
                )

                return proposition ?: Line("?", "exo Haut-Saint-Laurent ($id)", "#1f1f1f", "#ffffff", R.drawable.bus)
            }
        }

        private fun getSTQLineById(context: Context, operatorId: UInt, id: UInt): Line {
            return LineRegistry.getLineForSTQ(id) ?: run {
                val proposition: Line? = lookForLineProposition(
                    context,
                    operatorId.toString(),
                    id.toString(),
                    R.drawable.ferry
                )

                return proposition ?: Line("?", "STQ ($id)", "#002e46", "#ffffff", R.drawable.ferry)
            }
        }

        private fun getSTQHeadsignById(lineId: UInt, directionId: UInt): String {
            return HeadsignRegistry.getHeadsignForSTQ(lineId, directionId) ?: run {
                return "? ($directionId)"
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

        private fun getMRCJolietteHeadsignById(lineId: UInt, directionId: UInt): String {
            return HeadsignRegistry.getHeadsignForMRCJoliette(lineId, directionId) ?: run {
                return "? ($directionId)"
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
                    val fareProduct = FareProduct(
                        R.string.unknown_fare,
                        R.string.unknown_fare_info
                    )
                    fareProduct.setName(proposition!!.name)

                    return fareProduct
                }
            } catch (_: Error) {}

            return null
        }

        private fun lookForCardTypeVariantProposition(
            context: Context,
            id: String
        ): CardTypeVariant? {
            var proposition: CardPropositionEntity? = null
            try {
                val job = CoroutineScope(Dispatchers.IO).launch {
                    val localDb = CardDatabase.getInstance(context)
                    proposition = localDb.daoProposition.getStoredPropositionById(CardType.Opus.name, id, "type")
                }

                runBlocking {
                    job.join()
                }

                if (proposition != null) {
                    return when (proposition!!.name) {
                        context.getString(R.string.standard_card) -> CardTypeVariant.Standard
                        context.getString(R.string.all_modes_AB_card) -> CardTypeVariant.AllModesAB
                        context.getString(R.string.all_modes_ABC_card) -> CardTypeVariant.AllModesABC
                        context.getString(R.string.all_modes_ABCD_card) -> CardTypeVariant.AllModesABCD
                        context.getString(R.string.bus_out_territory_card) -> CardTypeVariant.BusOutOfTerritory
                        else -> null
                    }
                }
            } catch (_: Error) {}

            return null
        }
    }
}