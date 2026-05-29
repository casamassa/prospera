package com.casamassa.prospera.domain.repository

import com.casamassa.prospera.domain.model.FinancialTransaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun insertTransaction(transaction: FinancialTransaction): Long
    suspend fun updateTransaction(transaction: FinancialTransaction)
    suspend fun deleteTransaction(transaction: FinancialTransaction)
    suspend fun getTransactionById(id: Long): FinancialTransaction?
    fun getTransactionsByDateRange(startMillis: Long, endMillis: Long): Flow<List<FinancialTransaction>>
    fun getTransactionsByAccount(accountId: Long): Flow<List<FinancialTransaction>>
}
