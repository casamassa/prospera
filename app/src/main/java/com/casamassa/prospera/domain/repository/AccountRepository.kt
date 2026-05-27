package com.casamassa.prospera.domain.repository

import com.casamassa.prospera.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    suspend fun insertAccount(account: Account): Long
    suspend fun updateAccount(account: Account)
    fun getAllActiveAccounts(): Flow<List<Account>>
    suspend fun getAccountById(id: Long): Account?
    suspend fun updateAccountBalance(id: Long, newBalance: Double)
}
