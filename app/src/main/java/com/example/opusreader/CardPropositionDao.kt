package com.example.opusreader

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert


@Dao
interface CardPropositionDao {
    @Upsert
    suspend fun insertStoredProposition(proposition: CardPropositionEntity)

    @Query("DELETE FROM card_proposition")
    suspend fun deleteStoredPropositions()

    @Query("SELECT * FROM card_proposition WHERE operatorId = :operatorId and idOnCard = :idOnCard and type = :type LIMIT 1")
    fun getStoredPropositionById(operatorId: String, idOnCard: String, type: String): CardPropositionEntity?

    @Query("SELECT * FROM card_proposition WHERE operatorId = :operatorId and idOnCard = :idOnCard and type = :type LIMIT 1")
    fun getLiveStoredPropositionById(operatorId: String, idOnCard: String, type: String): LiveData<CardPropositionEntity?>
}