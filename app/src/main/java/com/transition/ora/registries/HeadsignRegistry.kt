package com.transition.ora.registries

import kotlin.String


object HeadsignRegistry {
    private val directionMapForSTM: Map<UInt, String> = mapOf(
        3u to "Nord",
        4u to "Sud",
        5u to "Est",
        6u to "Ouest",
    )

    private val headsignMapForSTL: Map<UInt, Map<UInt, String>> = mapOf(
        1u to mapOf(1u to "Métro Montmorency", 2u to "Métro Henri-Bourassa"),
        2u to mapOf(1u to "Pont-Viau", 2u to "Métro Cartier"),
        302u to mapOf(1u to "Métro Montmorency", 2u to "Laval-des-Rapides / Armand-Frappier"),
        3u to mapOf(1u to "Auteuil", 2u to "Métro Cartier"),
        4u to mapOf(1u to "Métro Cartier", 2u to "Chomedey"),
        264u to mapOf(1u to "Saint-François", 2u to "Métro Cartier"),
        5u to mapOf(1u to "Métro Cartier", 2u to "Sainte-Dorothée"),
        6u to mapOf(1u to "Saint-François", 2u to "Métro Cartier"),
        7u to mapOf(1u to "Métro Montmorency", 2u to "Station Sainte-Dorothée"),
        8u to mapOf(1u to "Gare Vimont", 2u to "Métro Cartier"),
        9u to mapOf(1u to "Saint-Vincent-de-Paul", 2u to "Métro Cartier"),
        10u to mapOf(1u to "Auteuil", 2u to "Métro Henri-Bourassa"),
        11u to mapOf(1u to "Métro Montmorency", 2u to "Métro Cartier"),
        265u to mapOf(1u to "Métro Montmorency", 2u to "Chomedey"),
        12u to mapOf(1u to "Sainte-Rose", 2u to "Métro Cartier"),
        13u to mapOf(1u to "Auteuil", 2u to "Terminus Le Carrefour"),
        14u to mapOf(1u to "Métro Montmorency", 2u to "Chomedey"),
        15u to mapOf(1u to "Auteuil", 2u to "Métro Cartier"),
        16u to mapOf(1u to "Saint-François", 2u to "Terminus Le Carrefour"),
        129u to mapOf(1u to "Auteuil", 2u to "Métro Cartier"),
        130u to mapOf(1u to "Auteuil", 2u to "Métro Montmorency"),
        131u to mapOf(1u to "Laval-Ouest", 2u to "Métro Montmorency"),
        132u to mapOf(1u to "Gare Vimont", 2u to "Métro Cartier"),
        133u to mapOf(1u to "Saint-Vincent-de-Paul", 2u to "Terminus Le Carrefour"),
        134u to mapOf(1u to "Saint-François", 2u to "Métro Henri-Bourassa"),
        135u to mapOf(1u to "Laval-Ouest", 2u to "Métro Henri-Bourassa"),
        136u to mapOf(1u to "Métro Montmorency", 2u to "Sainte-Dorothée"),
        137u to mapOf(1u to "Saint-Vincent-de-Paul", 2u to "Métro Cartier"),
        138u to mapOf(1u to "Métro Cartier", 2u to "Chomedey"),
        139u to mapOf(1u to "Fabreville", 2u to "Métro Montmorency"),
        140u to mapOf(1u to "Gare Sainte-Rose", 2u to "Métro Cartier"),
        141u to mapOf(1u to "Gare Sainte-Rose", 2u to "Métro Montmorency"),
        142u to mapOf(1u to "Terminus Le Carrefour", 2u to "Sainte-Dorothée"),
        143u to mapOf(1u to "Métro Cartier", 2u to "Métro Montmorency"),
        144u to mapOf(1u to "Fabreville", 2u to "Métro Cartier"),
        145u to mapOf(1u to "Saint-François", 2u to "Métro Cartier"),
        146u to mapOf(1u to "Métro Montmorency", 2u to "Station Sainte-Dorothée"),
        147u to mapOf(1u to "Métro Côte-Vertu", 2u to "Sainte-Dorothée"),
        148u to mapOf(1u to "Sainte-Rose", 2u to "Métro Côte-Vertu"),
        308u to mapOf(1u to "Station Sainte-Dorothée", 2u to "Fabreville"),
        190u to mapOf(1u to "Saint-Vincent-de-Paul", 2u to "Métro Cartier"),
        309u to mapOf(1u to "Sainte-Dorothée", 2u to "Métro Montmorency"),
        149u to mapOf(1u to "Saint-Vincent-de-Paul", 2u to "Métro Cartier"),
        310u to mapOf(1u to "Sainte-Dorothée", 2u to "Station Bois-Franc"),
        311u to mapOf(1u to "Sainte-Rose", 2u to "Station Bois-Franc"),
        150u to mapOf(1u to "Saint-François", 2u to "Métro Henri-Bourassa"),
        312u to mapOf(1u to "Laval-Ouest", 2u to "Station Bois-Franc"),
        64u to mapOf(1u to "Chomedey", 2u to "Métro Côte-Vertu"),
        27u to mapOf(1u to "Duvernay", 2u to "Métro Henri-Bourassa"),
        76u to mapOf(1u to "Vimont", 2u to "Métro Henri-Bourassa"),
        151u to mapOf(1u to "Sainte-Rose", 2u to "Gare Sainte-Dorothée"),
        152u to mapOf(1u to "Sainte-Dorothée", 2u to "Gare Sainte-Dorothée"),
        299u to mapOf(1u to "Station Sainte-Dorothée", 2u to "Métro Côte-Vertu"),
        290u to mapOf(1u to "Station Sainte-Dorothée", 2u to "Montréal"),
        295u to mapOf(1u to "Métro Côte-Vertu", 2u to "Station Sainte-Dorothée"),
        153u to mapOf(1u to "Saint-François", 2u to "Métro Cartier"),
        154u to mapOf(1u to "Terminus Le Carrefour", 2u to "Métro Côte-Vertu"),
        155u to mapOf(1u to "Station Sainte-Dorothée", 2u to ""),
        193u to mapOf(1u to "Saint-François", 2u to "Métro Radisson"),
        270u to mapOf(1u to "Saint-François", 2u to "Métro Montmorency"),
        304u to mapOf(1u to "Horizon-Jeunesse", 2u to "Métro Cartier"),
        191u to mapOf(1u to "Collège Laval", 2u to "Auteuil"),
    )

    private val headsignMapForExoLaurentides: Map<UInt, Map<UInt, String>> = mapOf(
        153u to mapOf(1u to "Carrefour du Nord", 2u to "SmartCentre Saint-Jérôme"),
        158u to mapOf(1u to "Carrefour du Nord", 2u to "Terminus Saint-Jérôme"),
        183u to mapOf(1u to "Fontainebleau - Chambéry", 2u to "Terminus Sainte-Thérèse"),
        184u to mapOf(1u to "Rosemère", 2u to "Gare Rosemère"),
        185u to mapOf(1u to "Lorraine - Terrebonne", 2u to "Terminus Sainte-Thérèse"),
        186u to mapOf(1u to "Sainte-Anne-des-Plaines", 2u to "Terminus Sainte-Thérèse"),
        189u to mapOf(1u to "Lorraine - Terrebonne", 2u to "Gare Rosemère"),
        198u to mapOf(1u to "Sainte-Thérèse", 2u to "Deux-Montagnes"),
        199u to mapOf(1u to "Station Deux-Montagnes", 2u to "Métro Montmorency"),
        200u to mapOf(1u to "Sainte-Anne-des-Plaines", 2u to "Métro Cartier"),
        201u to mapOf(1u to "Sainte-Thérèse", 2u to "Station Deux-Montagnes"),
        202u to mapOf(1u to "Sainte-Anne-des-Plaines", 2u to "Métro Cartier"),
        203u to mapOf(1u to "Sainte-Thérèse", 2u to "Station Deux-Montagnes"),
        204u to mapOf(1u to "Terminus Terrebonne", 2u to "Terminus Sainte-Thérèse"),
        205u to mapOf(1u to "Boisbriand", 2u to "Métro Montmorency"),
        206u to mapOf(1u to "Station Deux-Montagnes", 2u to "Métro Montmorency"),
        171u to mapOf(1u to "Gare Saint-Jérôme", 2u to "Métro Montmorency"),
        142u to mapOf(1u to "Saint-Placide", 2u to "Station Deux-Montagnes"),
    )

    fun getHeadsignForSTM(directionId: UInt): String? = directionMapForSTM[directionId]
    fun getHeadsignForRTL(lineId: UInt, directionId: UInt): String? = null
    fun getHeadsignForRTC(lineId: UInt, directionId: UInt): String? = null
    fun getHeadsignForSTL(lineId: UInt, directionId: UInt): String? = headsignMapForSTL[lineId]?.get(directionId)
    fun getHeadsignForExoSorelVarennes(lineId: UInt, directionId: UInt): String? = null
    fun getHeadsignForExoSainteJulie(lineId: UInt, directionId: UInt): String? = null
    fun getHeadsignForExoValleeRichelieu(lineId: UInt, directionId: UInt): String? = null
    fun getHeadsignForExoChamblyRichelieuCarignan(lineId: UInt, directionId: UInt): String? = null
    fun getHeadsignForExoLeRichelain(lineId: UInt, directionId: UInt): String? = null
    fun getHeadsignForExoSudOuest(lineId: UInt, directionId: UInt): String? = null
    fun getHeadsignForExoLaurentides(lineId: UInt, directionId: UInt): String? = headsignMapForExoLaurentides[lineId]?.get(directionId)
    fun getHeadsignForSTLevis(lineId: UInt, directionId: UInt): String? = null
    fun getHeadsignForExoPresquIle(lineId: UInt, directionId: UInt): String? = null
    fun getHeadsignForExoTerrebonneMascouche(lineId: UInt, directionId: UInt): String? = null
    fun getHeadsignForExoLassomption(lineId: UInt, directionId: UInt): String? = null
    fun getHeadsignForMRCJoliette(lineId: UInt, directionId: UInt): String? = null
    fun getHeadsignForSTQ(lineId: UInt, directionId: UInt): String? = null
}