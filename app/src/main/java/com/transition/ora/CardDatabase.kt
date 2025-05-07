package com.transition.ora

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `card_proposition` (`operatorId` TEXT NOT NULL, `idOnCard` TEXT NOT NULL, `type` TEXT NOT NULL, `id` TEXT NOT NULL, `name` TEXT NOT NULL, `color` TEXT NOT NULL, `textColor` TEXT NOT NULL, PRIMARY KEY(`operatorId`, `idOnCard`, `type`))")
    }
}

private val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE card ADD COLUMN birthDate TEXT")
    }
}

private val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE card ADD COLUMN typeVariant TEXT")
    }
}


@Database(entities = [CardEntity::class, CardPropositionEntity::class], version = 4)
abstract class CardDatabase: RoomDatabase() {
    abstract val dao: CardDao
    abstract val daoProposition: CardPropositionDao

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
            ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4).build()
        }
    }
}