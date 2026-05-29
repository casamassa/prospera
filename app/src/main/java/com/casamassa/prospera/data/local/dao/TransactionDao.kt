package com.casamassa.prospera.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Transaction
import com.casamassa.prospera.data.local.entities.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
@JvmSuppressWildcards
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity): Long

    @Update
    suspend fun update(transaction: TransactionEntity): Int

    @Delete
    suspend fun delete(transaction: TransactionEntity): Int

    @Query("SELECT * FROM lancamentos WHERE id = :id")
    suspend fun getTransactionById(id: Long): TransactionEntity?

    @Query("""
        SELECT l.* FROM lancamentos l 
        INNER JOIN contas c ON l.conta_id = c.id 
        WHERE c.is_active = 1 
        AND l.data_timestamp BETWEEN :startMillis AND :endMillis
    """)
    fun getTransactionsByDateRange(startMillis: Long, endMillis: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM lancamentos WHERE conta_id = :accountId")
    fun getTransactionsByAccount(accountId: Long): Flow<List<TransactionEntity>>
}
