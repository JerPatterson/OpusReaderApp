package com.transition.ora.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(tableName = "card_proposition", primaryKeys = [ "operatorId", "idOnCard", "type" ])
data class CardPropositionEntity(
    @ColumnInfo(name = "operatorId")
    val operatorId: String,
    @ColumnInfo(name = "idOnCard")
    val idOnCard: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "color")
    val color: String,
    @ColumnInfo(name = "textColor")
    val textColor: String,
)