package com.transition.ora.types

import android.content.Context
import com.transition.ora.enums.FareZones


class FareProduct(
    val nameStringId: Int,
    val descriptionStringId: Int,
    val includedZones: FareZones? = null
) {
    private var customName: String? = null

    fun setName(name: String) {
        customName = name
    }

    fun getName(context: Context): String {
        return customName ?: context.getString(nameStringId)
    }
}