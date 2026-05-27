package com.casamassa.prospera.data.repository

import com.casamassa.prospera.data.local.dao.AccountDao
import com.casamassa.prospera.data.mapper.toDomain
import com.casamassa.prospera.data.mapper.toEntity
import com.casamassa.prospera.domain.model.Account
import com.casamassa.prospera.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AccountRepositoryImpl(
    private val dao: AccountDao
) : AccountRepository {
    override suspend fun insertAccount(account: Account): Long {
        return dao.insert(account.toEntity())
    }

    override suspend fun updateAccount(account: Account) {
        dao.update(account.toEntity())
    }

    override fun getAllActiveAccounts(): Flow<List<Account>> {
        return dao.getAllActiveAccounts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getAccountById(id: Long): Account? {
        return dao.getAccountById(id)?.toDomain()
    }

    override suspend fun updateAccountBalance(id: Long, newBalance: Double) {
        dao.updateAccountBalance(id, newBalance)
    }
}
