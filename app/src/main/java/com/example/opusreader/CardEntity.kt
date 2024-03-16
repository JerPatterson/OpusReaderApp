package com.example.opusreader

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(tableName = "card", primaryKeys = [ "id", "scanDate" ])
data class CardEntity(
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "scanDate")
    val scanDate: String,
    @ColumnInfo(name = "expiryDate")
    val expiryDate: String,
    @ColumnInfo(name = "fares")
    val fares: String,
    @ColumnInfo(name = "trips")
    val trips: String,
)
