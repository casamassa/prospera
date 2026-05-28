package com.casamassa.prospera.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.casamassa.prospera.data.local.entities.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
@JvmSuppressWildcards
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: AccountEntity): Long

    @Update
    suspend fun update(account: AccountEntity): Int

    @Query("SELECT * FROM contas WHERE is_active = 1")
    fun getAllActiveAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM contas WHERE id = :id")
    suspend fun getAccountById(id: Long): AccountEntity?

    @Query("UPDATE contas SET saldo_atual = :newBalance WHERE id = :id")
    suspend fun updateAccountBalance(id: Long, newBalance: Double): Int

    @Delete
    suspend fun delete(account: AccountEntity): Int
}
