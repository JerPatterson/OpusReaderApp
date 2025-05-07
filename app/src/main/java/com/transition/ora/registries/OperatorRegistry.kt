package com.transition.ora.registries

import com.transition.ora.Operator
import com.transition.ora.R

object OperatorRegistry {
    private val operatorMap: Map<UInt, Operator> = mapOf(
        1u to Operator("ARTM", "#007373", R.drawable.artm),
        2u to Operator("STM", "#00aeef", R.drawable.stm),
        3u to Operator("RTL", "#9e2536", R.drawable.rtl),
        4u to Operator("exo", "#000000", R.drawable.exo),
        5u to Operator("RTC", "#003878", R.drawable.rtc),
        6u to Operator("STL", "#151f6d", R.drawable.stl),
        7u to Operator("exo", "#000000", R.drawable.exo),
        8u to Operator("exo", "#000000", R.drawable.exo),
        9u to Operator("exo", "#000000", R.drawable.exo),
        10u to Operator("exo", "#000000", R.drawable.exo),
        11u to Operator("exo", "#000000", R.drawable.exo),
        12u to Operator("exo", "#000000", R.drawable.exo),
        13u to Operator("exo", "#000000", R.drawable.exo),
        14u to Operator("exo", "#000000", R.drawable.exo),
        15u to Operator("exo", "#000000", R.drawable.exo),
        16u to Operator("STLévis", "#0091b3", R.drawable.stlevis),
        17u to Operator("exo", "#000000", R.drawable.exo),
        18u to Operator("exo", "#000000", R.drawable.exo),
        19u to Operator("exo", "#000000", R.drawable.exo),
        20u to Operator("MRCJoliette", "#81a449", R.drawable.mrcjoliette),
        21u to Operator("exo", "#000000", R.drawable.exo),
        22u to Operator("REM", "#034638", R.drawable.rem)
    )


    fun get(id: UInt): Operator? = operatorMap[id]
}