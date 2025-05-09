package com.transition.ora.types

import com.transition.ora.enums.FareZones


data class FareProduct(
    val name: String,
    val descriptionStringId: Int,
    val includedZones: FareZones? = null
)