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
    suspend fun deleteStoredCard(card: CardEntity)

    @Query("SELECT * FROM card WHERE id = :id")
    fun getStoredCardById(id: String): Flow<List<CardEntity>>

    @Query("SELECT DISTINCT * FROM card ORDER BY scanDate")
    fun getStoredCards(): Flow<List<CardEntity>>
}