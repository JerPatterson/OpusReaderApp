package com.example.opusreader

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [CardEntity::class], version = 1)
abstract class CardDatabase: RoomDatabase() {
    abstract val dao: CardDao
}