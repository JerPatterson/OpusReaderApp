package com.transition.ora

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class CardContentConverter {
    companion object {
        fun getFareProductById(context: Context, operatorId: UInt, id: UInt): FareProduct {
            return when (id) {
                FareProductId.OPUS_8TICKETS_STL.id -> FareProduct("8 passages, STL", R.string.old_fare_info)
                FareProductId.OPUS_8TICKETS_STL_RED.id -> FareProduct("8 passages, STL", R.string.old_fare_info)
                FareProductId.OPUS_MONTHLY_STL_RED.id -> FareProduct("Mensuel, STL", R.string.old_fare_info)
                FareProductId.OPUS_1TICKET_STM_RED.id -> FareProduct("1 passage, STM", R.string.old_fare_info)
                FareProductId.OPUS_2TICKETS_STM.id -> FareProduct("2 passages, STM", R.string.old_fare_info)
                FareProductId.OPUS_2TICKETS_STM_RED.id -> FareProduct("2 passages, STM", R.string.old_fare_info)
                FareProductId.OPUS_10TICKETS_STM.id -> FareProduct("10 passages, STM", R.string.old_fare_info)
                FareProductId.OPUS_MONTHLY_TRAM3.id -> FareProduct("Mensuel, TRAM 3", R.string.old_fare_info)

                FareProductId.OPUS_1TICKET_BUS.id -> FareProduct("1 passage, Bus", R.string.bus_one_ticket_info)
                FareProductId.OPUS_10TICKETS_BUS.id -> FareProduct("10 passages, Bus", R.string.bus_ten_tickets_info)

                FareProductId.OPUS_2TICKETS_ALL_MODES_A_RED.id -> FareProduct("2 passages, Tous modes A", R.string.all_modes_A_two_tickets_info)
                FareProductId.OPUS_10TICKETS_ALL_MODES_A.id -> FareProduct("10 passages, Tous modes A", R.string.all_modes_A_ten_tickets_info)
                FareProductId.OPUS_WEEKLY_ALL_MODES_A.id -> FareProduct("Hebdo, Tous modes A", R.string.all_modes_A_weekly_info)
                FareProductId.OPUS_MONTHLY_ALL_MODES_A.id -> FareProduct("Mensuel, Tous modes A", R.string.all_modes_A_monthly_info)
                FareProductId.OPUS_MONTHLY_ALL_MODES_A_STU.id -> FareProduct("Mensuel, Tous modes A", R.string.all_modes_A_monthly_info)
                FareProductId.OPUS_MONTHLY_ALL_MODES_A_RED.id -> FareProduct("Mensuel, Tous modes A", R.string.all_modes_A_monthly_info)

                FareProductId.OPUS_10TICKETS_ALL_MODES_AB.id -> FareProduct("10 passages, Tous modes AB", R.string.all_modes_AB_ten_tickets_info)
                FareProductId.OPUS_24HOURS_ALL_MODES_AB.id -> FareProduct("24hrs, Tous modes AB", R.string.all_modes_AB_24_hours_info)
                FareProductId.OPUS_MONTHLY_ALL_MODES_AB.id -> FareProduct("Mensuel, Tous modes AB", R.string.all_modes_AB_monthly_info)
                FareProductId.OPUS_MONTHLY_ALL_MODES_AB_STU.id -> FareProduct("Mensuel, Tous modes AB", R.string.all_modes_AB_monthly_info)

                FareProductId.OPUS_EVENING_UNLIMITED.id -> FareProduct("Soirée illimitée", R.string.evening_unlimited_info)


                FareProductId.OPUS_6TICKETS_SDO_MTL.id -> FareProduct("6 passages, SDO-MTL", R.string.sdo_mtl_six_tickets_info)



                FareProductId.OCC_6TICKETS_TRAM2.id -> FareProduct("6 passages, TRAM 2", R.string.old_fare_info)

                FareProductId.OCC_1TICKET_BUS.id -> FareProduct("1 passage, Bus", R.string.bus_one_ticket_info)
                FareProductId.OCC_2TICKETS_BUS.id -> FareProduct("2 passages, Bus", R.string.bus_two_tickets_info)
                FareProductId.OCC_10TICKETS_BUS.id -> FareProduct("10 passages, Bus", R.string.bus_ten_tickets_info)
                FareProductId.OCC_24HOURS_BUS.id -> FareProduct("24hrs, Bus", R.string.bus_24_hours_info)

                FareProductId.OCC_2TICKETS_ALL_MODES_A.id -> FareProduct("2 passages, Tous modes A", R.string.all_modes_A_two_tickets_info)
                FareProductId.OCC_24HOURS_ALL_MODES_A.id -> FareProduct("24hrs, Tous modes A", R.string.all_modes_A_24_hours_info)

                FareProductId.OCC_1TICKET_ALL_MODES_AB.id-> FareProduct("1 passage, Tous modes AB", R.string.all_modes_AB_one_ticket_info)
                FareProductId.OCC_2TICKETS_ALL_MODES_AB.id -> FareProduct("2 passages, Tous modes AB", R.string.all_modes_AB_two_tickets_info)
                FareProductId.OCC_10TICKETS_ALL_MODES_AB.id -> FareProduct("10 passages, Tous modes AB", R.string.all_modes_AB_ten_tickets_info)
                FareProductId.OCC_24HOURS_ALL_MODES_AB.id -> FareProduct("24hrs, Tous modes AB", R.string.all_modes_AB_24_hours_info)
                FareProductId.OCC_3DAYS_ALL_MODES_AB.id -> FareProduct("3 jours, Tous modes AB", R.string.all_modes_AB_three_days_info)

                FareProductId.OCC_1TICKET_ALL_MODES_ABC.id -> FareProduct("1 passage, Tous modes ABC", R.string.all_modes_ABC_one_ticket_info)
                FareProductId.OCC_2TICKETS_ALL_MODES_ABC.id -> FareProduct("2 passages, Tous modes ABC", R.string.all_modes_ABC_two_tickets_info)
                FareProductId.OCC_3DAYS_ALL_MODES_ABC.id -> FareProduct("3 jours, Tous modes ABC", R.string.all_modes_AB_three_days_info)

                FareProductId.OCC_EVENING_UNLIMITED.id -> FareProduct("Soirée illimitée", R.string.evening_unlimited_info)
                FareProductId.OCC_WEEKEND_UNLIMITED.id -> FareProduct("Week-end illimité", R.string.weekend_unlimited_info)

                FareProductId.OCC_1TICKET_STL_ENG_QUAL.id -> FareProduct("1 passage, STL Eng. Qualité", R.string.stl_eng_qual_one_ticket_info)
                FareProductId.OCC_2TICKETS_ALL_MODES_ABCD_SPECIAL_ILE_AUX_TOURTES.id -> FareProduct("2 passages, Tous modes ABCD", R.string.all_modes_ABCD_two_tickets_info_iat)
                FareProductId.OCC_10TICKETS_ALL_MODES_ABC_SPECIAL_ILE_AUX_TOURTES.id -> FareProduct("10 passages, Tous modes ABC", R.string.all_modes_ABC_ten_tickets_info_iat)


                else -> {
                    val proposition: FareProduct? = lookForFareProposition(
                        context,
                        operatorId.toString(),
                        id.toString()
                    )

                    return proposition ?: FareProduct("Unknown (fareID: ${id})", R.string.unknown_fare_info)
                }
            }
        }

        fun getOperatorById(id: UInt): Operator {
            return when (id) {
                4u -> Operator("ARTM", "#007373", R.drawable.artm)
                8u -> Operator("STM", "#00aeef", R.drawable.stm)
                12u -> Operator("RTL", "#9e2536", R.drawable.rtl)
                16u -> Operator("exo", "#000000", R.drawable.exo)
                20u -> Operator("RTC", "#003878", R.drawable.rtc)
                24u -> Operator("STL", "#151f6d", R.drawable.stl)
                60u -> Operator("exo", "#000000", R.drawable.exo)
                72u -> Operator("exo", "#000000", R.drawable.exo)
                64u -> Operator("STLévis", "#0091b3", R.drawable.stlevis)
                80u -> Operator("MRCJoliette", "#81a449", R.drawable.mrcjoliette)
                88u -> Operator("REM", "#034638", R.drawable.rem)

                else -> Operator("Unknown (id: $id)", "#696969", R.drawable.unknown)
            }
        }

        fun getLineById(context: Context, operatorId: UInt, lineId: UInt): Line {
            return when (operatorId) {
                8u -> this.getSTMLineById(context, operatorId, lineId)
                12u -> this.getRTLLineById(context, operatorId, lineId)
                16u -> this.getEXOLineById(context, operatorId, lineId)
                20u -> this.getRTCLineById(context, operatorId, lineId)
                24u -> this.getSTLLineById(context, operatorId, lineId)
                60u -> this.getEXOLaurentidesLineById(context, operatorId, lineId)
                64u -> this.getSTLevisLineById(context, operatorId, lineId)
                72u -> this.getEXOTerrebonneMascoucheLineById(context, operatorId, lineId)
                80u -> this.getMRCJolietteLineById(context, operatorId, lineId)
                88u -> this.getREMLineById(context, operatorId, lineId)

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

        private fun getSTMLineById(context: Context, operatorId: UInt, id: UInt): Line {
            // TODO Find ids of all lines
            //   (Id of the line is on 9bits so higher
            //   than 512u means it's not known yet)
            return when (id) {
                1u -> Line("1", "Ligne Verte", "#00a553", "#ffffff", R.drawable.metro)
                2u -> Line("2", "Ligne Orange", "#f2832c", "#ffffff", R.drawable.metro)
                224u -> Line("2", "Ligne Orange (zone B)", "#f2832c", "#ffffff", R.drawable.metro)
                3u -> Line("4", "Ligne Jaune", "#ffd200", "#000000", R.drawable.metro)
                4u -> Line("5", "Ligne Bleue", "#0078d0", "#ffffff", R.drawable.metro)
                5u -> Line("10", "De Lorimier", "#009ee0", "#ffffff", R.drawable.bus)
                6u -> Line("11", "Parc-du-Mont-Royal / Ridgewood", "#009ee0", "#ffffff", R.drawable.bus)
                7u -> Line("12", "Île-des-Soeurs", "#009ee0", "#ffffff", R.drawable.bus)
                8u -> Line("13", "Christophe-Colomb", "#009ee0", "#ffffff", R.drawable.bus)
                9u -> Line("14", "Atateken", "#009ee0", "#ffffff", R.drawable.bus)
                10u -> Line("15", "Sainte-Catherine", "#009ee0", "#ffffff", R.drawable.bus)
                11u -> Line("16", "Graham", "#009ee0", "#ffffff", R.drawable.bus)
                12u -> Line("17", "Décarie", "#009ee0", "#ffffff", R.drawable.bus)
                13u -> Line("18", "Beaubien", "#781b7d", "#ffffff", R.drawable.bus)
                14u -> Line("22", "Notre-Dame", "#009ee0", "#ffffff", R.drawable.bus)
                15u -> Line("24", "Sherbrooke", "#781b7d", "#ffffff", R.drawable.bus)
                16u -> Line("25", "Angus", "#009ee0", "#ffffff", R.drawable.bus)
                231u -> Line("26", "Mercier-Est", "#009ee0", "#ffffff", R.drawable.bus)
                17u -> Line("27", "Boulevard Saint-Joseph", "#009ee0", "#ffffff", R.drawable.bus)
                18u -> Line("28", "Honoré-Beaugrand", "#009ee0", "#ffffff", R.drawable.bus)
                19u -> Line("29", "Rachel", "#009ee0", "#ffffff", R.drawable.bus)
                20u -> Line("30", "Saint-Denis / Saint-Hubert", "#009ee0", "#ffffff", R.drawable.bus)
                21u -> Line("31", "Saint-Denis", "#009ee0", "#ffffff", R.drawable.bus)
                22u -> Line("32", "Lacordaire", "#781b7d", "#ffffff", R.drawable.bus)
                23u -> Line("33", "Langelier", "#781b7d", "#ffffff", R.drawable.bus)
                24u -> Line("34", "Sainte-Catherine", "#009ee0", "#ffffff", R.drawable.bus)
                279u -> Line("35", "Griffintown", "#009ee0", "#ffffff", R.drawable.bus)
                25u -> Line("36", "Monk", "#009ee0", "#ffffff", R.drawable.bus)
                26u -> Line("37", "Jolicoeur", "#009ee0", "#ffffff", R.drawable.bus)
                338u -> Line("38", "De l'Église", "#009ee0", "#ffffff", R.drawable.bus)
                27u -> Line("39", "Des Grandes-Prairies", "#009ee0", "#ffffff", R.drawable.bus)
                28u -> Line("40", "Henri-Bourassa-Est", "#009ee0", "#ffffff", R.drawable.bus)
                226u -> Line("41", "Quartier Saint-Michel / Ahuntsic", "#009ee0", "#ffffff", R.drawable.bus)
                29u -> Line("43", "Monselet", "#009ee0", "#ffffff", R.drawable.bus)
                30u -> Line("44", "Armand-Bombardier", "#781b7d", "#ffffff", R.drawable.bus)
                31u -> Line("45", "Papineau", "#781b7d", "#ffffff", R.drawable.bus)
                32u -> Line("46", "Casgrain", "#009ee0", "#ffffff", R.drawable.bus)
                33u -> Line("47", "Masson", "#009ee0", "#ffffff", R.drawable.bus)
                34u -> Line("48", "Perras", "#781b7d", "#ffffff", R.drawable.bus)
                35u -> Line("49", "Maurice-Duplessis", "#781b7d", "#ffffff", R.drawable.bus)
                339u -> Line("50", "Vieux-Montréal / Vieux-Port", "#009ee0", "#ffffff", R.drawable.bus)
                36u -> Line("51", "Édouard-Montpetit", "#781b7d", "#ffffff", R.drawable.bus)
                37u -> Line("52", "de Liège", "#009ee0", "#ffffff", R.drawable.bus)
                38u -> Line("53", "Boulevard Saint-Laurent", "#009ee0", "#ffffff", R.drawable.bus)
                39u -> Line("54", "Charland / Chabanel", "#009ee0", "#ffffff", R.drawable.bus)
                40u -> Line("55", "Boulevard Saint-Laurent", "#781b7d", "#ffffff", R.drawable.bus)
                41u -> Line("56", "Saint-Hubert", "#009ee0", "#ffffff", R.drawable.bus)
                42u -> Line("57", "Pointe-Saint-Charles", "#009ee0", "#ffffff", R.drawable.bus)
                43u -> Line("58", "Wellington", "#009ee0", "#ffffff", R.drawable.bus)
                44u -> Line("61", "Wellington", "#009ee0", "#ffffff", R.drawable.bus)
                45u -> Line("63", "Girouard", "#009ee0", "#ffffff", R.drawable.bus)
                46u -> Line("64", "Grenet", "#781b7d", "#ffffff", R.drawable.bus)
                47u -> Line("66", "The Boulevard", "#009ee0", "#ffffff", R.drawable.bus)
                48u -> Line("67", "Saint-Michel", "#781b7d", "#ffffff", R.drawable.bus)
                49u -> Line("68", "Pierrefonds", "#009ee0", "#ffffff", R.drawable.bus)
                50u -> Line("69", "Gouin", "#781b7d", "#ffffff", R.drawable.bus)
                51u -> Line("70", "Bois-Franc", "#009ee0", "#ffffff", R.drawable.bus)
                232u -> Line("71", "Du Centre", "#009ee0", "#ffffff", R.drawable.bus)
                52u -> Line("72", "Alfred-Nobel", "#009ee0", "#ffffff", R.drawable.bus)
                53u -> Line("73", "Dalton", "#009ee0", "#ffffff", R.drawable.bus)
                54u -> Line("74", "Bridge", "#009ee0", "#ffffff", R.drawable.bus)
                55u -> Line("75", "De la Commune", "#009ee0", "#ffffff", R.drawable.bus)
                56u -> Line("76", "McArthur", "#009ee0", "#ffffff", R.drawable.bus)
                57u -> Line("77", "Cégep Marie-Victorin", "#009ee0", "#ffffff", R.drawable.bus)
                58u -> Line("78", "Laurendeau", "#009ee0", "#ffffff", R.drawable.bus)
                59u -> Line("80", "Avenue du Parc", "#781b7d", "#ffffff", R.drawable.bus)
                60u -> Line("85", "Hochelaga", "#009ee0", "#ffffff", R.drawable.bus)
                61u -> Line("86", "Pointe-aux-Trembles", "#009ee0", "#ffffff", R.drawable.bus)
                62u -> Line("86", "Gouin / Perras", "#009ee0", "#ffffff", R.drawable.bus)
                63u -> Line("90", "Saint-Jacques", "#781b7d", "#ffffff", R.drawable.bus)
                64u -> Line("92", "Jean-Talon Ouest", "#009ee0", "#ffffff", R.drawable.bus)
                65u -> Line("93", "Jean-Talon", "#009ee0", "#ffffff", R.drawable.bus)
                66u -> Line("94", "D'Iberville", "#009ee0", "#ffffff", R.drawable.bus)
                67u -> Line("95", "Bélanger", "#009ee0", "#ffffff", R.drawable.bus)
                68u -> Line("97", "Avenue-du-Mont-Royal", "#781b7d", "#ffffff", R.drawable.bus)
                69u -> Line("99", "Villeray", "#009ee0", "#ffffff", R.drawable.bus)
                70u -> Line("100", "Crémazie", "#009ee0", "#ffffff", R.drawable.bus)
                71u -> Line("101", "Saint-Patrick", "#009ee0", "#ffffff", R.drawable.bus)
                72u -> Line("102", "Somerled", "#009ee0", "#ffffff", R.drawable.bus)
                73u -> Line("103", "Monkland", "#781b7d", "#ffffff", R.drawable.bus)
                74u -> Line("104", "Cavendish", "#009ee0", "#ffffff", R.drawable.bus)
                75u -> Line("105", "Sherbrooke", "#781b7d", "#ffffff", R.drawable.bus)
                76u -> Line("106", "Newman", "#781b7d", "#ffffff", R.drawable.bus)
                77u -> Line("107", "Verdun", "#781b7d", "#ffffff", R.drawable.bus)
                78u -> Line("108", "Bannantyne", "#009ee0", "#ffffff", R.drawable.bus)
                79u -> Line("109", "Boulevard Shevchenko", "#009ee0", "#ffffff", R.drawable.bus)
                80u -> Line("110", "Centrale", "#009ee0", "#ffffff", R.drawable.bus)
                81u -> Line("112", "Airlie", "#781b7d", "#ffffff", R.drawable.bus)
                82u -> Line("113", "Lapierre", "#009ee0", "#ffffff", R.drawable.bus)
                333u -> Line("114", "Angrignon", "#009ee0", "#ffffff", R.drawable.bus)
                83u -> Line("115", "Paré", "#009ee0", "#ffffff", R.drawable.bus)
                84u -> Line("116", "Lafleur / Norman", "#009ee0", "#ffffff", R.drawable.bus)
                85u -> Line("117", "O'Brien", "#009ee0", "#ffffff", R.drawable.bus)
                86u -> Line("119", "Rockland", "#009ee0", "#ffffff", R.drawable.bus)
                87u -> Line("121", "Sauvé / Côte-Vertu", "#781b7d", "#ffffff", R.drawable.bus)
                88u -> Line("123", "Dollard", "#009ee0", "#ffffff", R.drawable.bus)
                89u -> Line("124", "Victoria", "#009ee0", "#ffffff", R.drawable.bus)
                90u -> Line("125", "Ontario", "#009ee0", "#ffffff", R.drawable.bus)
                91u -> Line("126", "Thimens / Grenet", "#009ee0", "#ffffff", R.drawable.bus)
                92u -> Line("128", "Saint-Laurent", "#009ee0", "#ffffff", R.drawable.bus)
                93u -> Line("129", "Côte-Sainte-Catherine", "#009ee0", "#ffffff", R.drawable.bus)
                94u -> Line("131", "De l'Assomption", "#009ee0", "#ffffff", R.drawable.bus)
                95u -> Line("132", "Viau", "#009ee0", "#ffffff", R.drawable.bus)
                234u -> Line("136", "Viau", "#781b7d", "#ffffff", R.drawable.bus)
                96u -> Line("135", "De l'Esplanade", "#009ee0", "#ffffff", R.drawable.bus)
                97u -> Line("138", "Notre-Dame-de-Grâce", "#009ee0", "#ffffff", R.drawable.bus)
                98u -> Line("139", "Pie-IX", "#009ee0", "#ffffff", R.drawable.bus)
                99u -> Line("140", "Fleury", "#009ee0", "#ffffff", R.drawable.bus)
                100u -> Line("141", "Jean-Talon Est", "#781b7d", "#ffffff", R.drawable.bus)
                101u -> Line("143", "Métrobus Charleroi", "#009ee0", "#ffffff", R.drawable.bus)
                102u -> Line("144", "Avenue des Pins", "#009ee0", "#ffffff", R.drawable.bus)
                103u -> Line("146", "Christophe-Colomb / Meilleur", "#009ee0", "#ffffff", R.drawable.bus)
                104u -> Line("148", "Métrobus Maurice-Duplessis", "#009ee0", "#ffffff", R.drawable.bus)
                105u -> Line("150", "René-Lévesque", "#009ee0", "#ffffff", R.drawable.bus)
                106u -> Line("159", "Métrobus Henri-Bourassa", "#009ee0", "#ffffff", R.drawable.bus)
                107u -> Line("160", "Barclay", "#009ee0", "#ffffff", R.drawable.bus)
                108u -> Line("161", "Van Horne", "#781b7d", "#ffffff", R.drawable.bus)
                109u -> Line("162", "Westminster", "#009ee0", "#ffffff", R.drawable.bus)
                110u -> Line("164", "Dudemaine", "#009ee0", "#ffffff", R.drawable.bus)
                111u -> Line("165", "Côte-des-Neiges", "#781b7d", "#ffffff", R.drawable.bus)
                112u -> Line("166", "Queen-Mary", "#009ee0", "#ffffff", R.drawable.bus)
                113u -> Line("167", "Les Îles", "#009ee0", "#ffffff", R.drawable.bus)
                114u -> Line("168", "Cité-du-Havre", "#009ee0", "#ffffff", R.drawable.bus)
                115u -> Line("170", "Keller", "#009ee0", "#ffffff", R.drawable.bus)
                116u -> Line("171", "Henri-Bourassa", "#781b7d", "#ffffff", R.drawable.bus)
                323u -> Line("172", "Du Golf", "#009ee0", "#ffffff", R.drawable.bus)
                117u -> Line("174", "Côte-Vertu-Ouest", "#009ee0", "#ffffff", R.drawable.bus)
                118u -> Line("175", "Griffith / Saint-François", "#009ee0", "#ffffff", R.drawable.bus)
                119u -> Line("176", "Berlioz", "#009ee0", "#ffffff", R.drawable.bus)
                120u -> Line("177", "Thimens", "#009ee0", "#ffffff", R.drawable.bus)
                235u -> Line("178", "Pointe-Nord / Île-des-Soeurs", "#009ee0", "#ffffff", R.drawable.bus)
                121u -> Line("179", "De l'Acadie", "#009ee0", "#ffffff", R.drawable.bus)
                122u -> Line("180", "De Salaberry", "#009ee0", "#ffffff", R.drawable.bus)
                123u -> Line("182", "Métrobus Sherbrooke", "#009ee0", "#ffffff", R.drawable.bus)
                124u -> Line("183", "Gouin Est", "#009ee0", "#ffffff", R.drawable.bus)
                125u -> Line("184", "Métrobus Bout-de-l’Île", "#009ee0", "#ffffff", R.drawable.bus)
                126u -> Line("185", "Sherbrooke", "#009ee0", "#ffffff", R.drawable.bus)
                127u -> Line("186", "Sherbrooke-Est", "#009ee0", "#ffffff", R.drawable.bus)
                128u -> Line("187", "René-Lévesque", "#781b7d", "#ffffff", R.drawable.bus)
                129u -> Line("188", "Couture", "#009ee0", "#ffffff", R.drawable.bus)
                130u -> Line("189", "Notre-Dame", "#009ee0", "#ffffff", R.drawable.bus)
                131u -> Line("190", "Métrobus Lachine", "#009ee0", "#ffffff", R.drawable.bus)
                335u -> Line("190", "Norman", "#009ee0", "#ffffff", R.drawable.bus)
                132u -> Line("191", "Broadway / Provost", "#009ee0", "#ffffff", R.drawable.bus)
                133u -> Line("192", "Robert", "#009ee0", "#ffffff", R.drawable.bus) // To confirm
                134u -> Line("193", "Jarry", "#781b7d", "#ffffff", R.drawable.bus) // To confirm
                135u -> Line("194", "Métrobus Rivière-des-Prairies", "#009ee0", "#ffffff", R.drawable.bus)
                136u -> Line("195", "Sherbrooke / Notre-Dame", "#009ee0", "#ffffff", R.drawable.bus)
                137u -> Line("196", "Parc-Industriel-Lachine", "#781b7d", "#ffffff", R.drawable.bus)
                138u -> Line("197", "Rosemont", "#781b7d", "#ffffff", R.drawable.bus)
                334u -> Line("198", "Broadway", "#009ee0", "#ffffff", R.drawable.bus)
                139u -> Line("199", "Métrobus Lacordaire", "#009ee0", "#ffffff", R.drawable.bus)
                140u -> Line("200", "Sainte-Anne-de-Bellevue", "#009ee0", "#ffffff", R.drawable.bus)
                141u -> Line("201", "Saint-Charles / Saint-Jean", "#009ee0", "#ffffff", R.drawable.bus)
                142u -> Line("202", "Dawson", "#009ee0", "#ffffff", R.drawable.bus)
                143u -> Line("203", "Carson", "#009ee0", "#ffffff", R.drawable.bus)
                144u -> Line("204", "Cardinal", "#009ee0", "#ffffff", R.drawable.bus)
                145u -> Line("205", "Gouin", "#009ee0", "#ffffff", R.drawable.bus)
                146u -> Line("206", "Roger-Pilon", "#009ee0", "#ffffff", R.drawable.bus)
                147u -> Line("207", "Jacques-Bizard", "#009ee0", "#ffffff", R.drawable.bus)
                148u -> Line("208", "Brunswick", "#009ee0", "#ffffff", R.drawable.bus)
                149u -> Line("209", "Des Sources", "#009ee0", "#ffffff", R.drawable.bus)
                150u -> Line("210", "Jonh Abbott", "#009ee0", "#ffffff", R.drawable.bus)
                151u -> Line("211", "Bord-du-Lac", "#009ee0", "#ffffff", R.drawable.bus)
                236u -> Line("212", "Sainte-Anne", "#009ee0", "#ffffff", R.drawable.bus)
                152u -> Line("213", "Parc-Industriel-Saint-Laurent", "#009ee0", "#ffffff", R.drawable.bus)
                153u -> Line("214", "Des Sources", "#009ee0", "#ffffff", R.drawable.bus)
                154u -> Line("215", "Henri-Bourassa", "#009ee0", "#ffffff", R.drawable.bus)
                155u -> Line("216", "Transcanadienne", "#009ee0", "#ffffff", R.drawable.bus)
                156u -> Line("217", "Anse-à-l'Orme", "#009ee0", "#ffffff", R.drawable.bus)
                157u -> Line("218", "Antoine-Faucon", "#009ee0", "#ffffff", R.drawable.bus)
                158u -> Line("219", "Chemin Sainte-Marie", "#009ee0", "#ffffff", R.drawable.bus)
                159u -> Line("220", "Kieran", "#009ee0", "#ffffff", R.drawable.bus)
                160u -> Line("221", "Métrobus Lionel-Groulx", "#009ee0", "#ffffff", R.drawable.bus)
                161u -> Line("225", "Hymus", "#009ee0", "#ffffff", R.drawable.bus)
                162u -> Line("261", "Trainbus Saint-Charles", "#009ee0", "#ffffff", R.drawable.bus)
                163u -> Line("265", "Trainbus Île-Bizard", "#009ee0", "#ffffff", R.drawable.bus)
                164u -> Line("268", "Trainbus Pierrefonds", "#009ee0", "#ffffff", R.drawable.bus)
                174u -> Line("350", "Verdun / LaSalle", "#000000", "#ffffff", R.drawable.bus)
                228u -> Line("353", "Lacordaire / Maurice-Duplessis", "#000000", "#ffffff", R.drawable.bus)
                229u -> Line("354", "Sainte-Anne-de-Bellevue / Centre-ville", "#000000", "#ffffff", R.drawable.bus)
                175u -> Line("355", "Pie-IX", "#000000", "#ffffff", R.drawable.bus)
                176u -> Line("356", "Lachine / YUL Aéroport / Des Sources", "#000000", "#ffffff", R.drawable.bus)
                177u -> Line("357", "Saint-Michel", "#000000", "#ffffff", R.drawable.bus)
                178u -> Line("358", "Sainte-Catherine", "#000000", "#ffffff", R.drawable.bus)
                179u -> Line("359", "Papineau", "#000000", "#ffffff", R.drawable.bus)
                180u -> Line("360", "Avenue des Pins", "#000000", "#ffffff", R.drawable.bus)
                181u -> Line("361", "Saint-Denis", "#000000", "#ffffff", R.drawable.bus)
                182u -> Line("362", "Hochelaga / Notre-Dame", "#000000", "#ffffff", R.drawable.bus)
                183u -> Line("363", "Boulevard Saint-Laurent", "#000000", "#ffffff", R.drawable.bus)
                184u -> Line("364", "Sherbrooke / Joseph-Renaud", "#000000", "#ffffff", R.drawable.bus)
                185u -> Line("365", "Avenue du Parc", "#000000", "#ffffff", R.drawable.bus)
                186u -> Line("368", "Avenue-du-Mont-Royal", "#000000", "#ffffff", R.drawable.bus)
                187u -> Line("369", "Côte-des-Neiges", "#000000", "#ffffff", R.drawable.bus)
                188u -> Line("370", "Rosemont", "#000000", "#ffffff", R.drawable.bus)
                189u -> Line("371", "Décarie", "#000000", "#ffffff", R.drawable.bus)
                190u -> Line("372", "Jean-Talon", "#000000", "#ffffff", R.drawable.bus)
                230u -> Line("376", "Pierrefonds / Centre-ville", "#000000", "#ffffff", R.drawable.bus)
                191u -> Line("378", "Sauvé / YUL Aéroport", "#000000", "#ffffff", R.drawable.bus)
                192u -> Line("380", "Henri-Bourassa", "#000000", "#ffffff", R.drawable.bus)
                193u -> Line("382", "Pierrefonds / Saint-Charles", "#000000", "#ffffff", R.drawable.bus)
                237u -> Line("401", "Express Saint-Charles", "#009ee0", "#ffffff", R.drawable.bus)
                261u -> Line("405", "Express Bord-du-Lac", "#009ee0", "#ffffff", R.drawable.bus)
                238u -> Line("406", "Express Newman", "#781b7d", "#ffffff", R.drawable.bus)
                239u -> Line("407", "Express Île-Bizard", "#009ee0", "#ffffff", R.drawable.bus)
                240u -> Line("409", "Express Des Sources", "#009ee0", "#ffffff", R.drawable.bus)
                165u -> Line("410", "Express Notre-Dame", "#009ee0", "#ffffff", R.drawable.bus)
                241u -> Line("411", "Express Lionel-Groulx", "#009ee0", "#ffffff", R.drawable.bus)
                242u -> Line("419", "Express John Abbott", "#009ee0", "#ffffff", R.drawable.bus)
                166u -> Line("420", "Express Notre-Dame-de-Grâce", "#009ee0", "#ffffff", R.drawable.bus)
                262u -> Line("425", "Express Anse-à-l'Orme", "#009ee0", "#ffffff", R.drawable.bus)
                218u -> Line("427", "Express Saint-Joseph", "#009ee0", "#ffffff", R.drawable.bus)
                167u -> Line("430", "Express Pointe-aux-Trembles", "#009ee0", "#ffffff", R.drawable.bus)
                243u -> Line("432", "Express Lacordaire", "#009ee0", "#ffffff", R.drawable.bus)
                244u -> Line("435", "Express Du Parc / Côte-des-Neiges", "#009ee0", "#ffffff", R.drawable.bus)
                245u -> Line("439", "Express Pie-IX", "#781b7d", "#ffffff", R.drawable.bus)
                246u -> Line("440", "Express Charleroi", "#009ee0", "#ffffff", R.drawable.bus)
                247u -> Line("444", "Express Cégep Marie-Victorin", "#009ee0", "#ffffff", R.drawable.bus)
                282u -> Line("445", "Express Papineau", "#009ee0", "#ffffff", R.drawable.bus)
                248u -> Line("448", "Express Maurice-Duplessis", "#009ee0", "#ffffff", R.drawable.bus)
                249u -> Line("449", "Express Rivière-des-Prairies", "#009ee0", "#ffffff", R.drawable.bus)
                168u -> Line("460", "Express Métropolitaine", "#009ee0", "#ffffff", R.drawable.bus)
                283u -> Line("465", "Express Côte-des-Neiges", "#009ee0", "#ffffff", R.drawable.bus)
                211u -> Line("467", "Express Saint-Michel", "#009ee0", "#ffffff", R.drawable.bus)
                250u -> Line("468", "Express Pierrefonds / Gouin", "#009ee0", "#ffffff", R.drawable.bus)
                251u -> Line("469", "Express Henri-Bourassa", "#009ee0", "#ffffff", R.drawable.bus)
                169u -> Line("470", "Express Pierrefonds", "#781b7d", "#ffffff", R.drawable.bus)
                263u -> Line("475", "Express Dollard-des-Ormeaux", "#009ee0", "#ffffff", R.drawable.bus)
                170u -> Line("480", "Pointe-Nord / Île-des-Soeurs", "#009ee0", "#ffffff", R.drawable.bus)
                284u -> Line("480", "Express du Parc", "#009ee0", "#ffffff", R.drawable.bus)
                264u -> Line("485", "Express Antoine-Faucon", "#009ee0", "#ffffff", R.drawable.bus)
                252u -> Line("486", "Express Sherbrooke", "#009ee0", "#ffffff", R.drawable.bus)
                253u -> Line("487", "Express Bout-de-l'Île", "#009ee0", "#ffffff", R.drawable.bus)
                254u -> Line("491", "Express Lachine", "#009ee0", "#ffffff", R.drawable.bus)
                255u -> Line("495", "Express Lachine / LaSalle", "#009ee0", "#ffffff", R.drawable.bus)
                256u -> Line("496", "Express Victoria", "#781b7d", "#ffffff", R.drawable.bus)
                171u -> Line("505", "Voie réservée Pie-IX", "#009ee0", "#ffffff", R.drawable.bus)
                172u -> Line("506", "Voie réservée Newman", "#009ee0", "#ffffff", R.drawable.bus)
                173u -> Line("535", "Voie réservée Du Parc / Côte-des-Neiges", "#009ee0", "#ffffff", R.drawable.bus)
                257u -> Line("715", "Vieux-Montréal / Vieux-Port", "#009ee0", "#ffffff", R.drawable.bus)
                219u -> Line("747", "YUL Aéroport / Centre-Ville", "#009ee0", "#ffffff", R.drawable.bus)
                258u -> Line("767", "La Ronde / Station Jean-Drapeau", "#009ee0", "#ffffff", R.drawable.bus)
                259u -> Line("769", "La Ronde / Station Papineau", "#009ee0", "#ffffff", R.drawable.bus)
                260u -> Line("777", "Jean-Drapeau / Casino / Bonaventure", "#009ee0", "#ffffff", R.drawable.bus)
                336u -> Line("872", "Île-des-Soeurs", "#009ee0", "#ffffff", R.drawable.bus)
                281u -> Line("968", "Trainbus Roxboro / Côte-Vertu", "#009ee0", "#ffffff", R.drawable.bus)

                512u -> Line("19", "Chabanel / Marché Central", "#009ee0", "#ffffff", R.drawable.bus)
                513u -> Line("21", "Place du Commerce", "#009ee0", "#ffffff", R.drawable.bus)
                514u -> Line("81", "Saint-Jean-Baptiste", "#009ee0", "#ffffff", R.drawable.bus)
                702u -> Line("428", "Express Parcs industriels de l'Est", "#009ee0", "#ffffff", R.drawable.bus)
                725u -> Line("711", "Parc-du-Mont-Royal / Oratoire", "#009ee0", "#ffffff", R.drawable.bus)
                729u -> Line("768", "Plage Jean-Doré / Station Jean-Drapeau", "#009ee0", "#ffffff", R.drawable.bus)
                732u -> Line("811", "Navette Services santé", "#009ee0", "#ffffff", R.drawable.bus)
                733u -> Line("822", "Navette Longue-Pointe", "#009ee0", "#ffffff", R.drawable.bus)
                735u -> Line("874", "Robert-Bourassa", "#009ee0", "#ffffff", R.drawable.bus)

                else -> {
                    val proposition: Line? = lookForLineProposition(
                        context,
                        operatorId.toString(),
                        id.toString(),
                        R.drawable.bus
                    )

                    return proposition ?: Line("?", "STM ($id)", "#009ee0", "#ffffff", R.drawable.bus)
                }
            }
        }

        private fun getRTLLineById(context: Context, operatorId: UInt, id: UInt): Line {
            // TODO Find ids of all lines
            //   (Id of the line is on 9bits so higher
            //   than 512u means it's not known yet)
            return when (id) {
                2u -> Line("1", "Desaulniers / Victoria / Windsor", "#9e2536", "#ffffff", R.drawable.bus)
                5u -> Line("5", "Auteuil / Mtée St-Hubert / Maisonneuve", "#9e2536", "#ffffff", R.drawable.bus)
                8u -> Line("6", "Victoria", "#9e2536", "#ffffff", R.drawable.bus)
                9u -> Line("8", "Ch. Chambly / Cousineau / Promenades St-Bruno", "#9e2536", "#ffffff", R.drawable.bus)
                12u -> Line("13", "Riverside / Secteurs P-V Brossard", "#9e2536", "#ffffff", R.drawable.bus)
                13u -> Line("14", "Rome / DIX30", "#9e2536", "#ffffff", R.drawable.bus)
                14u -> Line("15", "Riverside / Alexandra / Churchill", "#9e2536", "#ffffff", R.drawable.bus)
                16u -> Line("16", "Nobert / King-George / Adoncour", "#9e2536", "#ffffff", R.drawable.bus)
                17u -> Line("17", "Roland-Therrien / Roberval", "#9e2536", "#ffffff", R.drawable.bus)
                18u -> Line("19", "Davis", "#9e2536", "#ffffff", R.drawable.bus)
                19u -> Line("20", "Jean-Paul-Vincent / Beauharnois", "#9e2536", "#ffffff", R.drawable.bus)
                20u -> Line("21", "Grande-Allée / du Quartier", "#9e2536", "#ffffff", R.drawable.bus)
                23u -> Line("29", "Collectivité Nouvelle", "#9e2536", "#ffffff", R.drawable.bus)
                26u -> Line("32", "Secteur B Brossard / Mountainview", "#9e2536", "#ffffff", R.drawable.bus)
                28u -> Line("33", "Secteurs M-N-O Brossard", "#9e2536", "#ffffff", R.drawable.bus)
                29u -> Line("34", "Secteur A Brossard / Bellevue", "#9e2536", "#ffffff", R.drawable.bus)
                35u -> Line("42", "Gaétan-Boucher / Parc de la Cité", "#9e2536", "#ffffff", R.drawable.bus)
                44u -> Line("49", "Secteurs R-S Brossard", "#9e2536", "#ffffff", R.drawable.bus)
                320u -> Line("60", "Milan / Gaétan-Boucher / Promenades St-Bruno", "#9e2536", "#ffffff", R.drawable.bus)
                52u -> Line("71", "Curé-Poirier", "#9e2536", "#ffffff", R.drawable.bus)
                53u -> Line("73", "Joliette / de Lyon", "#9e2536", "#ffffff", R.drawable.bus)
                55u -> Line("75", "Quinn / Brébeuf", "#9e2536", "#ffffff", R.drawable.bus)
                56u -> Line("77", "Taschereau / Coteau-Rouge / Cégep É.-Montpetit", "#9e2536", "#ffffff", R.drawable.bus)
                57u -> Line("80", "De Montarville / Carrefour de la Rive-Sud", "#9e2536", "#ffffff", R.drawable.bus)
                61u -> Line("84", "Samuel-De Champlain / De Montarville", "#9e2536", "#ffffff", R.drawable.bus)
                65u -> Line("88", "Ch. Chambly / Mountainview", "#9e2536", "#ffffff", R.drawable.bus)
                147u -> Line("99", "Promenades St-Bruno / Saint-Bruno-de-Montarville", "#9e2536", "#ffffff", R.drawable.bus)
                70u -> Line("123", "Jacques-Cartier / Parcs industriels", "#9e2536", "#ffffff", R.drawable.bus)
                270u -> Line("160", "Milan / Gaétan-Boucher / Centre-ville St-Bruno", "#9e2536", "#ffffff", R.drawable.bus)
                340u -> Line("417", "Express Roland-Therrien / Roberval", "#9e2536", "#ffffff", R.drawable.bus)


                514u -> Line("3", "Secteur Laflèche", "#9e2536", "#ffffff", R.drawable.bus)
                515u -> Line("4", "Taschereau / Payer / DIX30", "#9e2536", "#ffffff", R.drawable.bus)
                519u -> Line("9", "Secteurs L-M St-Hubert", "#9e2536", "#ffffff", R.drawable.bus)
                520u -> Line("10", "Roland-Therrien / Belcourt", "#9e2536", "#ffffff", R.drawable.bus)
                529u -> Line("22", "Gare Longueuil - St-Hubert / Sect. B Vieux-Longueuil", "#9e2536", "#ffffff", R.drawable.bus)
                530u -> Line("23", "Ste-Hélène / Jacques-Cartier", "#9e2536", "#ffffff", R.drawable.bus)
                531u -> Line("25", "Parcs industriels Vieux-Longueuil / Boucherville", "#9e2536", "#ffffff", R.drawable.bus)
                532u -> Line("28", "Ch. Chambly / Savane / ENA", "#9e2536", "#ffffff", R.drawable.bus)
                534u -> Line("30", "Secteurs P-V Brossard", "#9e2536", "#ffffff", R.drawable.bus)
                535u -> Line("31", "Secteurs R-S-T Brossard / St-Laurent", "#9e2536", "#ffffff", R.drawable.bus)
                539u -> Line("37", "Simard / du Béarn", "#9e2536", "#ffffff", R.drawable.bus)
                540u -> Line("38", "Chevrier / Secteur B Brossard", "#9e2536", "#ffffff", R.drawable.bus)
                541u -> Line("41", "Rome / Milan", "#9e2536", "#ffffff", R.drawable.bus)
                543u -> Line("43", "Milan / Rome", "#9e2536", "#ffffff", R.drawable.bus)
                544u -> Line("44", "Secteurs M-N-O Brossard", "#9e2536", "#ffffff", R.drawable.bus)
                545u -> Line("46", "Secteurs R-S-T Brossard", "#9e2536", "#ffffff", R.drawable.bus)
                546u -> Line("47", "Secteurs R-S-T Brossard", "#9e2536", "#ffffff", R.drawable.bus)
                548u -> Line("50", "Bienville / Orchard / Prince-Charles", "#9e2536", "#ffffff", R.drawable.bus)
                549u -> Line("54", "Tiffin / St-Georges / Taschereau", "#9e2536", "#ffffff", R.drawable.bus)
                550u -> Line("55", "Victoria / Wellington", "#9e2536", "#ffffff", R.drawable.bus)
                551u -> Line("59", "Montgomery / Gareau", "#9e2536", "#ffffff", R.drawable.bus)
                553u -> Line("61", "Boucherville / Station Radisson", "#9e2536", "#ffffff", R.drawable.bus)
                556u -> Line("74", "St-Laurent / Secteur Bellerive", "#9e2536", "#ffffff", R.drawable.bus)
                558u -> Line("76", "Roland-Therrien / Roberval", "#9e2536", "#ffffff", R.drawable.bus)
                560u -> Line("78", "Adoncour / du Colisée", "#9e2536", "#ffffff", R.drawable.bus)
                562u -> Line("81", "du Fort-St-Louis / Marie-Victorin", "#9e2536", "#ffffff", R.drawable.bus)
                563u -> Line("82", "Marie-Victorin / du Fort-St-Louis", "#9e2536", "#ffffff", R.drawable.bus)
                564u -> Line("83", "De Montarville / Samuel-De Champlain", "#9e2536", "#ffffff", R.drawable.bus)
                566u -> Line("85", "Îles-Percées / de Mortagne / de Gascogne", "#9e2536", "#ffffff", R.drawable.bus)
                567u -> Line("86", "Samuel-De Champlain / De Montarville / TCV", "#9e2536", "#ffffff", R.drawable.bus)
                568u -> Line("87", "Marie-Victorin / du Fort St-Louis / TCV", "#9e2536", "#ffffff", R.drawable.bus)
                570u -> Line("93", "Gare St-Bruno / Montarville / Y.-Duckett", "#9e2536", "#ffffff", R.drawable.bus)
                571u -> Line("98", "Parc industriel St-Bruno / Parent", "#9e2536", "#ffffff", R.drawable.bus)
                573u -> Line("120", "Fernand-Lafontaine / Stationnement de Mortagne", "#9e2536", "#ffffff", R.drawable.bus)
                575u -> Line("125", "Pratt & Whitney / Lumenpulse", "#9e2536", "#ffffff", R.drawable.bus)
                576u -> Line("128", "Zone aéroportuaire / Parc industriel St-Bruno", "#9e2536", "#ffffff", R.drawable.bus)
                577u -> Line("132", "DIX30 / Parc de la Cité / Mountainview", "#9e2536", "#ffffff", R.drawable.bus)
                579u -> Line("161", "R.-Therrien / J.-Cartier / De Montarville", "#9e2536", "#ffffff", R.drawable.bus)
                580u -> Line("170", "Station Papineau / Ste-Hélène / J.-Cartier", "#9e2536", "#ffffff", R.drawable.bus)
                581u -> Line("180", "De Montarville / des Sureaux", "#9e2536", "#ffffff", R.drawable.bus)
                582u -> Line("185", "Ampère / Gay-Lussac", "#9e2536", "#ffffff", R.drawable.bus)
                583u -> Line("192", "Station Brossard / Montarville / Y.-Duckett", "#9e2536", "#ffffff", R.drawable.bus)
                584u -> Line("199", "Seigneurial / Grand Boulevard", "#9e2536", "#ffffff", R.drawable.bus)
                585u -> Line("214", "Station Brossard / de Rome / St-Laurent", "#9e2536", "#ffffff", R.drawable.bus)
                586u -> Line("284", "Navette Boucherville", "#9e2536", "#ffffff", R.drawable.bus)
                587u -> Line("410", "Express Roland-Therrien / Belcourt", "#9e2536", "#ffffff", R.drawable.bus)
                589u -> Line("421", "Grande-Allée / A.-Frappier", "#9e2536", "#ffffff", R.drawable.bus)
                590u -> Line("428", "Zone aéroportuaire / Agence Spatiale canadienne", "#9e2536", "#ffffff", R.drawable.bus)
                591u -> Line("442", "Cousineau / Pacific", "#9e2536", "#ffffff", R.drawable.bus)
                592u -> Line("461", "Express de Touraine / de Montarville / Radisson", "#9e2536", "#ffffff", R.drawable.bus)
                593u -> Line("462", "Touraine-Mortagne-Radisson-HMR-Inst.cardio", "#9e2536", "#ffffff", R.drawable.bus)

                594u -> Line("500", "École Saint-Lambert International High School", "#9e2536", "#ffffff", R.drawable.bus)
                666u -> Line("501", "École privées", "#9e2536", "#ffffff", R.drawable.bus)
                595u -> Line("503", "École Centennial Regional High School", "#9e2536", "#ffffff", R.drawable.bus)
                596u -> Line("505", "École Centennial Regional High School", "#9e2536", "#ffffff", R.drawable.bus)
                597u -> Line("521", "École Centennial Regional High School", "#9e2536", "#ffffff", R.drawable.bus)
                598u -> Line("524", "École Centennial Regional High School", "#9e2536", "#ffffff", R.drawable.bus)
                599u -> Line("525", "École Centennial Regional High School", "#9e2536", "#ffffff", R.drawable.bus)
                600u -> Line("526", "École Centennial Regional High School", "#9e2536", "#ffffff", R.drawable.bus)
                601u -> Line("530", "École Centennial Regional High School", "#9e2536", "#ffffff", R.drawable.bus)
                602u -> Line("532", "École Centennial Regional High School", "#9e2536", "#ffffff", R.drawable.bus)
                603u -> Line("533", "École Centennial Regional High School", "#9e2536", "#ffffff", R.drawable.bus)
                604u -> Line("534", "École Centennial Regional High School", "#9e2536", "#ffffff", R.drawable.bus)
                605u -> Line("535", "École Centennial Regional High School", "#9e2536", "#ffffff", R.drawable.bus)
                606u -> Line("536", "École Centennial Regional High School", "#9e2536", "#ffffff", R.drawable.bus)
                607u -> Line("538", "École Heritage Regional High School", "#9e2536", "#ffffff", R.drawable.bus)
                608u -> Line("539", "École Heritage Regional High School", "#9e2536", "#ffffff", R.drawable.bus)
                609u -> Line("540", "École Heritage Regional High School", "#9e2536", "#ffffff", R.drawable.bus)
                610u -> Line("542", "École Heritage Regional High School", "#9e2536", "#ffffff", R.drawable.bus)
                611u -> Line("544", "École Centennial Regional High School", "#9e2536", "#ffffff", R.drawable.bus)
                612u -> Line("546", "École Centennial Regional High School", "#9e2536", "#ffffff", R.drawable.bus)
                613u -> Line("547", "École Centennial Regional High School", "#9e2536", "#ffffff", R.drawable.bus)
                614u -> Line("549", "École Centennial Regional High School", "#9e2536", "#ffffff", R.drawable.bus)
                615u -> Line("550", "École Centennial Regional High School", "#9e2536", "#ffffff", R.drawable.bus)
                616u -> Line("551", "École Centennial Regional High School", "#9e2536", "#ffffff", R.drawable.bus)
                667u -> Line("554", "École privées", "#9e2536", "#ffffff", R.drawable.bus)
                617u -> Line("565", "École Saint-Lambert International High School", "#9e2536", "#ffffff", R.drawable.bus)
                618u -> Line("570", "École Saint-Lambert International High School", "#9e2536", "#ffffff", R.drawable.bus)
                619u -> Line("572", "École Saint-Lambert International High School", "#9e2536", "#ffffff", R.drawable.bus)
                620u -> Line("575", "École Saint-Lambert International High School", "#9e2536", "#ffffff", R.drawable.bus)
                621u -> Line("580", "École Saint-Lambert International High School", "#9e2536", "#ffffff", R.drawable.bus)
                622u -> Line("600", "Écoles privées", "#9e2536", "#ffffff", R.drawable.bus)
                623u -> Line("601", "École de l'Agora", "#9e2536", "#ffffff", R.drawable.bus)
                624u -> Line("602", "École de l'Agora", "#9e2536", "#ffffff", R.drawable.bus)
                625u -> Line("604", "École de l'Agora", "#9e2536", "#ffffff", R.drawable.bus)
                626u -> Line("605", "École de l'Agora", "#9e2536", "#ffffff", R.drawable.bus)
                627u -> Line("606", "Écoles privées / Collège Champlain", "#9e2536", "#ffffff", R.drawable.bus)
                628u -> Line("607", "École de l'Agora", "#9e2536", "#ffffff", R.drawable.bus)
                629u -> Line("608", "École André-Laurendeau et Cégep Édouard-Montpetit", "#9e2536", "#ffffff", R.drawable.bus)
                630u -> Line("609", "École de l'Agora", "#9e2536", "#ffffff", R.drawable.bus)
                631u -> Line("613", "Écoles privées / Collège Champlain", "#9e2536", "#ffffff", R.drawable.bus)
                632u -> Line("614", "Écoles privées / Collège Champlain", "#9e2536", "#ffffff", R.drawable.bus)
                633u -> Line("616", "École Gérard-Filion", "#9e2536", "#ffffff", R.drawable.bus)
                634u -> Line("618", "Écoles privées / Collège Champlain", "#9e2536", "#ffffff", R.drawable.bus)
                635u -> Line("620", "Collège Français", "#9e2536", "#ffffff", R.drawable.bus)
                636u -> Line("624", "École St-Jean-Baptiste", "#9e2536", "#ffffff", R.drawable.bus)
                637u -> Line("640", "École Antoine-Brossard", "#9e2536", "#ffffff", R.drawable.bus)
                638u -> Line("644", "École Antoine-Brossard", "#9e2536", "#ffffff", R.drawable.bus)
                639u -> Line("650", "École Antoine-Brossard", "#9e2536", "#ffffff", R.drawable.bus)
                640u -> Line("653", "École Antoine-Brossard", "#9e2536", "#ffffff", R.drawable.bus)
                641u -> Line("656", "École Antoine-Brossard", "#9e2536", "#ffffff", R.drawable.bus)
                642u -> Line("660", "École Antoine-Brossard", "#9e2536", "#ffffff", R.drawable.bus)
                643u -> Line("664", "École Antoine-Brossard", "#9e2536", "#ffffff", R.drawable.bus)
                644u -> Line("665", "École Antoine-Brossard", "#9e2536", "#ffffff", R.drawable.bus)
                645u -> Line("670", "École Antoine-Brossard", "#9e2536", "#ffffff", R.drawable.bus)
                646u -> Line("671", "École Jacques-Rousseau", "#9e2536", "#ffffff", R.drawable.bus)
                647u -> Line("672", "École Jacques-Rousseau", "#9e2536", "#ffffff", R.drawable.bus)
                648u -> Line("673", "École Jacques-Rousseau", "#9e2536", "#ffffff", R.drawable.bus)
                649u -> Line("674", "École Antoine-Brossard", "#9e2536", "#ffffff", R.drawable.bus)
                650u -> Line("675", "École Jacques-Rousseau", "#9e2536", "#ffffff", R.drawable.bus)
                651u -> Line("676", "École Jacques-Rousseau", "#9e2536", "#ffffff", R.drawable.bus)
                652u -> Line("678", "École Jacques-Rousseau", "#9e2536", "#ffffff", R.drawable.bus)
                653u -> Line("683", "Cégep Édouard-Montpetit", "#9e2536", "#ffffff", R.drawable.bus)
                654u -> Line("684", "École Antoine-Brossard", "#9e2536", "#ffffff", R.drawable.bus)
                655u -> Line("689", "École Antoine-Brossard", "#9e2536", "#ffffff", R.drawable.bus)
                656u -> Line("690", "École Antoine-Brossard", "#9e2536", "#ffffff", R.drawable.bus)
                657u -> Line("691", "École Lucille-Teasdale", "#9e2536", "#ffffff", R.drawable.bus)
                658u -> Line("692", "École Lucille-Teasdale", "#9e2536", "#ffffff", R.drawable.bus)
                659u -> Line("693", "École Lucille-Teasdale", "#9e2536", "#ffffff", R.drawable.bus)
                660u -> Line("694", "École Lucille-Teasdale", "#9e2536", "#ffffff", R.drawable.bus)
                661u -> Line("695", "École Lucille-Teasdale", "#9e2536", "#ffffff", R.drawable.bus)
                662u -> Line("696", "École Lucille-Teasdale", "#9e2536", "#ffffff", R.drawable.bus)
                663u -> Line("697", "École Lucille-Teasdale - Secteur Chevrier", "#9e2536", "#ffffff", R.drawable.bus)

                664u -> Line("T48", "Brossard M.-Victorin / St-Laurent", "#9e2536", "#ffffff", R.drawable.bus)
                665u -> Line("T89", "Parc ind. Boucherville / Eiffel", "#9e2536", "#ffffff", R.drawable.bus)

                else -> {
                    val proposition: Line? = lookForLineProposition(
                        context,
                        operatorId.toString(),
                        id.toString(),
                        R.drawable.bus
                    )

                    return proposition ?: Line("?", "RTL ($id)", "#9e2536", "#ffffff", R.drawable.bus)
                }
            }
        }

        private fun getEXOLineById(context: Context, operatorId: UInt, id: UInt): Line {
            // TODO Find ids of all lines
            //   (Id of the line is on 9bits so higher
            //   than 512u means it's not known yet)
            return when (id) {
                2u -> Line("11", "Vaudreuil-Hudson", "#f16179", "#000000", R.drawable.train)
                3u -> Line("12", "Saint-Jérôme", "#fed16d", "#000000", R.drawable.train)
                4u -> Line("13", "Mont-Saint-Hilaire", "#999ac6", "#000000", R.drawable.train)
                5u -> Line("14", "Candiac", "#5ab6b2", "#000000", R.drawable.train)
                7u -> Line("15", "Mascouche", "#ca5898", "#000000", R.drawable.train)

                else -> {
                    val proposition: Line? = lookForLineProposition(
                        context,
                        operatorId.toString(),
                        id.toString(),
                        R.drawable.train
                    )

                    return proposition ?: Line("?", "exo ($id)", "#000000", "#ffffff", R.drawable.train)
                }
            }
        }

        private fun getEXOLaurentidesLineById(context: Context, operatorId: UInt, id: UInt): Line {
            // TODO Find ids of all lines
            //   (Id of the line is on 9bits so higher
            //   than 512u means it's not known yet)
            return when (id) {
                8u -> Line("8", "Saint-Eustache - Laval (métro Montmorency)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                9u -> Line("9", "Saint-Jérôme / Laval", "#000000", "#ffffff", R.drawable.bus)
                11u -> Line("11", "Laval / Rosemère / Sainte-Thérèse", "#000000", "#ffffff", R.drawable.bus)
                12u -> Line("12", "Direction Bois-des-Filion", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                14u -> Line("15", "Rosemère - Gare Rosemère", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                16u -> Line("17", "Terrebonne - Gare Rosemère", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                19u -> Line("20", "Terrebonne ouest - Gare Rosemère", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                21u -> Line("22", "Bois-des-Filion - Sainte-Thérèse", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                22u -> Line("23", "Sainte-Anne-des-Plaines - Sainte-Thérèse", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                23u -> Line("24", "Sainte-Anne-des-Plaines - Laval (métro Cartier)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                24u -> Line("27", "Lorraine - Sainte-Thérèse", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                26u -> Line("51", "Boisbriand Nord vers Boisbriand Sud", "#000000", "#ffffff", R.drawable.bus)
                27u -> Line("52", "Boisbriand Sud vers Boisbriand Nord", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                28u -> Line("60", "Sainte-Thérèse", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                29u -> Line("61", "Sainte-Thérèse - secteur Bas Sainte-Thérèse", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                30u -> Line("62", "Sainte-Thérèse - secteur des Mille-Îles", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                31u -> Line("80", "Saint-Eustache - Pointe-Calumet", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                32u -> Line("88", "Saint-Eustache / Sainte-Thérèse", "#000000", "#ffffff", R.drawable.bus)
                33u -> Line("89", "Saint-Eustache - secteur Dubois", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                34u -> Line("90", "Saint-Eustache - Arthur-Sauvé et Industriel", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                35u -> Line("91", "Saint-Eustache - secteur est (Saint-Laurent)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                36u -> Line("92", "Saint-Eustache - secteur centre", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                37u -> Line("93", "Saint-Eustache - Deux-Montagnes", "#1F1F1F", "#FFFFFF", R.drawable.bus)

                521u -> Line("28", "Terrebonne - Laval (métro Cartier)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                523u -> Line("59", "Boisbriand - Laval (métro Montmorency)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                527u -> Line("71", "Blainville - secteur est", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                528u -> Line("72", "Blainville - secteur ouest", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                529u -> Line("73", "Blainville - secteur Fontainebleau", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                530u -> Line("74", "Blainville - secteur Fontainebleau", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                532u -> Line("81", "Saint-Eustache - Pointe-Calumet", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                538u -> Line("100", "Saint-Jérôme - secteur Bellefeuille sud", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                539u -> Line("101", "Saint-Jérôme - secteur Saint-Antoine", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                540u -> Line("102", "Saint-Jérôme - secteur Lafontaine", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                541u -> Line("103", "Saint-Jérôme - secteur Centre", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                542u -> Line("105", "Saint-Jérôme - secteur Bellefeuille nord", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                543u -> Line("107", "Saint-Jérôme - Bellefeuille sud (Lajeunesse)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                544u -> Line("243", "Saint-Augustin - Sainte-Thérèse", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                545u -> Line("404", "Service Express Centre-Ville / Deux-Montagnes", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                546u -> Line("496", "Navette Deux-Montagnes - Sunnybrooke", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                547u -> Line("498", "St-Eustache - Ste-Dorothée - Centre-ville", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                548u -> Line("499", "Service Express Côte-Vertu / Deux-Montagnes", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                549u -> Line("744", "Express d'Oka", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                550u -> Line("904", "Gare Deux-Montagnes - Grand-Moulin", "#1F1F1F", "#FFFFFF", R.drawable.bus)

                551u -> Line("T12", "Bois-des-Filion - Gare Rosemère (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                552u -> Line("T15", "Rosemère - Gare Rosemère (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                553u -> Line("T17", "Terrebonne - Gare Rosemère (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                554u -> Line("T20", "Terrebonne ouest - Gare Rosemère (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                555u -> Line("T21", "Taxi collectif - Établissement Archambault", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                556u -> Line("T23", "Ste-Anne-des-Plaines - Ste-Thérèse (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                557u -> Line("T25", "Ste-Anne-des-Plaines -Terrebonne (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                558u -> Line("T53", "Boisbriand S vers Boisbriand N (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                559u -> Line("T60", "Sainte-Thérèse (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                560u -> Line("T71", "Blainville - secteur est (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                561u -> Line("T72", "Blainville - secteur ouest (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                562u -> Line("T74", "Blainville - Fontainebleau (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                563u -> Line("T100", "Saint-Jérôme - Bellefeuille sud (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                564u -> Line("T101", "Saint-Jérôme - Saint-Antoine (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                565u -> Line("T102", "Saint-Jérôme - Lafontaine (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                566u -> Line("T103", "Saint-Jérôme - Centre (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                567u -> Line("T105", "Saint-Jérôme - Bellefeuille nord (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                568u -> Line("T107", "Saint-Jérôme - Bellefeuille sud (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                569u -> Line("T904", "Gare Deux-Montagnes - Grand-Moulin", "#1F1F1F", "#FFFFFF", R.drawable.bus)

                570u -> Line("YMX", "YMX Express", "#1F1F1F", "#FFFFFF", R.drawable.bus)

                else -> {
                    val proposition: Line? = lookForLineProposition(
                        context,
                        operatorId.toString(),
                        id.toString(),
                        R.drawable.bus
                    )

                    return proposition ?: Line("?", "exo Laurentides ($id)", "#000000", "#ffffff", R.drawable.bus)
                }
            }
        }

        private fun getEXOTerrebonneMascoucheLineById(context: Context, operatorId: UInt, id: UInt): Line {
            // TODO Find ids of all lines
            //   (Id of the line is on 9bits so higher
            //   than 512u means it's not known yet)
            return when (id) {
                1u -> Line("1", "Terrebonne - Mascouche", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                2u -> Line("2", "Terrebonne - Mascouche", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                3u -> Line("3", "Terrebonne - Mascouche", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                4u -> Line("5", "Terrebonne - Bois-des-Filion", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                6u -> Line("8", "Terrebonne - secteur centre", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                7u -> Line("9", "Terrebonne - secteur ouest", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                8u -> Line("11", "Terrebonne - Lachenaie", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                9u -> Line("14", "Terrebonne - La Plaine", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                12u -> Line("17", "Terrebonne - La Plaine", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                13u -> Line("18", "Terrebonne - Cité du sport - Cégep de Terrebonne", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                14u -> Line("19", "Terrebonne / Terminus Montmorency", "#000000", "#ffffff", R.drawable.bus)
                15u -> Line("20", "Terrebonne - Mascouche", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                16u -> Line("21", "Terrebonne - Mascouche", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                17u -> Line("23", "Terrebonne - Sainte-Thérèse (Cégep Lionel-Groulx)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                19u -> Line("25", "Terrebonne - Montréal", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                21u -> Line("40", "Lachenaie - Montréal - Terminus Radisson", "#1F1F1F", "#FFFFFF", R.drawable.bus)

                515u -> Line("4", "Terrebonne - Mascouche", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                520u -> Line("11C", "Terrebonne - Cité du sport - Lachenaie", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                526u -> Line("22", "Mascouche - Cité du sport - Cégep de Terrebonne", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                528u -> Line("24C", "Terrebonne - Cité du sport - Cégep de Terrebonne", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                530u -> Line("25B", "Terrebonne - Montréal", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                531u -> Line("27", "Terrebonne - Cité du Sport - Cégep de Terrebonne", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                532u -> Line("30", "Gare Mascouche - Terrebonne - Terminus Radisson", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                534u -> Line("41", "Terrebonne - Mascouche", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                535u -> Line("45", "Terrebonne - Bois-des-Filion", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                536u -> Line("48", "Terrebonne - secteur centre", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                537u -> Line("140", "Lachenaie - Gare de Terrebonne - Terminus Radisson", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                538u -> Line("417", "La Plaine - Gare Mascouche", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                539u -> Line("T6", "Taxibus - Alpes / des Rocheuses (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                540u -> Line("T12", "Taxibus - Cégep de Terrebonne (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                541u -> Line("T13", "Taxibus - Cégep de Terrebonne (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                542u -> Line("T15", "Taxibus - Cégep de Terrebonne (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                543u -> Line("T24", "Taxibus - Place Longchamps (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                544u -> Line("T26", "Taxibus - Nord de Mascouche (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                545u -> Line("T55", "Taxibus - Urbanova (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                546u -> Line("T56", "Taxibus - Parc Industriel (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                547u -> Line("T57", "Taxibus - Gascon / Alexander (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)
                548u -> Line("T417", "Terrebonne - Gare Mascouche (sur réservation)", "#1F1F1F", "#FFFFFF", R.drawable.bus)

                else -> {
                    val proposition: Line? = lookForLineProposition(
                        context,
                        operatorId.toString(),
                        id.toString(),
                        R.drawable.bus
                    )

                    return proposition ?: Line("?", "exo Terrebonne-Mascouche ($id)", "#000000", "#ffffff", R.drawable.bus)
                }
            }
        }

        private fun getRTCLineById(context: Context, operatorId: UInt, id: UInt): Line {
            // TODO Find ids of all lines
            //   (Id of the line is on 9bits so higher
            //   than 512u means it's not known yet)
            return when (id) {
                1u -> Line("1", "Station Belvédère - Gare fluviale / Cap-Blanc", "#013888", "#FFFFFF", R.drawable.bus)
                2u -> Line("3", "Grand Théâtre - 41e Rue Est / Jean-Talon Ouest", "#013888", "#FFFFFF", R.drawable.bus)
                3u -> Line("4", "Place Jacques-Cartier - Maizerets", "#013888", "#FFFFFF", R.drawable.bus)
                6u -> Line("9", "Parcs Jean-Talon Nord et Duberger - Place Jacques-Cartier", "#013888", "#FFFFFF", R.drawable.bus)
                7u -> Line("11", "Gare fluviale - Pointe-de-Sainte-Foy", "#013888", "#FFFFFF", R.drawable.bus)
                9u -> Line("13", "Saint-Louis/Sainte-Foy Centre/du Versant-Nord - Du Versant-Nord/Sainte-Foy Centre/Saint-Louis", "#013888", "#FFFFFF", R.drawable.bus)
                11u -> Line("16", "Sillery / U. Laval / Sainte-Foy Centre - U. Laval / Sillery / Sainte-Foy Centre", "#013888", "#FFFFFF", R.drawable.bus)
                12u -> Line("18", "Gare du Palais - Université Laval", "#013888", "#FFFFFF", R.drawable.bus)
                316u -> Line("19", "Grand Théâtre - Limoilou", "#013888", "#FFFFFF", R.drawable.bus)
                13u -> Line("22", "Sainte-Foy Centre - Terminus Les Saules", "#013888", "#FFFFFF", R.drawable.bus)
                15u -> Line("25", "Place D'Youville / Gare du Palais - Pointe-de-Sainte-Foy", "#013888", "#FFFFFF", R.drawable.bus)
                16u -> Line("28", "Colline Parlementaire - 51e Rue Est", "#013888", "#FFFFFF", R.drawable.bus)
                17u -> Line("29", "Parc Colbert Est - Place Jacques-Cartier", "#013888", "#FFFFFF", R.drawable.bus)
                18u -> Line("31", "Terminus de la Faune - Lac-Saint-Charles", "#013888", "#FFFFFF", R.drawable.bus)
                19u -> Line("32", "Terminus de la Faune - Lac Clément", "#013888", "#FFFFFF", R.drawable.bus)
                20u -> Line("33", "Parc industriel Charlesbourg - Terminus de la Faune", "#013888", "#FFFFFF", R.drawable.bus)
                21u -> Line("34", "Terminus de la Faune - Des Laurentides", "#013888", "#FFFFFF", R.drawable.bus)
                22u -> Line("36", "Place Jacques-Cartier - Station des Roses", "#013888", "#FFFFFF", R.drawable.bus)
                25u -> Line("39", "41e Rue Est - Terminus de la Faune", "#013888", "#FFFFFF", R.drawable.bus)
                321u -> Line("51", "Parc O-Bus Sainte-Anne - Terminus Beauport", "#013888", "#FFFFFF", R.drawable.bus)
                27u -> Line("52", "Maizerets - Terminus Charlesbourg", "#013888", "#FFFFFF", R.drawable.bus)
                28u -> Line("53", "Courville - Terminus Beauport", "#013888", "#FFFFFF", R.drawable.bus)
                29u -> Line("54", "Terminus Beauport / Vieux-Québec - Sainte-Thérèse-de-Lisieux", "#013888", "#FFFFFF", R.drawable.bus)
                30u -> Line("55", "Terminus Beauport - Sainte-Thérèse-de-Lisieux", "#013888", "#FFFFFF", R.drawable.bus)
                31u -> Line("57", "Sainte-Thérèse-de-Lisieux - Terminus Beauport", "#013888", "#FFFFFF", R.drawable.bus)
                32u -> Line("58", "Terminus Beauport - Sainte-Thérèse-de-Lisieux", "#013888", "#FFFFFF", R.drawable.bus)
                33u -> Line("59", "Terminus Charlesbourg - Terminus Chute-Montmorency", "#013888", "#FFFFFF", R.drawable.bus)
                171u -> Line("61", "Maizerets - Lebourgneuf", "#013888", "#FFFFFF", R.drawable.bus)
                161u -> Line("64", "Place Jacques-Cartier - Lebourgneuf", "#013888", "#FFFFFF", R.drawable.bus)
                162u -> Line("65", "Place  J.-Cartier / colline Parlementaire - Lebourgneuf", "#013888", "#FFFFFF", R.drawable.bus)
                35u -> Line("70", "Sainte-Foy Centre - Val-Bélair", "#013888", "#FFFFFF", R.drawable.bus)
                36u -> Line("72", "Terminus Charlesbourg - Loretteville", "#013888", "#FFFFFF", R.drawable.bus)
                37u -> Line("74", "Place Jacques-Cartier - Val-Bélair", "#013888", "#FFFFFF", R.drawable.bus)
                38u -> Line("75", "Sainte-Foy Centre - Val-Bélair", "#013888", "#FFFFFF", R.drawable.bus)
                39u -> Line("77", "Terminus Les Saules - Val-Bélair", "#013888", "#FFFFFF", R.drawable.bus)
                40u -> Line("79", "Terminus Les Saules / Belvédère - Chauveau", "#013888", "#FFFFFF", R.drawable.bus)
                41u -> Line("80", "Place Jacques-Cartier - Aéroport international Jean-Lesage", "#013888", "#FFFFFF", R.drawable.bus)
                42u -> Line("81", "Terminus Les Saules - Terminus de la Faune", "#013888", "#FFFFFF", R.drawable.bus)
                43u -> Line("82", "Place Jacques-Cartier - Lac-Saint-Charles", "#013888", "#FFFFFF", R.drawable.bus)
                44u -> Line("84", "Place Jacques-Cartier - Val-Bélair", "#013888", "#FFFFFF", R.drawable.bus)
                45u -> Line("85", "Place Jacques-Cartier - Lebourgneuf", "#013888", "#FFFFFF", R.drawable.bus)
                46u -> Line("86", "Place J.-Cartier / colline Parlementaire - Terminus Les Saules", "#013888", "#FFFFFF", R.drawable.bus)
                47u -> Line("88", "Sainte-Foy Centre - Terminus Les Saules", "#013888", "#FFFFFF", R.drawable.bus)
                48u -> Line("92", "Cégep Garneau - Saint-Augustin", "#013888", "#FFFFFF", R.drawable.bus)
                49u -> Line("93", "Sainte-Foy Centre/Univ.Laval/Cégep Garneau - Champigny", "#013888", "#FFFFFF", R.drawable.bus)
                53u -> Line("133", "Vieux-Québec - Terminus de la Faune", "#013888", "#FFFFFF", R.drawable.bus)
                54u -> Line("136", "Colline Parlementaire - Montagne-des-Roches", "#013888", "#FFFFFF", R.drawable.bus)
                55u -> Line("185", "Station Belvédère / Sainte-Foy Centre - Neufchâtel", "#013888", "#FFFFFF", R.drawable.bus)
                56u -> Line("214", "Gare du Palais - Cap-Rouge", "#E04503", "#000000", R.drawable.bus)
                57u -> Line("215", "Gare du Palais - Cap-Rouge", "#E04503", "#000000", R.drawable.bus)
                58u -> Line("230", "Colline Parlementaire - Terminus de la Faune", "#E04503", "#000000", R.drawable.bus)
                59u -> Line("236", "Colline Parlementaire - Station des Roses", "#E04503", "#000000", R.drawable.bus)
                60u -> Line("238", "Colline Parlementaire - Guillaume-Mathieu", "#E04503", "#000000", R.drawable.bus)
                61u -> Line("239", "Colline Parlementaire - Terminus de la Faune", "#E04503", "#000000", R.drawable.bus)
                62u -> Line("250", "Colline Parlementaire - Terminus Chute-Montmorency", "#E04503", "#000000", R.drawable.bus)
                63u -> Line("251", "Colline Parlementaire - Parc-O-Bus Sainte-Anne", "#E04503", "#000000", R.drawable.bus)
                64u -> Line("254", "Colline Parlementaire - Sainte-Thérèse-de-Lisieux", "#E04503", "#000000", R.drawable.bus)
                65u -> Line("255", "Colline Parlementaire - Sainte-Thérèse-de-Lisieux", "#E04503", "#000000", R.drawable.bus)
                67u -> Line("272", "Colline Parlementaire - Loretteville", "#E04503", "#000000", R.drawable.bus)
                168u -> Line("289", "Colline Parlementaire - Saint-Émile", "#E04503", "#000000", R.drawable.bus)
                148u -> Line("294", "Terminus Gare-du-Palais - Saint-Augustin", "#E04503", "#000000", R.drawable.bus)
                78u -> Line("330", "Univ.Laval/Cégep Garneau - Terminus Charlesbourg", "#E04503", "#000000", R.drawable.bus)
                79u -> Line("331", "Pointe-de-Sainte-Foy - Terminus de la Faune", "#E04503", "#000000", R.drawable.bus)
                80u -> Line("332", "Saint-Rodrigue - Sainte-Foy Centre", "#E04503", "#000000", R.drawable.bus)
                81u -> Line("336", "Univ.Laval/Cégep Garneau - Station des Roses", "#E04503", "#000000", R.drawable.bus)
                82u -> Line("337", "Univ.Laval/Cégep Garneau - Terminus de la Faune", "#E04503", "#000000", R.drawable.bus)
                83u -> Line("338", "Univ.Laval/Cégep Garneau - Montagne-des-Roches", "#E04503", "#000000", R.drawable.bus)
                84u -> Line("350", "Univ.Laval/Cégep Garneau - Terminus Chute-Montmorency", "#E04503", "#000000", R.drawable.bus)
                90u -> Line("382", "Univ.Laval/Cégep Garneau - Saint-Émile", "#E04503", "#000000", R.drawable.bus)
                91u -> Line("384", "Univ.Laval/Cégep Garneau - Loretteville", "#E04503", "#000000", R.drawable.bus)
                94u -> Line("391", "Pointe-de-Sainte-Foy - Terminus Les Saules", "#E04503", "#000000", R.drawable.bus)
                95u -> Line("800", "Terminus Chute-Montmorency - Pointe-de-Sainte-Foy", "#97BF0D", "#000000", R.drawable.bus)
                96u -> Line("801", "Terminus de la Faune - Pointe-de-Sainte-Foy", "#97BF0D", "#000000", R.drawable.bus)
                164u -> Line("802", "Terminus Beauport - Station Belvédère", "#97BF0D", "#000000", R.drawable.bus)
                172u -> Line("803", "Terminus Beauport - Terminus Les Saules", "#97BF0D", "#000000", R.drawable.bus)
                318u -> Line("804", "Sainte-Foy Centre - Loretteville", "#97BF0D", "#000000", R.drawable.bus)
                317u -> Line("807", "Place D'Youville - Pointe-de-Sainte-Foy", "#97BF0D", "#000000", R.drawable.bus)
                208u -> Line("950", "Terminus Chute-Montmorency -", "#1A171B", "#FFFFFF", R.drawable.bus)

                512u -> Line("3A","Saint-Jean-Eudes - Des Lilas Ouest / du Colisée", "#013888", "#FFFFFF", R.drawable.bus)
                513u -> Line("11G","C.-Laforte / McCartney", "#013888", "#FFFFFF", R.drawable.bus)
                514u -> Line("13A","École secondaire De Rochebelle - De Terrebonne / de La Suète", "#013888", "#FFFFFF", R.drawable.bus)
                515u -> Line("13B","École secondaire De Rochebelle - Des Sapins / du Versant-Nord", "#013888", "#FFFFFF", R.drawable.bus)
                516u -> Line("14","Pointe-de-Sainte-Foy - Cap-Rouge", "#013888", "#FFFFFF", R.drawable.bus)
                517u -> Line("14A","Collège des Compagnons - De la Rivière / du Domaine", "#013888", "#FFFFFF", R.drawable.bus)
                518u -> Line("14G","Collège Jésus-Marie de Sillery - Des Carougeois / J.-C.-Cantin", "#013888", "#FFFFFF", R.drawable.bus)
                519u -> Line("15","Pointe-de-Sainte-Foy - Cap-Rouge", "#013888", "#FFFFFF", R.drawable.bus)
                510u -> Line("15A","Collège des Compagnons - de la Prom.-des-Soeurs / J.-C.-Cantin", "#013888", "#FFFFFF", R.drawable.bus)
                511u -> Line("15B","Collège des Compagnons - de la Prom-des-Soeurs / Escoffier", "#013888", "#FFFFFF", R.drawable.bus)
                522u -> Line("25G","Pointe-de-Sainte-Foy", "#013888", "#FFFFFF", R.drawable.bus)
                523u -> Line("54A","Académie Sainte-Marie - du Rang-Saint-Ignace / Forain", "#013888", "#FFFFFF", R.drawable.bus)
                524u -> Line("54G","École secondaire de la Seigneurie - des Sablonnières / Berrouard", "#013888", "#FFFFFF", R.drawable.bus)
                525u -> Line("55A","Académie Sainte-Marie - Raymond / L.-Chardon", "#013888", "#FFFFFF", R.drawable.bus)
                526u -> Line("55G","École secondaire de la Seigneurie - du Rang-Saint-Ignace / Forain", "#013888", "#FFFFFF", R.drawable.bus)
                527u -> Line("56","Parc industriel de Beauport - Terminus Beauport", "#013888", "#FFFFFF", R.drawable.bus)
                528u -> Line("76","Gare de train de Sainte-Foy - Aéroport international Jean-Lesage", "#013888", "#FFFFFF", R.drawable.bus)
                529u -> Line("79B","École sec. Polyvalente de L'Ancienne-Lorette - Saint-Jean-Baptiste / Barrès", "#013888", "#FFFFFF", R.drawable.bus)
                530u -> Line("79G","École secondaire La Camaradière - Saint-Paul / Saint-Olivier", "#013888", "#FFFFFF", R.drawable.bus)
                531u -> Line("80A","École sec. Polyvalente de L'Ancienne-Lorette - Michelet / Saint-Jean-Baptiste", "#013888", "#FFFFFF", R.drawable.bus)
                532u -> Line("80G","École secondaire La Camaradière - O'Neil / W.-Hamel", "#013888", "#FFFFFF", R.drawable.bus)
                533u -> Line("82A","Saint-Jean-Eudes - Samson / Baker", "#013888", "#FFFFFF", R.drawable.bus)
                534u -> Line("82D","École secondaire du Phare - Du Lac-Saint-Charles / Delage", "#013888", "#FFFFFF", R.drawable.bus)
                535u -> Line("82M","École secondaire du Phare - Lac-Saint-Charles", "#013888", "#FFFFFF", R.drawable.bus)
                536u -> Line("84G","École secondaire La Camaradière - Du Costebelle / de Pertuis", "#013888", "#FFFFFF", R.drawable.bus)
                537u -> Line("86G","École secondaire La Camaradière - Père-Lelièvre / Godin", "#013888", "#FFFFFF", R.drawable.bus)
                538u -> Line("92A","Campus Notre-Dame-de-Foy - Terminus Charlesbourg", "#013888", "#FFFFFF", R.drawable.bus)
                539u -> Line("94","Cégep Garneau - Saint-Augustin", "#013888", "#FFFFFF", R.drawable.bus)
                540u -> Line("107","Gare du Palais - Pointe-de-Sainte-Foy", "#013888", "#FFFFFF", R.drawable.bus)

                541u -> Line("253","Colline Parlementaire - Courville", "#E04503", "#000000", R.drawable.bus)
                542u -> Line("257","Colline Parlementaire - Sainte-Thérèse-de-Lisieux", "#E04503", "#000000", R.drawable.bus)
                543u -> Line("258","Colline Parlementaire - Sainte-Thérèse-de-Lisieux", "#E04503", "#000000", R.drawable.bus)
                544u -> Line("273","Colline Parlementaire - Loretteville Sud", "#E04503", "#000000", R.drawable.bus)
                545u -> Line("274","Colline Parlementaire - Neufchâtel", "#E04503", "#000000", R.drawable.bus)
                546u -> Line("277","Colline Parlementaire - Val-Bélair", "#E04503", "#000000", R.drawable.bus)
                547u -> Line("279","Colline Parlementaire - L'Ancienne-Lorette", "#E04503", "#000000", R.drawable.bus)
                548u -> Line("280","Place Jacques-Cartier / colline Parlementaire - L'Ancienne-Lorette", "#E04503", "#000000", R.drawable.bus)
                549u -> Line("281","Colline Parlementaire - Neufchâtel", "#E04503", "#000000", R.drawable.bus)
                550u -> Line("282","Colline Parlementaire - Lac-Saint-Charles", "#E04503", "#000000", R.drawable.bus)
                551u -> Line("283","Gare du Palais - Champigny", "#E04503", "#000000", R.drawable.bus)
                552u -> Line("284","Colline Parlementaire - Loretteville", "#E04503", "#000000", R.drawable.bus)
                553u -> Line("290","Vieux-Québec - Loretteville", "#E04503", "#000000", R.drawable.bus)
                554u -> Line("292","Terminus Gare-du-Palais - Saint-Augustin", "#E04503", "#000000", R.drawable.bus)
                555u -> Line("315",": Univ.Laval/Cégep Garneau - Cap-Rouge", "#E04503", "#000000", R.drawable.bus)
                556u -> Line("344", "Univ.Laval/Cégep Garneau - Sainte-Thérèse-de-Lisieux", "#E04503", "#000000", R.drawable.bus)
                557u -> Line("354","Pointe-de-Sainte-Foy - Sainte-Thérèse-de-Lisieux", "#E04503", "#000000", R.drawable.bus)
                558u -> Line("355","Univ.Laval/Cégep Garneau - Sainte-Thérèse-de-Lisieux", "#E04503", "#000000", R.drawable.bus)
                559u -> Line("358","Univ.Laval/Cégep Garneau - Sainte-Thérèse-de-Lisieux", "#E04503", "#000000", R.drawable.bus)
                560u -> Line("372","Univ.Laval/Cégep Garneau - Lac-Saint-Charles", "#E04503", "#000000", R.drawable.bus)
                561u -> Line("374","Univ.Laval/Cégep Garneau - Neufchâtel", "#E04503", "#000000", R.drawable.bus)
                562u -> Line("377","Univ.Laval/Cégep Garneau - Val-Bélair", "#E04503", "#000000", R.drawable.bus)
                563u -> Line("380","Univ.Laval/Cégep Garneau - L'Ancienne-Lorette", "#E04503", "#000000", R.drawable.bus)
                564u -> Line("381","Univ.Laval/Cégep Garneau - Neufchâtel", "#E04503", "#000000", R.drawable.bus)

                565u -> Line("400","Terminus Gare-du-Palais - Sainte-Foy Centre", "#013888", "#FFFFFF", R.drawable.bus)
                566u -> Line("405","Centre Vidéotron - Hôtels centre-ville", "#013888", "#FFFFFF", R.drawable.bus)
                567u -> Line("410","Festival d'été - Hydro-Québec", "#013888", "#FFFFFF", R.drawable.bus)
                568u -> Line("420","Festival d'été - Aquarium du Québec / Sainte-Foy", "#013888", "#FFFFFF", R.drawable.bus)
                569u -> Line("430","Festival d'été - Charlesbourg", "#013888", "#FFFFFF", R.drawable.bus)
                570u -> Line("450","Festival d'été - Les Promenades Beauport", "#013888", "#FFFFFF", R.drawable.bus)
                571u -> Line("530","Sainte-Foy Centre - Station des Roses", "#E04503", "#000000", R.drawable.bus)
                572u -> Line("536","Sainte-Foy Centre - du Bourg-Royal", "#E04503", "#000000", R.drawable.bus)
                573u -> Line("537","Sainte-Foy Centre - Terminus de la Faune", "#E04503", "#000000", R.drawable.bus)
                574u -> Line("538","Sainte-Foy Centre - du Bourg-Royal", "#E04503", "#000000", R.drawable.bus)
                575u -> Line("550","Sainte-Foy Centre - Terminus Chute-Montmorency", "#E04503", "#000000", R.drawable.bus)
                576u -> Line("554","Sainte-Foy Centre - Sainte-Thérèse-de-Lisieux", "#E04503", "#000000", R.drawable.bus)
                577u -> Line("555","Sainte-Foy Centre - Sainte-Thérèse-de-Lisieux", "#E04503", "#000000", R.drawable.bus)
                578u -> Line("558","Sainte-Foy Centre - Sainte-Thérèse-de-Lisieux", "#E04503", "#000000", R.drawable.bus)
                579u -> Line("572","Sainte-Foy Centre - Lac-Saint-Charles", "#E04503", "#000000", R.drawable.bus)
                580u -> Line("574","Sainte-Foy Centre - Neufchâtel", "#E04503", "#000000", R.drawable.bus)
                581u -> Line("577","Sainte-Foy Centre - Val-Bélair", "#E04503", "#000000", R.drawable.bus)
                582u -> Line("580","Sainte-Foy Centre - L'Ancienne-Lorette", "#E04503", "#000000", R.drawable.bus)
                583u -> Line("581","Lebourgneuf / Sainte-Foy Centre - Lebourgneuf / Neufchâtel", "#E04503", "#000000", R.drawable.bus)
                584u -> Line("582","Lebourgneuf / Sainte-Foy Centre - Lebourgneuf / Saint-Émile", "#E04503", "#000000", R.drawable.bus)
                585u -> Line("584","Sainte-Foy Centre - Loretteville", "#E04503", "#000000", R.drawable.bus)
                586u -> Line("904","Loretteville", "#1A171B", "#FFFFFF", R.drawable.bus)
                587u -> Line("907","Place D'Youville - Pointe-de-Sainte-Foy", "#1A171B", "#FFFFFF", R.drawable.bus)
                588u -> Line("915","Cap-Rouge", "#1A171B", "#FFFFFF", R.drawable.bus)
                589u -> Line("925","Pointe-de-Sainte-Foy", "#1A171B", "#FFFFFF", R.drawable.bus)
                590u -> Line("931","Place D'Youville - Lac-Saint-Charles", "#1A171B", "#FFFFFF", R.drawable.bus)
                591u -> Line("936","Station des Roses", "#1A171B", "#FFFFFF", R.drawable.bus)
                592u -> Line("954","Sainte-Thérèse-de-Lisieux", "#1A171B", "#FFFFFF", R.drawable.bus)
                593u -> Line("972","Loretteville", "#1A171B", "#FFFFFF", R.drawable.bus)
                594u -> Line("980","L'Ancienne-Lorette", "#1A171B", "#FFFFFF", R.drawable.bus)
                595u -> Line("982","Lac-Saint-Charles", "#1A171B", "#FFFFFF", R.drawable.bus)
                596u -> Line("984","Neufchâtel", "#1A171B", "#FFFFFF", R.drawable.bus)
                597u -> Line("992","Saint-Augustin", "#1A171B", "#FFFFFF", R.drawable.bus)

                else -> {
                    val proposition: Line? = lookForLineProposition(
                        context,
                        operatorId.toString(),
                        id.toString(),
                        R.drawable.bus
                    )

                    return proposition ?: Line("?", "RTC ($id)", "#003878", "#ffffff", R.drawable.bus)
                }
            }
        }

        private fun getSTLLineById(context: Context, operatorId: UInt, id: UInt): Line {
            // TODO Find ids of all lines
            //   (Id of the line is on 9bits so higher
            //   than 512u means it's not known yet)
            return when (id) {
                1u -> Line("2", "Métro Henri-Bourassa / Métro Montmorency", "#151f6d", "#ffffff", R.drawable.bus)
                2u -> Line("12", "Pont-Viau / Métro Cartier", "#151f6d", "#ffffff", R.drawable.bus)
                302u -> Line("16", "Métro Montmorency / Laval-des-Rapides / Armand-Frappier", "#151f6d", "#ffffff", R.drawable.bus)
                3u -> Line("17", "Auteuil / Métro Cartier", "#151f6d", "#ffffff", R.drawable.bus)
                4u -> Line("20", "Métro Cartier / Chomedey", "#151f6d", "#ffffff", R.drawable.bus)
                264u -> Line("22", "Saint-François / Métro Cartier", "#151f6d", "#ffffff", R.drawable.bus)
                5u -> Line("24", "Métro Cartier / Sainte-Dorothée", "#151f6d", "#ffffff", R.drawable.bus)
                6u -> Line("25", "Saint-François / Métro Cartier", "#151f6d", "#ffffff", R.drawable.bus)
                7u -> Line("26", "Métro Montmorency / Gare Sainte-Dorothée", "#151f6d", "#ffffff", R.drawable.bus)
                8u -> Line("27", "Gare Vimont / Métro Cartier", "#151f6d", "#ffffff", R.drawable.bus)
                9u -> Line("28", "Saint-Vincent-De-Paul / Métro Cartier", "#151f6d", "#ffffff", R.drawable.bus)
                10u -> Line("31", "Auteuil / Métro Henri-Bourassa", "#151f6d", "#ffffff", R.drawable.bus)
                11u -> Line("33", "Métro Montmorency / Métro Cartier", "#151f6d", "#ffffff", R.drawable.bus)
                12u -> Line("37", "Sainte-Rose / Métro Cartier", "#151f6d", "#ffffff", R.drawable.bus)
                13u -> Line("39", "Auteuil / Terminus Le Carrefour", "#151f6d", "#ffffff", R.drawable.bus)
                14u -> Line("40", "Métro Montmorency / Chomedey", "#151f6d", "#ffffff", R.drawable.bus)
                15u -> Line("41", "Auteuil / Métro Cartier", "#151f6d", "#ffffff", R.drawable.bus)
                16u -> Line("42", "Saint-François / Terminus Le Carrefour", "#151f6d", "#ffffff", R.drawable.bus)
                129u -> Line("43", "Auteuil / Métro Cartier", "#151f6d", "#ffffff", R.drawable.bus)
                130u -> Line("45", "Auteuil / Métro Montmorency", "#151f6d", "#ffffff", R.drawable.bus)
                131u -> Line("46", "Laval-Ouest / Métro Montmorency", "#151f6d", "#ffffff", R.drawable.bus)
                132u -> Line("48", "Gare Vimont / Métro Cartier", "#151f6d", "#ffffff", R.drawable.bus)
                133u -> Line("50", "Saint-Vincent-De-Paul / Terminus Le Carrefour", "#151f6d", "#ffffff", R.drawable.bus)
                134u -> Line("52", "Saint-François / Métro Henri-Bourassa", "#151f6d", "#ffffff", R.drawable.bus)
                135u -> Line("55", "Laval-Ouest / Métro Henri-Bourassa", "#151f6d", "#ffffff", R.drawable.bus)
                136u -> Line("56", "Métro Montmorency / Sainte-Dorothée", "#151f6d", "#ffffff", R.drawable.bus)
                137u -> Line("58", "Saint-Vincent-De-Paul / Métro Cartier", "#151f6d", "#ffffff", R.drawable.bus)
                138u -> Line("60", "Métro Cartier / Chomedey", "#151f6d", "#ffffff", R.drawable.bus)
                139u -> Line("61", "Fabreville / Métro Montmorency", "#151f6d", "#ffffff", R.drawable.bus)
                140u -> Line("63", "Gare Sainte-Rose / Métro Cartier", "#151f6d", "#ffffff", R.drawable.bus)
                141u -> Line("65", "Gare Sainte-Rose / Métro Montmorency", "#151f6d", "#ffffff", R.drawable.bus)
                142u -> Line("66", "Terminus Le Carrefour / Sainte-Dorothée", "#151f6d", "#ffffff", R.drawable.bus)
                143u -> Line("70", "Métro Cartier / Métro Montmorency", "#151f6d", "#ffffff", R.drawable.bus)
                144u -> Line("73", "Fabreville / Métro Cartier", "#151f6d", "#ffffff", R.drawable.bus)
                145u -> Line("74", "Saint-François / Métro Cartier", "#151f6d", "#ffffff", R.drawable.bus)
                146u -> Line("76", "Métro Montmorency / Gare Sainte-Dorothée", "#151f6d", "#ffffff", R.drawable.bus)
                147u -> Line("144", "Métro Côte-Vertu / Sainte-Dorothée", "#151f6d", "#ffffff", R.drawable.bus)
                148u -> Line("151", "Sainte-Rose / Métro Côte-Vertu", "#151f6d", "#ffffff", R.drawable.bus)
                190u -> Line("222", "Saint-Vincent-De-Paul / Métro Cartier", "#151f6d", "#ffffff", R.drawable.bus)
                149u -> Line("228", "Saint-Vincent-De-Paul / Métro Cartier", "#151f6d", "#ffffff", R.drawable.bus)
                150u -> Line("252", "Saint-François / Métro Henri-Bourassa", "#151f6d", "#ffffff", R.drawable.bus)
                64u -> Line("313", "Chomedey / Métro Côte-Vertu", "#151f6d", "#ffffff", R.drawable.bus)
                27u -> Line("322", "Duvernay / Métro Henri-Bourassa", "#151f6d", "#ffffff", R.drawable.bus)
                151u -> Line("402", "Sainte-Rose / Sainte-Dorothée", "#151f6d", "#ffffff", R.drawable.bus)
                152u -> Line("404", "Sainte-Dorothée / Gare Sainte-Dorothée", "#151f6d", "#ffffff", R.drawable.bus)
                299u -> Line("713", "Sainte-Dorothée / Métro Côte-Vertu", "#151f6d", "#ffffff", R.drawable.bus)
                290u -> Line("730", "Gare Sainte-Dorothée / Montréal", "#151f6d", "#ffffff", R.drawable.bus)
                295u -> Line("744", "Métro Côte-Vertu / Gare Sainte-Dorothée", "#151f6d", "#ffffff", R.drawable.bus)
                153u -> Line("901", "Saint-François / Métro Cartier", "#151f6d", "#ffffff", R.drawable.bus)
                154u -> Line("902", "Terminus Le Carrefour / Métro Côte-Vertu", "#151f6d", "#ffffff", R.drawable.bus)
                155u -> Line("903", "Gare Sainte-Dorothée / Métro Montmorency", "#151f6d", "#ffffff", R.drawable.bus)
                193u -> Line("925", "Saint-François / Métro Radisson", "#151f6d", "#ffffff", R.drawable.bus)
                270u -> Line("942", "Saint-François / Métro Montmorency", "#151f6d", "#ffffff", R.drawable.bus)

                530u -> Line("36", "Métro Montmorency / Chomedey", "#151f6d", "#ffffff", R.drawable.bus)
                529u -> Line("345", "Gare Vimont / Métro Henri-Bourassa", "#151f6d", "#ffffff", R.drawable.bus)

                602u -> Line("3C", "Laval Senior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                603u -> Line("4C", "Laval Senior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                604u -> Line("5C", "Laval Senior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                605u -> Line("6C", "Laval Senior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                606u -> Line("8C", "Laval Senior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                607u -> Line("11C", "Laval Senior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                608u -> Line("12C", "Laval Senior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                609u -> Line("13C", "Laval Senior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                610u -> Line("14C", "Laval Senior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                611u -> Line("15C", "Laval Senior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                612u -> Line("16C", "Laval Senior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                613u -> Line("18C", "Laval Senior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                590u -> Line("45C", "Horizon-Jeunesse", "#151f6d", "#ffffff", R.drawable.bus)
                588u -> Line("49C", "CQPEL", "#151f6d", "#ffffff", R.drawable.bus)
                589u -> Line("59C", "Georges-Vanier", "#151f6d", "#ffffff", R.drawable.bus)
                583u -> Line("71C", "Collège Letendre", "#151f6d", "#ffffff", R.drawable.bus)
                584u -> Line("73C", "Collège Letendre", "#151f6d", "#ffffff", R.drawable.bus)
                585u -> Line("75C", "Collège Letendre", "#151f6d", "#ffffff", R.drawable.bus)
                586u -> Line("76C", "Collège Letendre", "#151f6d", "#ffffff", R.drawable.bus)
                587u -> Line("78C", "Collège Letendre", "#151f6d", "#ffffff", R.drawable.bus)
                562u -> Line("80C", "Collège Laval", "#151f6d", "#ffffff", R.drawable.bus)
                563u -> Line("81C", "Collège Laval", "#151f6d", "#ffffff", R.drawable.bus)
                564u -> Line("82C", "Collège Laval", "#151f6d", "#ffffff", R.drawable.bus)
                565u -> Line("83C", "Collège Laval", "#151f6d", "#ffffff", R.drawable.bus)
                566u -> Line("84C", "Collège Laval", "#151f6d", "#ffffff", R.drawable.bus)
                567u -> Line("85C", "Collège Laval", "#151f6d", "#ffffff", R.drawable.bus)
                568u -> Line("86C", "Collège Laval", "#151f6d", "#ffffff", R.drawable.bus)
                569u -> Line("87C", "Collège Laval", "#151f6d", "#ffffff", R.drawable.bus)
                570u -> Line("88C", "Collège Laval", "#151f6d", "#ffffff", R.drawable.bus)
                571u -> Line("89C", "Collège Laval", "#151f6d", "#ffffff", R.drawable.bus)
                591u -> Line("91C", "Laval Junior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                592u -> Line("94C", "Laval Junior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                593u -> Line("95C", "Laval Junior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                594u -> Line("97C", "Laval Junior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                595u -> Line("99C", "Laval Junior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                614u -> Line("110C", "Laval Senior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                615u -> Line("111C", "Laval Senior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                616u -> Line("112C", "Laval Senior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                572u -> Line("183C", "Collège Laval", "#151f6d", "#ffffff", R.drawable.bus)
                573u -> Line("185C", "Collège Laval", "#151f6d", "#ffffff", R.drawable.bus)
                574u -> Line("188C", "Collège Laval", "#151f6d", "#ffffff", R.drawable.bus)
                575u -> Line("189C", "Collège Laval", "#151f6d", "#ffffff", R.drawable.bus)
                596u -> Line("193C", "Laval Junior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                597u -> Line("194C", "Laval Junior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                598u -> Line("196C", "Laval Junior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                599u -> Line("197C", "Laval Junior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                600u -> Line("198C", "Laval Junior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                601u -> Line("199C", "Laval Junior Academy", "#151f6d", "#ffffff", R.drawable.bus)
                560u -> Line("240C", "Collège Citoyen", "#151f6d", "#ffffff", R.drawable.bus)
                561u -> Line("241C", "Collège Citoyen", "#151f6d", "#ffffff", R.drawable.bus)
                576u -> Line("281C", "Collège Laval", "#151f6d", "#ffffff", R.drawable.bus)
                577u -> Line("282C", "Collège Laval", "#151f6d", "#ffffff", R.drawable.bus)
                578u -> Line("283C", "Collège Laval", "#151f6d", "#ffffff", R.drawable.bus)
                579u -> Line("284C", "Collège Laval", "#151f6d", "#ffffff", R.drawable.bus)
                580u -> Line("285C", "Collège Laval", "#151f6d", "#ffffff", R.drawable.bus)
                581u -> Line("288C", "Collège Laval", "#151f6d", "#ffffff", R.drawable.bus)
                582u -> Line("289C", "Collège Laval", "#151f6d", "#ffffff", R.drawable.bus)

                512u -> Line("T01", "Rue Gaumont / Boul De la Concorde", "#151f6d", "#ffffff", R.drawable.bus)
                513u -> Line("T02", "Rue Gaumont / Boul De la Concorde", "#151f6d", "#ffffff", R.drawable.bus)
                514u -> Line("T03", "Plastiques Balcan / Boul des Laurentides", "#151f6d", "#ffffff", R.drawable.bus)
                515u -> Line("T07", "Montée Saint-François / Boul Dagenais", "#151f6d", "#ffffff", R.drawable.bus)
                516u -> Line("T10", "Avenue des Perron / Boul Sainte-Rose", "#151f6d", "#ffffff", R.drawable.bus)
                517u -> Line("T11", "Rue Louis-B.-Mayer / Boul Saint-Elzéar", "#151f6d", "#ffffff", R.drawable.bus)
                518u -> Line("T12", "Boul Cléroux / Boul Saint-Martin", "#151f6d", "#ffffff", R.drawable.bus)
                519u -> Line("T18", "Rang Saint-Antoine / Montée Champagne", "#151f6d", "#ffffff", R.drawable.bus)
                520u -> Line("T19", "Avenue Marcel-Villeneuve / Rue Duchesneau", "#151f6d", "#ffffff", R.drawable.bus)
                521u -> Line("T21", "Voie de désserte A440 sud", "#151f6d", "#ffffff", R.drawable.bus)
                522u -> Line("T22", "Boul Dagenais / Rue Ovide", "#151f6d", "#ffffff", R.drawable.bus)
                523u -> Line("T23", "Rue Frégault / Terminus Le Carrefour", "#151f6d", "#ffffff", R.drawable.bus)
                524u -> Line("T26", "Gare Île Bigras / Chemin du Bord-de-l'Eau", "#151f6d", "#ffffff", R.drawable.bus)
                525u -> Line("T27", "Stationnement Gare Ste-Dorothée", "#151f6d", "#ffffff", R.drawable.bus)
                526u -> Line("T28", "Stationnement Gare Ste-Dorothée", "#151f6d", "#ffffff", R.drawable.bus)
                527u -> Line("T29", "Rue Étienne-Lavoie / Boul Notre-Dame", "#151f6d", "#ffffff", R.drawable.bus)

                else -> {
                    val proposition: Line? = lookForLineProposition(
                        context,
                        operatorId.toString(),
                        id.toString(),
                        R.drawable.bus
                    )

                    return proposition ?: Line("?", "STL ($id)", "#151f6d", "#ffffff", R.drawable.bus)
                }
            }
        }

        private fun getSTLevisLineById(context: Context, operatorId: UInt, id: UInt): Line {
            // TODO Find ids of all lines
            //   (Id of the line is on 9bits so higher
            //   than 512u means it's not known yet)
            return when (id) {
                45u -> Line("L2", "Lévisien 2", "#1E4289", "#FFFFFF", R.drawable.bus)
                89u -> Line("L3,", "Lévisien 3", "#1E4289", "#FFFFFF", R.drawable.bus)

                512u -> Line("11", "Lauzon / Lévis centre", "#0091B3", "#FFFFFF", R.drawable.bus)
                513u -> Line("11A", "Lévis centre / Lauzon", "#0091B3", "#FFFFFF", R.drawable.bus)
                514u -> Line("13", "Saint-David / Vieux-Lévis", "#4EA685", "#FFFFFF", R.drawable.bus)
                515u -> Line("15", "Pintendre", "#0091B3", "#FFFFFF", R.drawable.bus)
                516u -> Line("22", "Saint-Nicolas - Bernières", "#0091B3", "#FFFFFF", R.drawable.bus)
                517u -> Line("23", "Saint-Nicolas - Village", "#0091B3", "#FFFFFF", R.drawable.bus)
                518u -> Line("23E", "Express Saint-Nicolas - Village", "#FFA400", "#FFFFFF", R.drawable.bus)
                519u -> Line("24", "Saint-Rédempteur", "#4EA685", "#FFFFFF", R.drawable.bus)
                520u -> Line("24E", "Express Saint-Rédempteur", "#FFA400", "#FFFFFF", R.drawable.bus)
                521u -> Line("31", "Sainte-Hélène-de-Breakeyville", "#0091B3", "#FFFFFF", R.drawable.bus)
                522u -> Line("34", "Saint-Romuald", "#0091B3", "#FFFFFF", R.drawable.bus)
                523u -> Line("34E", "Saint-Romuald / Sainte-Foy entre", "#FFA400", "#FFFFFF", R.drawable.bus)
                524u -> Line("35", "Charny via de l'Eau-Vive", "#0091B3", "#FFFFFF", R.drawable.bus)
                525u -> Line("36", "Charny via du C.-Hospitalier", "#0091B3", "#FFFFFF", R.drawable.bus)
                526u -> Line("36E", "Charny / Sainte-Foy entre", "#FFA400", "#FFFFFF", R.drawable.bus)
                527u -> Line("37", "Saint-Jean-de-Crysostome via Taniata", "#0091B3", "#FFFFFF", R.drawable.bus)
                528u -> Line("37E", "Saint-Jean-de-Crysostome / Sainte-Foy entre", "#FFA400", "#FFFFFF", R.drawable.bus)
                529u -> Line("38", "Saint-Jean-de-Crysostome via Vanier", "#0091B3", "#FFFFFF", R.drawable.bus)
                530u -> Line("38E", "Saint-Jean-de-Crysostome / Sainte-Foy entre", "#FFA400", "#FFFFFF", R.drawable.bus)
                531u -> Line("39", "Saint-Jean-de-Crysostome / Charny", "#4EA685", "#FFFFFF", R.drawable.bus)
                532u -> Line("42E", "Lévis centre / Cégep Garneau", "#FFA400", "#FFFFFF", R.drawable.bus)
                533u -> Line("43E", "Parc-Relais-Bus des Rivières / Cégep Garneau", "#FFA400", "#FFFFFF", R.drawable.bus)
                534u -> Line("65", "Saint-Lambert-de-Lauzon", "#4EA685", "#FFFFFF", R.drawable.bus)
                535u -> Line("111", "Lauzon - Saint-Romuald / Pointe-Lévy - Juvénat", "#7E4082", "#FFFFFF", R.drawable.bus)
                536u -> Line("115", "Lauzon / Vieux-Lévis", "#7E4082", "#FFFFFF", R.drawable.bus)
                537u -> Line("119", "Lauzon / Pointe-Lévy", "#7E4082", "#FFFFFF", R.drawable.bus)
                538u -> Line("125", "Lauzon / Vieux-Lévis", "#7E4082", "#FFFFFF", R.drawable.bus)
                539u -> Line("126", "Étienne-Dallaire / Vieux-Lévis", "#7E4082", "#FFFFFF", R.drawable.bus)
                540u -> Line("127", "Lauzon / Vieux-Lévis", "#7E4082", "#FFFFFF", R.drawable.bus)
                541u -> Line("129", "Lauzon / Vieux-Lévis", "#7E4082", "#FFFFFF", R.drawable.bus)
                542u -> Line("131", "Saint-David - Pintendre / Juvénat", "#7E4082", "#FFFFFF", R.drawable.bus)
                543u -> Line("135", "Saint-David / Vieux-Lévis", "#7E4082", "#FFFFFF", R.drawable.bus)
                544u -> Line("136", "Saint-David / Vieux-Lévis", "#7E4082", "#FFFFFF", R.drawable.bus)
                545u -> Line("137", "Saint-David / Pointe-Lévy", "#7E4082", "#FFFFFF", R.drawable.bus)
                546u -> Line("138", "Saint-David / Lévis centre", "#7E4082", "#FFFFFF", R.drawable.bus)
                547u -> Line("139", "Saint-David / Pointe-Lévy", "#7E4082", "#FFFFFF", R.drawable.bus)
                548u -> Line("155", "Pintendre / Vieux-Lévis", "#7E4082", "#FFFFFF", R.drawable.bus)
                549u -> Line("156", "Pintendre / Vieux-Lévis", "#7E4082", "#FFFFFF", R.drawable.bus)
                550u -> Line("158", "Pintendre / Pointe-Lévy", "#7E4082", "#FFFFFF", R.drawable.bus)
                551u -> Line("221", "Saint-Nicolas - Bernières / Juvénat", "#7E4082", "#FFFFFF", R.drawable.bus)
                552u -> Line("222", "Saint-Nicolas / Juvénat", "#7E4082", "#FFFFFF", R.drawable.bus)
                553u -> Line("225", "Saint-Nicolas - Bernières / UQAR - Lévis centre", "#7E4082", "#FFFFFF", R.drawable.bus)
                554u -> Line("229", "Saint-Nicolas - Bernières", "#7E4082", "#FFFFFF", R.drawable.bus)
                555u -> Line("231", "Saint-Nicolas - Village / Juvénat", "#7E4082", "#FFFFFF", R.drawable.bus)
                556u -> Line("232", "Saint-Nicolas - Roc-Pointe / Juvénat", "#7E4082", "#FFFFFF", R.drawable.bus)
                557u -> Line("235", "Saint-Nicolas - Roc-Pointe / UQAR - Lévis centre", "#7E4082", "#FFFFFF", R.drawable.bus)
                558u -> Line("241", "Saint-Rédempteur / Juvénat", "#7E4082", "#FFFFFF", R.drawable.bus)
                559u -> Line("242", "Saint-Rédempteur / Juvénat", "#7E4082", "#FFFFFF", R.drawable.bus)
                560u -> Line("245", "Saint-Rédempteur / UQAR - Lévis centre", "#7E4082", "#FFFFFF", R.drawable.bus)
                561u -> Line("311", "Breakeyville - Saint-Jean-de-Crysostome / Juvénat", "#7E4082", "#FFFFFF", R.drawable.bus)
                562u -> Line("325", "Saint-Nicolas - Saint-Romuald / Lévis centre", "#7E4082", "#FFFFFF", R.drawable.bus)
                563u -> Line("345", "Saint-Romuald / Lévis centre", "#7E4082", "#FFFFFF", R.drawable.bus)
                564u -> Line("351", "Charny / Juvénat", "#7E4082", "#FFFFFF", R.drawable.bus)
                565u -> Line("355", "Charny - Saint-David / Lévis centre", "#7E4082", "#FFFFFF", R.drawable.bus)
                566u -> Line("375", "Saint-Jean-de-Crysostome / UQAR - Lévis centre", "#7E4082", "#FFFFFF", R.drawable.bus)
                567u -> Line("381", "Saint-Jean-de-Crysostome / Juvénat", "#7E4082", "#FFFFFF", R.drawable.bus)
                568u -> Line("385", "Saint-Jean-de-Crysostome / UQAR - Lévis centre", "#7E4082", "#FFFFFF", R.drawable.bus)
                569u -> Line("L1", "Lévisien 1", "#4EA685", "#FFFFFF", R.drawable.bus)
                572u -> Line("L4,", "Lévisien 4", "#4EA685", "#FFFFFF", R.drawable.bus)

                573u -> Line("T1", "Chemin des Îles", "#A47400", "#FFFFFF", R.drawable.bus)
                574u -> Line("T2", "Rue Saint-Laurent", "#A47400", "#FFFFFF", R.drawable.bus)
                575u -> Line("T15", "Pintendre", "#A47400", "#FFFFFF", R.drawable.bus)
                576u -> Line("T22", "Parc industriel de Bernières", "#A47400", "#FFFFFF", R.drawable.bus)
                577u -> Line("T31", "Sainte-Hélène-de-Breakeyville", "#A47400", "#FFFFFF", R.drawable.bus)
                578u -> Line("T34", "Saint-Romuald", "#A47400", "#FFFFFF", R.drawable.bus)
                579u -> Line("T65", "Saint-Lambert-de-Lauzon", "#A47400", "#FFFFFF", R.drawable.bus)
                580u -> Line("T66", "Saint-Lambert-de-Lauzon", "#A47400", "#FFFFFF", R.drawable.bus)

                else -> {
                    val proposition: Line? = lookForLineProposition(
                        context,
                        operatorId.toString(),
                        id.toString(),
                        R.drawable.bus
                    )

                    return proposition ?: Line("?", "STLévis ($id)", "#0091b3", "#ffffff", R.drawable.bus)
                }
            }
        }

        private fun getMRCJolietteLineById(context: Context, operatorId: UInt, id: UInt): Line {
            // TODO Find ids of all lines
            //   (Id of the line is on 9bits so higher
            //   than 512u means it's not known yet)
            //   Note: The line id is most likely not relevant...
            //      (pretty much one fare per line)
            return when (id) {
                1u -> Line("125", "Saint-Donat / Chertsey / Montréal", "#81a449", "#ffffff", R.drawable.bus)

                512u -> Line("32", "Joliette / Saint-Michel-des-Saints", "#81a449", "#ffffff", R.drawable.bus)
                513u -> Line("34", "Joliette / Rawdon", "#81a449", "#ffffff", R.drawable.bus)
                514u -> Line("35", "Saint-Lin-Laurentides / Saint-Jérôme", "#81a449", "#ffffff", R.drawable.bus)
                515u -> Line("50", "Joliette / Montréal", "#81a449", "#ffffff", R.drawable.bus)
                516u -> Line("131-138", "Joliette / Berthierville", "#81a449", "#ffffff", R.drawable.bus)

                else -> {
                    val proposition: Line? = lookForLineProposition(
                        context,
                        operatorId.toString(),
                        id.toString(),
                        R.drawable.bus
                    )

                    return proposition ?: Line("?", "MRC Joliette ($id)", "#81a449", "#ffffff", R.drawable.bus)
                }
            }
        }

        private fun getREMLineById(context: Context, operatorId: UInt, id: UInt): Line {
            // TODO Find ids of all lines
            //   (Id of the line is on 9bits so higher
            //   than 512u means it's not known yet)
            return when (id) {
                2u -> Line("A", "Ligne A", "#82bf00", "#000000", R.drawable.lightmetro)

                else -> {
                    val proposition: Line? = lookForLineProposition(
                        context,
                        operatorId.toString(),
                        id.toString(),
                        R.drawable.lightmetro
                    )

                    return proposition ?: Line("?", "REM ($id)", "#82bf00", "#000000", R.drawable.lightmetro)
                }
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