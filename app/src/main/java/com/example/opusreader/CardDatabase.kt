package com.example.opusreader

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [CardEntity::class], version = 1)
abstract class CardDatabase: RoomDatabase() {
    abstract val dao: CardDao

    companion object {
        private const val DB_NAME = "cards"

        @Volatile
        private var instance: CardDatabase? = null

        @Synchronized
        fun getInstance(context: Context): CardDatabase {
            if (instance == null) instance = create(context)
            return instance as CardDatabase
        }

        private fun create(context: Context): CardDatabase {
            return Room.databaseBuilder(
                context,
                CardDatabase::class.java,
                this.DB_NAME
            ).build()
        }
    }
}