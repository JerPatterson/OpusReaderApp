package com.example.opusreader

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Upsert
    suspend fun insertStoredCard(card: CardEntity)

    @Delete
    suspend fun deleteStoredCardScan(card: CardEntity)

    @Query("DELETE FROM card WHERE id = :id")
    suspend fun deleteStoredCard(id: String)

    @Query("DELETE FROM card")
    suspend fun deleteStoredCards()

    @Query("SELECT * FROM card WHERE id = :id ORDER BY scanDate DESC")
    fun getStoredCardById(id: String): List<CardEntity>

    @Query("SELECT * FROM card WHERE scanDate IN(SELECT max(scanDate) FROM card GROUP BY id) ORDER BY scanDate DESC")
    fun getStoredCards(): List<CardEntity>
}