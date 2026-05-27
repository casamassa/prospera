package com.casamassa.prospera.data.repository

import com.casamassa.prospera.data.local.dao.TransactionDao
import com.casamassa.prospera.data.mapper.toDomain
import com.casamassa.prospera.data.mapper.toEntity
import com.casamassa.prospera.domain.model.FinancialTransaction
import com.casamassa.prospera.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepositoryImpl(
    private val dao: TransactionDao
) : TransactionRepository {
    override suspend fun insertTransaction(transaction: FinancialTransaction): Long {
        return dao.insert(transaction.toEntity())
    }

    override suspend fun deleteTransaction(transaction: FinancialTransaction) {
        dao.delete(transaction.toEntity())
    }

    override fun getTransactionsByDateRange(startMillis: Long, endMillis: Long): Flow<List<FinancialTransaction>> {
        return dao.getTransactionsByDateRange(startMillis, endMillis).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTransactionsByAccount(accountId: Long): Flow<List<FinancialTransaction>> {
        return dao.getTransactionsByAccount(accountId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
