package com.transition.ora.registries

import kotlin.String


object HeadsignRegistry {
    private val directionMapForSTM: Map<UInt, String> = mapOf(
        3u to "Nord",
        4u to "Sud",
        5u to "Est",
        6u to "Ouest",
    )

    private val headsignMapForRTL: Map<UInt, Map<UInt, String>> = mapOf(
        5u to mapOf(1u to "Sect. M St-Hubert", 2u to "Terminus Panama"),
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

    private val headsignMapForExoSorelVarennes: Map<UInt, Map<UInt, String>> = mapOf(
        18u to mapOf(1u to "Termins Sainte-Julie"),
        20u to mapOf(1u to "Varennes", 2u to "Terminus Radisson"),
        1u to mapOf(1u to "Sorel", 2u to "Longueuil"),
        22u to mapOf(1u to "Contrecoeur", 2u to "Longueuil"),
        13u to mapOf(1u to "Contrecoeur", 2u to "Longueuil"),
        15u to mapOf(1u to "Contrecoeur", 2u to "Longueuil"),
        3u to mapOf(1u to "Varennes", 2u to "Longueuil"),
        4u to mapOf(1u to "Varennes", 2u to "Longueuil"),
        5u to mapOf(1u to "Varennes", 2u to "Longueuil"),
        6u to mapOf(1u to "Varennes", 2u to "Longueuil"),
        9u to mapOf(1u to "Saint-Amable", 2u to "Longueuil"),
        10u to mapOf(1u to "Saint-Amable", 2u to "Longueuil"),
    )

    private val headsignMapForExoSainteJulie: Map<UInt, Map<UInt, String>> = mapOf(
        14u to mapOf(1u to "Sainte-Julie", 2u to "Longueuil"),
        15u to mapOf(1u to "Sainte-Julie", 2u to "Longueuil"),
        16u to mapOf(1u to "Sainte-Julie", 2u to "Longueuil"),
        4u to mapOf(1u to "Sainte-Julie", 2u to "Longueuil"),
        9u to mapOf(1u to "Sainte-Julie", 2u to "Brossard"),
    )

    private val headsignMapForExoValleeRichelieu: Map<UInt, Map<UInt, String>> = mapOf(
        5u to mapOf(1u to "Beloeil", 2u to "Gare McMasterville"),
        7u to mapOf(1u to "Gare Mont-St-Hilaire", 2u to "La Pommeraie"),
        9u to mapOf(1u to "Otterburn Park", 2u to "Gare McMasterville"),
        10u to mapOf(1u to "Bella-Vista / Montée Robert", 2u to "Gare St-Basile-le-Grand"),
        11u to mapOf(1u to "Mont-Saint-Hilaire", 2u to "Saint-Hyacinthe"),
        16u to mapOf(1u to "Champagne / des Trinitaires", 2u to "Gare St-Basile-le-Grand"),
        12u to mapOf(1u to "Saint-Hyacinthe", 2u to "Longueuil"),
        15u to mapOf(1u to "Mont-Saint-Hilaire", 2u to "Longueuil"),
        13u to mapOf(1u to "Mont-Saint-Hilaire", 2u to "Terminus Brossard"),
    )

    private val headsignMapForExoChamblyRichelieuCarignan: Map<UInt, Map<UInt, String>> = mapOf(
        49u to mapOf(1u to "Terminus Chambly", 2u to "Terminus Brossard"),
        50u to mapOf(1u to "Chambly", 2u to "Terminus Brossard"),
        51u to mapOf(1u to "Faubourg Carignan", 2u to "Terminus Brossard"),
        52u to mapOf(1u to "Île aux Lièvres", 2u to "Terminus Brossard"),
        53u to mapOf(1u to "Chambly", 2u to "Terminus Brossard"),
        54u to mapOf(1u to "Marieville", 2u to "Terminus Brossard"),
        55u to mapOf(1u to "Chambly", 2u to "Terminus Brossard"),
        56u to mapOf(1u to "Chambly", 2u to "Terminus Brossard"),
        57u to mapOf(1u to "Terminus Chambly", 2u to "Terminus Longueuil"),
        58u to mapOf(1u to "Terminus Chambly", 2u to "Longueuil"),
        59u to mapOf(1u to "Chambly - Carignan", 2u to "Saint-Jean-sur-Richelieu"),
    )

    private val headsignMapForExoLeRichelain: Map<UInt, Map<UInt, String>> = mapOf(
        43u to mapOf(1u to "Terminus La Prairie"),
        44u to mapOf(1u to "Terminus La Prairie"),
        45u to mapOf(1u to "Terminus Montcalm-Candiac"),
        46u to mapOf(1u to "Terminus Montcalm-Candiac"),
        47u to mapOf(1u to "Terminus La Prairie", 2u to "Symbiocité"),
        49u to mapOf(1u to "Terminus Montcalm-Candiac", 2u to "Gare Candiac"),
        50u to mapOf(1u to "Terminus Montcalm-Candiac", 2u to "Gare Candiac"),
        51u to mapOf(1u to "Boulevard Marie-Victorin", 2u to "Gare Sainte-Catherine"),
        52u to mapOf(1u to "Terminus Georges-Gagné"),
        53u to mapOf(1u to "Terminus Georges-Gagné"),
        54u to mapOf(1u to "Terminus Georges-Gagné"),
        55u to mapOf(1u to "Terminus Georges-Gagné"),
        56u to mapOf(1u to "Terminus Georges-Gagné"),
        57u to mapOf(1u to "Terminus Georges-Gagné"),
        63u to mapOf(1u to "Terminus Brossard", 2u to "La Prairie"),
        64u to mapOf(1u to "Terminus Brossard", 2u to "La Prairie"),
        65u to mapOf(1u to "Terminus Brossard", 2u to "La Prairie"),
        66u to mapOf(1u to "Terminus Panama", 2u to "Vieux-La Prairie"),
        67u to mapOf(1u to "Terminus Brossard", 2u to "Candiac"),
        68u to mapOf(1u to "Terminus Brossard", 2u to "Candiac"),
        69u to mapOf(1u to "Terminus Brossard", 2u to "Candiac"),
        70u to mapOf(1u to "Terminus Brossard", 2u to "Saint-Philippe"),
        71u to mapOf(1u to "Terminus Panama", 2u to "Terminus La Prairie"),
        72u to mapOf(1u to "Terminus Panama", 2u to "Terminus Montcalm-Candiac"),
        73u to mapOf(1u to "Longueuil", 2u to "Terminus La Prairie"),
        74u to mapOf(1u to "Longueuil", 2u to "Terminus Montcalm-Candiac"),
        75u to mapOf(1u to "Terminus Brossard", 2u to "Saint-Constant"),
        76u to mapOf(1u to "Terminus Panama", 2u to "Sainte-Catherine"),
        77u to mapOf(1u to "Terminus Brossard", 2u to "Terminus Georges-Gagné"),
        78u to mapOf(1u to "Longueuil", 2u to "Terminus Georges-Gagné"),
        79u to mapOf(1u to "Terminus Georges-Gagné"),
        80u to mapOf(1u to "Terminus Panama", 2u to "Terminus Georges-Gagné"),
        81u to mapOf(1u to "Longueuil", 2u to "Terminus Georges-Gagné"),
    )

    private val headsignMapForExoSudOuest: Map<UInt, Map<UInt, String>> = mapOf(
        1u to mapOf(1u to "Terminus Angrignon", 2u to "Terminus Salaberry-de-Valleyfield"),
        2u to mapOf(1u to "Maple Grove", 2u to "Melochville"),
        3u to mapOf(1u to "Terminus Angrignon", 2u to "Terminus Châteauguay"),
        4u to mapOf(1u to "Terminus Châteauguay"),
        5u to mapOf(1u to "Terminus Angrignon", 2u to "Châteauguay"),
        6u to mapOf(1u to "Terminus Châteauguay", 2u to "Maple - Deguire - D'Youville"),
        24u to mapOf(1u to "Terminus Angrignon", 2u to "Châteauguay"),
        7u to mapOf(1u to "Terminus Angrignon"),
        8u to mapOf(1u to "Terminus Châteauguay", 2u to "Higgins / Salaberry"),
        9u to mapOf(1u to "Terminus Centre-Ville", 2u to "Châteauguay / Beauharnois"),
        10u to mapOf(1u to "Terminus Angrignon"),
        11u to mapOf(1u to "Terminus Angrignon"),
        12u to mapOf(1u to "Terminus Châteauguay"),
    )

    private val headsignMapForExoLaurentides: Map<UInt, Map<UInt, String>> = mapOf(
        172u to mapOf(1u to "Station Deux-Montagnes"),
        173u to mapOf(1u to "Station Deux-Montagnes"),
        174u to mapOf(1u to "Station Deux-Montagnes"),
        175u to mapOf(1u to "Station Deux-Montagnes"),
        176u to mapOf(1u to "Station Deux-Montagnes"),
        177u to mapOf(1u to "Station Deux-Montagnes"),
        178u to mapOf(1u to "Station Deux-Montagnes"),
        153u to mapOf(1u to "Carrefour du Nord", 2u to "SmartCentre Saint-Jérôme"),
        154u to mapOf(1u to "Terminus Saint-Jérôme"),
        155u to mapOf(1u to "Terminus Saint-Jérôme"),
        156u to mapOf(1u to "Terminus Saint-Jérôme"),
        157u to mapOf(1u to "Terminus Saint-Jérôme"),
        158u to mapOf(1u to "Carrefour du Nord", 2u to "Terminus Saint-Jérôme"),
        159u to mapOf(1u to "Terminus Saint-Jérôme"),
        160u to mapOf(1u to "Terminus Saint-Jérôme"),
        161u to mapOf(1u to "Terminus Saint-Jérôme"),
        162u to mapOf(1u to "Terminus Saint-Jérôme"),
        179u to mapOf(1u to "Terminus Sainte-Thérèse"),
        180u to mapOf(1u to "Terminus Sainte-Thérèse"),
        181u to mapOf(1u to "Laval (Sainte-Rose)", 2u to "Terminus Sainte-Thérèse"),
        151u to mapOf(1u to "Terminus Sainte-Thérèse"),
        182u to mapOf(1u to "Terminus Sainte-Thérèse"),
        183u to mapOf(1u to "Fontainebleau - Chambéry", 2u to "Terminus Sainte-Thérèse"),
        184u to mapOf(1u to "Rosemère", 2u to "Gare Rosemère"),
        185u to mapOf(1u to "Lorraine - Terrebonne", 2u to "Terminus Sainte-Thérèse"),
        186u to mapOf(1u to "Sainte-Anne-des-Plaines", 2u to "Terminus Sainte-Thérèse"),
        187u to mapOf(1u to "Terminus Sainte-Thérèse"),
        188u to mapOf(1u to "Terminus Sainte-Thérèse"),
        189u to mapOf(1u to "Lorraine - Terrebonne", 2u to "Gare Rosemère"),
        190u to mapOf(1u to "Gare Blainville"),
        191u to mapOf(1u to "Gare Blainville"),
        198u to mapOf(1u to "Sainte-Thérèse", 2u to "Station Deux-Montagnes"),
        199u to mapOf(1u to "Station Deux-Montagnes", 2u to "Terminus Montmorency"),
        170u to mapOf(1u to "Terminus Saint-Jérôme", 2u to "Terminus Montmorency"),
        200u to mapOf(1u to "Sainte-Anne-des-Plaines", 2u to "Terminus Cartier"),
        201u to mapOf(1u to "Sainte-Thérèse", 2u to "Station Deux-Montagnes"),
        202u to mapOf(1u to "Station Deux-Montagnes", 2u to "Oka - Saint-Placide"),
        203u to mapOf(1u to "Sainte-Thérèse", 2u to "Station Deux-Montagnes"),
        204u to mapOf(1u to "Terminus Terrebonne", 2u to "Sainte-Thérèse"),
        205u to mapOf(1u to "Boisbriand", 2u to "Métro Montmorency"),
        206u to mapOf(1u to "Station Deux-Montagnes", 2u to "Terminus Montmorency"),
        171u to mapOf(1u to "Terminus Saint-Jérôme", 2u to "Terminus Montmorency"),
    )

    private val headsignMapForExoPresquIle: Map<UInt, Map<UInt, String>> = mapOf(
        29u to mapOf(1u to "Terminus Vaudreuil"),
        30u to mapOf(1u to "Terminus Vaudreuil"),
        31u to mapOf(1u to "Gare Dorion", 2u to "Terminus Vaudreuil"),
        32u to mapOf(1u to "Gare Dorion", 2u to "Terminus Vaudreuil"),
        33u to mapOf(1u to "Terminus Vaudreuil"),
        34u to mapOf(1u to "Terminus Vaudreuil"),
        35u to mapOf(1u to "Terminus Vaudreuil"),
        36u to mapOf(1u to "Gare Dorion"),
        37u to mapOf(1u to "Gare Île-Perrot", 2u to "Point-aux-Renards"),
        38u to mapOf(1u to "Gare Île-Perrot"),
        39u to mapOf(1u to "Gare Île-Perrot"),
        40u to mapOf(1u to "Station Anse-à-l'Orme", 2u to "Terminus Vaudreuil"),
        41u to mapOf(1u to "Station Anse-à-l'Orme", 2u to "Terminus Vaudreuil"),
        42u to mapOf(1u to "Cégep John Abbott", 2u to "Terminus Vaudreuil"),
        43u to mapOf(1u to "Station Anse-à-l'Orme", 2u to "Gare Dorion"),
        44u to mapOf(1u to "Terminus Macdonald", 2u to "Notre-Dame-de-l'Île-Perrot"),
        45u to mapOf(1u to "Terminus Vaudreuil"),
        46u to mapOf(1u to "Station Anse-à-l'Orme", 2u to "Dorion"),
        47u to mapOf(1u to "Terminus Macdonald", 2u to "Gare Dorion"),
    )

    private val headsignMapForExoTerrebonneMascouche: Map<UInt, Map<UInt, String>> = mapOf(
        1u to mapOf(1u to "Terminus Terrebonne"),
        2u to mapOf(1u to "Terminus Terrebonne"),
        3u to mapOf(1u to "Terminus Terrebonne"),
        4u to mapOf(1u to "Bois-des-Filion"),
        6u to mapOf(1u to "Terminus Terrebonne"),
        7u to mapOf(1u to "Terminus Terrebonne", 2u to "Terrebonne Ouest"),
        8u to mapOf(1u to "Lachenaie", 2u to "Terminus Terrebonne"),
        9u to mapOf(1u to "Terminus Terrebonne", 2u to "Forum de La Plaine"),
        12u to mapOf(1u to "La Plaine", 2u to "Terminus Terrebonne"),
        13u to mapOf(1u to "Terminus Terrebonne"),
        14u to mapOf(1u to "Terminus Terrebonne", 2u to "Terminus Montmorency"),
        15u to mapOf(1u to "Mascouche", 2u to "Terminus Terrebonne"),
        16u to mapOf(1u to "Terminus Terrebonne"),
        17u to mapOf(1u to "Collège Lionel-Groulx", 2u to "Terminus Terrebonne"),
        19u to mapOf(1u to "Terminus Henri-Bourassa", 2u to "Terminus Terrebonne"),
        25u to mapOf(1u to "Montréal-Nord", 2u to "Terminus Terrebonne"),
        21u to mapOf(1u to "Terminus Radisson", 2u to "Lachenaie"),
        58u to mapOf(1u to "Terrebonne Ouest", 2u to "Terminus Cartier")
    )

    private val headsignMapForExoLassomption: Map<UInt, Map<UInt, String>> = mapOf(
        1u to mapOf(1u to "L'Assomption", 2u to "Repentigny"),
        2u to mapOf(1u to "Saint-Suplice", 2u to "Repentigny"),
        23u to mapOf(1u to "Terminus Repentigny", 2u to "Gare Repentigny"),
        5u to mapOf(1u to "L'Assomption", 2u to "L'Épiphanie"),
        7u to mapOf(1u to "L'Assomption", 2u to "Repentigny"),
        8u to mapOf(1u to "Terminus Repentigny", 2u to "Place Repentigny"),
        10u to mapOf(1u to "L'Assomption", 2u to "Le Gardeur"),
        13u to mapOf(1u to "L'Assomption", 2u to "Repentigny"),
        14u to mapOf(1u to "Iberville / Jacques-Plante", 2u to "Place Repentigny"),
        18u to mapOf(1u to "L'Assomption", 2u to "Terminus Radisson"),
        19u to mapOf(1u to "Terminus Repentigny", 2u to "Terminus Radisson"),
        20u to mapOf(1u to "Repentigny", 2u to "Terminus Radisson"),
        21u to mapOf(1u to "Repentigny", 2u to "Terminus Radisson"),
    )

    fun getHeadsignForSTM(directionId: UInt): String? = directionMapForSTM[directionId]
    fun getHeadsignForRTL(lineId: UInt, directionId: UInt): String? = headsignMapForRTL[lineId]?.get(directionId)
    fun getHeadsignForRTC(lineId: UInt, directionId: UInt): String? = null
    fun getHeadsignForSTL(lineId: UInt, directionId: UInt): String? = headsignMapForSTL[lineId]?.get(directionId)
    fun getHeadsignForExoSorelVarennes(lineId: UInt, directionId: UInt): String? = headsignMapForExoSorelVarennes[lineId]?.get(directionId)
    fun getHeadsignForExoSainteJulie(lineId: UInt, directionId: UInt): String? = headsignMapForExoSainteJulie[lineId]?.get(directionId)
    fun getHeadsignForExoValleeRichelieu(lineId: UInt, directionId: UInt): String? = headsignMapForExoValleeRichelieu[lineId]?.get(directionId)
    fun getHeadsignForExoChamblyRichelieuCarignan(lineId: UInt, directionId: UInt): String? = headsignMapForExoChamblyRichelieuCarignan[lineId]?.get(directionId)
    fun getHeadsignForExoLeRichelain(lineId: UInt, directionId: UInt): String? = headsignMapForExoLeRichelain[lineId]?.get(directionId)
    fun getHeadsignForExoSudOuest(lineId: UInt, directionId: UInt): String? = headsignMapForExoSudOuest[lineId]?.get(directionId)
    fun getHeadsignForExoLaurentides(lineId: UInt, directionId: UInt): String? = headsignMapForExoLaurentides[lineId]?.get(directionId)
    fun getHeadsignForSTLevis(lineId: UInt, directionId: UInt): String? = null
    fun getHeadsignForExoPresquIle(lineId: UInt, directionId: UInt): String? = headsignMapForExoPresquIle[lineId]?.get(directionId)
    fun getHeadsignForExoTerrebonneMascouche(lineId: UInt, directionId: UInt): String? = headsignMapForExoTerrebonneMascouche[lineId]?.get(directionId)
    fun getHeadsignForExoLassomption(lineId: UInt, directionId: UInt): String? = headsignMapForExoLassomption[lineId]?.get(directionId)
    fun getHeadsignForMRCJoliette(lineId: UInt, directionId: UInt): String? = null
    fun getHeadsignForSTQ(lineId: UInt, directionId: UInt): String? = null
}